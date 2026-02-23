
<template>
  <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}" style="width: 100%">
    <div class="table-page-search-wrapper">
      <a-alert
        message="违规画像分析"
        description="展示近30天违规行为的多维度统计分析"
        type="warning"
        show-icon        style="margin-bottom: 24px"
      />

      <!-- 数据概览卡片 -->
      <a-row :gutter="24" style="margin-bottom: 24px">
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="总违规次数"
              :value="totalIllegalCount"
              :value-style="{ color: '#cf1322' }"
            >
              <template #prefix>
                <a-icon type="exclamation-circle" />
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
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="违规类型数"
              :value="violationTypeCount"
              suffix="种"
            >
              <template #prefix>
                <a-icon type="tag" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic
              title="涉及地点数"
              :value="locationCount"
              suffix="个"
            >
              <template #prefix>
                <a-icon type="environment" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>

      <!-- 图表展示区域 -->
      <a-tabs default-active-key="1">
        <!-- 违规趋势分析 -->
        <a-tab-pane key="1" tab="违规趋势分析">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-card title="近30天违规趋势" :bordered="false">
                <apexchart
                  width="100%"
                  height="400"
                  type="area"
                  :options="trendChartOptions"
                  :series="trendChartSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>

        <!-- 违规类型分析 -->
        <a-tab-pane key="2" tab="违规类型分析">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-card title="违规类型分布" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="donut"
                  :options="typeChartOptions"
                  :series="typeChartSeries"
                ></apexchart>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card title="Top违规类型排行" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="bar"
                  :options="topViolationOptions"
                  :series="topViolationSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>

        <!-- 时间分布分析 -->
        <a-tab-pane key="3" tab="时间分布分析">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-card title="24小时违规时间分布" :bordered="false">
                <apexchart
                  width="100%"
                  height="400"
                  type="heatmap"
                  :options="timeHeatmapOptions"
                  :series="timeHeatmapSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>

        <!-- 地点分布分析 -->
        <a-tab-pane key="4" tab="地点分布分析">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-card title="违规地点分布" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="pie"
                  :options="locationChartOptions"
                  :series="locationChartSeries"
                ></apexchart>
              </a-card>
            </a-col>
            <a-col :span="12">
              <a-card title="地点违规密度" :bordered="false">
                <apexchart
                  width="100%"
                  height="350"
                  type="radar"
                  :options="locationRadarOptions"
                  :series="locationRadarSeries"
                ></apexchart>
              </a-card>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-card>
</template>

<script>export default {
  name: 'IllegalPortrait',
  data () {
    return {
      loading: false,
      totalIllegalCount: 0,
      analysisPeriod: '',
      violationTypeCount: 0,
      locationCount: 0,

      // 趋势图配置
      trendChartOptions: {
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
        }
      },
      trendChartSeries: [{
        name: '违规次数',
        data: []
      }],

      // 类型分布图配置
      typeChartOptions: {
        chart: {
          type: 'donut'
        },
        labels: [],
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
      typeChartSeries: [],

      // Top违规类型排行
      topViolationOptions: {
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
      topViolationSeries: [{
        name: '违规次数',
        data: []
      }],

      // 时间热力图配置
      timeHeatmapOptions: {
        chart: {
          type: 'heatmap',
          height: 400
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          colors: ['#fff']
        },
        xaxis: {
          type: 'category',
          categories: [] // 这里会在processTimeDistributionData中动态设置
        },
        tooltip: {
          x: {
            formatter: function (val) {
              return `时间: ${val}`
            }
          },
          y: {
            formatter: function (val) {
              return `违规次数: ${val}`
            }
          }
        },
        legend: {
          position: 'top',
          horizontalAlign: 'right'
        }
      },
      timeHeatmapSeries: [{
        name: '违规密度',
        data: []
      }],

      // 地点分布图配置
      locationChartOptions: {
        chart: {
          type: 'pie'
        },
        labels: [],
        legend: {
          position: 'bottom'
        }
      },
      locationChartSeries: [],

      // 地点雷达图配置
      locationRadarOptions: {
        chart: {
          type: 'radar'
        },
        xaxis: {
          categories: []
        }
      },
      locationRadarSeries: [{
        name: '违规指数',
        data: []
      }]
    }
  },
  mounted () {
    this.queryIllegalPortrait()
  },
  methods: {
    queryIllegalPortrait () {
      this.loading = true
      this.$get(`/cos/work-ticket/queryIllegalPortrait`).then((r) => {
        const data = r.data
        this.processIllegalData(data)
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取违规画像数据失败')
      })
    },

    processIllegalData (data) {
      // 设置基础数据
      this.totalIllegalCount = data.totalIllegalCount || 0
      this.analysisPeriod = data.analysisPeriod || ''

      // 处理趋势数据
      this.processTrendData(data.trendStats)

      // 处理违规类型数据
      this.processViolationTypeData(data.violationTypes, data.topViolations)

      // 处理时间分布数据
      this.processTimeDistributionData(data.timeDistribution)

      // 处理地点分布数据
      this.processLocationData(data.locationDistribution)
    },

    processTrendData (trendStats) {
      const dates = Object.keys(trendStats).sort()
      const values = dates.map(date => trendStats[date])

      this.trendChartOptions.xaxis.categories = dates
      this.trendChartSeries[0].data = values
    },

    processViolationTypeData (violationTypes, topViolations) {
      // 处理类型分布
      const typeLabels = Object.keys(violationTypes)
      const typeValues = Object.values(violationTypes)

      this.typeChartOptions.labels = typeLabels
      this.typeChartSeries = typeValues
      this.violationTypeCount = typeLabels.length

      // 处理Top违规类型
      const topLabels = []
      const topValues = []

      topViolations.forEach(item => {
        const key = Object.keys(item)[0]
        const value = item[key]
        topLabels.push(key)
        topValues.push(value)
      })

      this.topViolationOptions.xaxis.categories = topLabels
      this.topViolationSeries[0].data = topValues
    },

    processTimeDistributionData (timeDistribution) {
      // 获取时间点和对应数值
      const timePoints = Object.keys(timeDistribution)
      const timeValues = Object.values(timeDistribution)

      // 构造热力图数据，x为时间点，y为违规次数
      this.timeHeatmapSeries[0].data = timePoints.map((timePoint, index) => ({
        x: timePoint, // 使用具体的时间点作为x轴标签
        y: timeValues[index] // 对应的违规次数作为y值
      }))
    },

    processLocationData (locationDistribution) {
      const locations = Object.keys(locationDistribution)
      const locationValues = Object.values(locationDistribution)

      this.locationChartOptions.labels = locations
      this.locationChartSeries = locationValues
      this.locationCount = locations.length

      // 为雷达图准备数据（这里简化处理）
      this.locationRadarOptions.xaxis.categories = locations
      this.locationRadarSeries[0].data = locationValues.map(val => val * 10) // 放大数值便于显示
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
