
<template>
  <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}" style="width: 100%">
    <div class="table-page-search-wrapper">
      <a-alert
        message="审批效能分析"
        description="展示近30天审批效率各项指标和人员绩效分析"
        type="success"
        show-icon        style="margin-bottom: 24px"
      />

      <!-- 核心效能指标 -->
      <a-row :gutter="24" style="margin-bottom: 24px">
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="总审批数"
              :value="totalApprovals"
              :value-style="{ color: '#1890ff' }"
            >
              <template #prefix>
                <a-icon type="file-done" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="已处理审批"
              :value="processedApprovals"
              :value-style="{ color: '#52c41a' }"
            >
              <template #prefix>
                <a-icon type="check" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="平均处理时间"
              :value="averageProcessingTimeValue"
              :precision="1"
              suffix="小时"
              :value-style="{ color: '#faad14' }"
            >
              <template #prefix>
                <a-icon type="clock-circle" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="分析周期"
              :value="analysisPeriod"
            >
              <template #prefix>
                <a-icon type="calendar" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>

      <!-- 图表展示区域 -->
      <a-tabs default-active-key="1">
        <!-- 效能趋势分析 -->
        <a-tab-pane key="1" tab="效能趋势分析">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-card title="近30天审批效能趋势" :bordered="false">
                <apexchart
                  width="100%"
                  height="400"
                  type="area"
                  :options="efficiencyTrendOptions"
                  :series="efficiencyTrendSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>

          <a-row :gutter="24" style="margin-top: 24px">
            <a-col :span="12">
              <a-card title="审批状态分布" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="donut"
                  :options="statusDistributionOptions"
                  :series="statusDistributionSeries"
                ></apexchart>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card title="处理时效分析" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="radialBar"
                  :options="processingTimeOptions"
                  :series="[parseFloat(averageProcessingTimeValue) || 0]"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>

        <!-- 审批人员绩效 -->
        <a-tab-pane key="2" tab="审批人员绩效">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-card title="审批人员效能对比" :bordered="false">
                <apexchart
                  width="100%"
                  height="400"
                  type="bar"
                  :options="performanceChartOptions"
                  :series="performanceChartSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>

          <a-row :gutter="24" style="margin-top: 24px">
            <a-col :span="12">
              <a-card title="人员处理量排行" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="bar"
                  :options="volumeRankingOptions"
                  :series="volumeRankingSeries"
                ></apexchart>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card title="人员效率评级" :bordered="false">
                <a-descriptions :column="1" bordered>
                  <a-descriptions-item
                    v-for="(perf, id) in approverPerformance"
                    :key="id"
                    :label="perf.staffName"
                  >
                    <a-tag :color="getEfficiencyColor(perf.efficiencyRating)">
                      {{ perf.efficiencyRating }}
                    </a-tag>
                  </a-descriptions-item>
                </a-descriptions>
                <div v-if="Object.keys(approverPerformance).length === 0" style="text-align: center; padding: 24px;">
                  <a-icon type="usergroup-delete" style="font-size: 48px; color: #bfbfbf;" />
                  <p style="margin-top: 16px;">暂无审批人员数据</p>
                </div>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>

        <!-- 瓶颈分析 -->
        <a-tab-pane key="3" tab="瓶颈分析">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-card title="审批瓶颈位置" :bordered="false">
                <a-list
                  :data-source="bottleneckPositions"
                  :pagination="false"
                >
                  <a-list-item slot="renderItem" slot-scope="item">
                    <a-list-item-meta>
                      <a slot="title">{{ item }}</a>
                    </a-list-item-meta>
                  </a-list-item>
                </a-list>
                <div v-if="bottleneckPositions.length === 0" style="text-align: center; padding: 24px;">
                  <a-icon type="check-circle" style="font-size: 48px; color: #52c41a;" />
                  <p style="margin-top: 16px;">暂无瓶颈问题</p>
                </div>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card title="优化建议" :bordered="false">
                <a-list
                  :data-source="optimizationSuggestions"
                  :pagination="false"
                >
                  <a-list-item slot="renderItem" slot-scope="item">
                    <a-list-item-meta>
                      <a slot="title">{{ item }}</a>
                    </a-list-item-meta>
                  </a-list-item>
                </a-list>
                <div v-if="optimizationSuggestions.length === 0" style="text-align: center; padding: 24px;">
                  <a-icon type="bulb" style="font-size: 48px; color: #1890ff;" />
                  <p style="margin-top: 16px;">暂无优化建议</p>
                </div>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-card>
</template>

