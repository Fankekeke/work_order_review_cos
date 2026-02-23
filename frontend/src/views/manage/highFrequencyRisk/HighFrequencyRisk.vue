
<template>
  <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}" style="width: 100%">
    <div class="table-page-search-wrapper">
      <a-alert
        message="高频风险作业统计分析"
        description="展示各类高风险作业的频率分布和趋势分析"
        type="info"
        show-icon        style="margin-bottom: 24px"
      />

      <!-- 总览卡片 -->
      <a-row :gutter="24" style="margin-bottom: 24px">
        <a-col :span="8">
          <a-card>
            <a-statistic
              title="总高风险作业票数"
              :value="totalHighRiskTickets"
              :value-style="{ color: '#cf1322' }"
            >
              <template #prefix>
                <a-icon type="alert" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card>
            <a-statistic
              title="风险类型数量"
              :value="riskTypeCount"
              :value-style="{ color: '#1890ff' }"
            >
              <template #prefix>
                <a-icon type="pie-chart" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card>
            <a-statistic
              title="最高频次"
              :value="maxFrequency"
              suffix="次"
              :value-style="{ color: '#faad14' }"
            >
              <template #prefix>
                <a-icon type="bar-chart" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>

      <!-- 图表区域 -->
      <a-row :gutter="24">
        <!-- 饼图：风险类型分布 -->
        <a-col :span="12">
          <a-card title="风险类型分布" :bordered="false">
            <apexchart
              width="100%"
              height="350"
              type="pie"
              :options="pieChartOptions"
              :series="pieChartSeries"
            ></apexchart>
          </a-card>
        </a-col>

        <!-- 柱状图：风险频次排行 -->
        <a-col :span="12">
          <a-card title="风险频次排行" :bordered="false">
            <apexchart
              width="100%"
              height="350"
              type="bar"
              :options="barChartOptions"
              :series="barChartSeries"
            ></apexchart>
          </a-card>
        </a-col>
      </a-row>

      <a-row :gutter="24" style="margin-top: 24px">
        <!-- 雷达图：多维度风险分析 -->
        <a-col :span="12">
          <a-card title="风险综合评估雷达图" :bordered="false">
            <apexchart
              width="100%"
              height="350"
              type="radar"
              :options="radarChartOptions"
              :series="radarChartSeries"
            ></apexchart>
          </a-card>
        </a-col>

        <!-- 折线图：趋势分析 -->
        <a-col :span="12">
          <a-card title="风险趋势分析" :bordered="false">
            <apexchart
              width="100%"
              height="350"
              type="line"
              :options="lineChartOptions"
              :series="lineChartSeries"
            ></apexchart>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>export default {
  name: 'HighFrequencyRisk',
  data () {
    return {
      loading: false,
      totalHighRiskTickets: 0,
      riskTypeCount: 0,
      maxFrequency: 0,

      // 饼图配置
      pieChartOptions: {
        chart: {
          type: 'pie'
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
      pieChartSeries: [],

      // 柱状图配置
      barChartOptions: {
        chart: {
          type: 'bar',
          height: 350
        },
        plotOptions: {
          bar: {
            horizontal: true,
          }
        },
        dataLabels: {
          enabled: false
        },
        xaxis: {
          categories: []
        }
      },
      barChartSeries: [{
        name: '作业次数',
        data: []
      }],

      // 雷达图配置
      radarChartOptions: {
        chart: {
          type: 'radar',
          height: 350
        },
        xaxis: {
          categories: ['安全风险', '操作复杂度', '人员要求', '设备依赖', '环境影响', '应急难度']
        }
      },
      radarChartSeries: [{
        name: '风险评分',
        data: [80, 70, 90, 60, 75, 85]
      }],

      // 折线图配置
      lineChartOptions: {
        chart: {
          type: 'line',
          height: 350
        },
        xaxis: {
          categories: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        stroke: {
          curve: 'smooth'
        }
      },
      lineChartSeries: [{
        name: '高风险作业数',
        data: [3, 4, 2, 5, 3, 6, 2]
      }]
    }
  },
  mounted () {
    this.queryHighFrequencyRisk()
  },
  methods: {
    queryHighFrequencyRisk () {
      this.loading = true
      this.$get(`/cos/work-ticket/queryHighFrequencyRisk`).then((r) => {
        const data = r.data
        this.totalHighRiskTickets = data.totalHighRiskTickets || 0
        this.processRiskData(data.highFrequencyRiskTypes || [])
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message.error('获取高频风险数据失败')
      })
    },

    processRiskData(riskTypes) {
      // 处理风险类型数据
      const labels = []
      const seriesData = []

      riskTypes.forEach(item => {
        const key = Object.keys(item)[0]
        const value = item[key]
        labels.push(key)
        seriesData.push(value)
      })

      // 更新饼图数据
      this.pieChartOptions.labels = labels
      this.pieChartSeries = seriesData

      // 更新柱状图数据
      this.barChartOptions.xaxis.categories = labels
      this.barChartSeries[0].data = seriesData

      // 计算统计数据
      this.riskTypeCount = riskTypes.length
      this.maxFrequency = Math.max(...seriesData)
    }
  }
}
</script>

<style scoped>.table-page-search-wrapper {
  padding: 24px;
}
</style>