<script>export default {
  name: 'ApprovalEfficiency',
  data () {
    return {
      loading: false,
      totalApprovals: 0,
      processedApprovals: 0,
      averageProcessingTimeValue: 0,
      analysisPeriod: '',
      bottleneckPositions: [],
      optimizationSuggestions: [],
      approverPerformance: {},

      // 效能趋势图配置
      efficiencyTrendOptions: {
        chart: {
          type: 'area',
          toolbar: {
            show: true
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'smooth'
        },
        xaxis: {
          type: 'datetime',
          categories: []
        },
        tooltip: {
          x: {
            format: 'yyyy-MM-dd'
          }
        },
        fill: {
          type: 'gradient',
          gradient: {
            shadeIntensity: 1,
            opacityFrom: 0.7,
            opacityTo: 0.9,
            stops: [0, 90, 100]
          }
        },
        legend: {
          position: 'top'
        }
      },
      efficiencyTrendSeries: [
        {
          name: '提交',
          data: []
        },
        {
          name: '处理',
          data: []
        },
        {
          name: '待处理',
          data: []
        }
      ],

      // 状态分布图配置
      statusDistributionOptions: {
        chart: {
          type: 'donut'
        },
        labels: ['已处理', '待处理', '未提交'],
        legend: {
          position: 'bottom'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      statusDistributionSeries: [],

      // 处理时效图配置
      processingTimeOptions: {
        chart: {
          type: 'radialBar'
        },
        plotOptions: {
          radialBar: {
            startAngle: -135,
            endAngle: 135,
            dataLabels: {
              name: {
                fontSize: '16px',
                color: undefined,
                offsetY: 120
              },
              value: {
                offsetY: 76,
                fontSize: '22px',
                color: undefined,
                formatter: function (val) {
                  return val + '小时'
                }
              }
            }
          }
        },
        fill: {
          type: 'gradient',
          gradient: {
            shade: 'dark',
            shadeIntensity: 0.15,
            inverseColors: false,
            opacityFrom: 1,
            opacityTo: 1,
            stops: [0, 50, 65, 91]
          }
        },
        stroke: {
          dashArray: 4
        },
        labels: ['平均处理时间']
      },

      // 人员绩效图配置
      performanceChartOptions: {
        chart: {
          type: 'bar',
          height: 400
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: true
        },
        xaxis: {
          categories: []
        },
        legend: {
          position: 'top'
        }
      },
      performanceChartSeries: [
        {
          name: '总审批数',
          data: []
        },
        {
          name: '已处理数',
          data: []
        },
        {
          name: '待处理数',
          data: []
        }
      ],

      // 处理量排行图配置
      volumeRankingOptions: {
        chart: {
          type: 'bar',
          height: 350
        },
        plotOptions: {
          bar: {
            horizontal: true
          }
        },
        dataLabels: {
          enabled: true
        },
        xaxis: {
          categories: []
        }
      },
      volumeRankingSeries: [{
        name: '处理量',
        data: []
      }]
    }
  },
  mounted () {
    this.queryApprovalEfficiency()
  },
  methods: {
    queryApprovalEfficiency () {
      this.loading = true
      this.$get(`/cos/work-ticket/queryApprovalEfficiency`).then((r) => {
        const data = r.data
        this.processEfficiencyData(data)
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取审批效能数据失败')
      })
    },

    processEfficiencyData(data) {
      // 设置基础数据
      this.totalApprovals = data.totalApprovals || 0
      this.processedApprovals = data.processedApprovals || 0
      this.averageProcessingTimeValue = parseFloat(data.averageProcessingTime) || 0
      this.analysisPeriod = data.analysisPeriod || ''
      this.bottleneckPositions = data.bottleneckPositions || []
      this.optimizationSuggestions = data.optimizationSuggestions || []
      this.approverPerformance = data.approverPerformance || {}

      // 处理效能趋势数据
      this.processEfficiencyTrend(data.efficiencyTrend)

      // 处理状态分布数据
      this.processStatusDistribution()

      // 处理人员绩效数据
      this.processPerformanceData()
    },

    processEfficiencyTrend(efficiencyTrend) {
      const dates = Object.keys(efficiencyTrend).sort()
      const submittedData = []
      const processedData = []
      const pendingData = []

      dates.forEach(date => {
        const dayData = efficiencyTrend[date]
        submittedData.push(dayData.submitted || 0)
        processedData.push(dayData.processed || 0)
        pendingData.push(dayData.pending || 0)
      })

      this.efficiencyTrendOptions.xaxis.categories = dates
      this.efficiencyTrendSeries[0].data = submittedData
      this.efficiencyTrendSeries[1].data = processedData
      this.efficiencyTrendSeries[2].data = pendingData

      // 设置状态分布数据
      this.statusDistributionSeries = [
        this.processedApprovals,
        this.totalApprovals - this.processedApprovals,
        0 // 未提交的数量，可以根据需要调整
      ]
    },

    processStatusDistribution() {
      // 状态分布已经在上面设置了
    },

    processPerformanceData() {
      const performers = Object.values(this.approverPerformance)
      const names = performers.map(p => p.staffName)
      const totalApprovals = performers.map(p => p.totalApprovals)
      const processedApprovals = performers.map(p => p.processedApprovals)
      const pendingApprovals = performers.map(p => p.pendingApprovals)

      // 人员绩效对比图
      this.performanceChartOptions.xaxis.categories = names
      this.performanceChartSeries[0].data = totalApprovals
      this.performanceChartSeries[1].data = processedApprovals
      this.performanceChartSeries[2].data = pendingApprovals

      // 处理量排行图
      const sortedPerformers = [...performers].sort((a, b) => b.processedApprovals - a.processedApprovals)
      this.volumeRankingOptions.xaxis.categories = sortedPerformers.map(p => p.staffName)
      this.volumeRankingSeries[0].data = sortedPerformers.map(p => p.processedApprovals)
    },

    getEfficiencyColor(rating) {
      const colorMap = {
        '优秀': 'green',
        '良好': 'blue',
        '一般': 'orange',
        '较差': 'red',
        '数据不足': 'cyan'
      }
      return colorMap[rating] || 'default'
    }
  }
}
</script>

<style scoped>.table-page-search-wrapper {
  padding: 24px;
}

.ant-card {
  margin-bottom: 24px;
}

.apexcharts-canvas {
  direction: ltr;
}
</style>
