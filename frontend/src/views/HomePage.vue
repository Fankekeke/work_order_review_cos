
<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card">
        <a-col :span="12">
          <!--          <div class="head-info-avatar">-->
          <!--            <img alt="头像" :src="avatar">-->
          <!--          </div>-->
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
            <div class="head-info-desc">
              <p>{{user.roleName ? user.roleName : '暂无角色'}}</p>
            </div>
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="12">
          <!--          <div>-->
          <!--            <a-row class="more-info" v-if="user.roleId == 74">-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="用户数量" :content="titleAdminData.merchantNum" :center="false" :bordered="false"/>-->
          <!--              </a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="员工数量" :content="titleAdminData.staffNum" :center="false" :bordered="false"/>-->
          <!--              </a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="总收益" :content="titleAdminData.totalPrice" :center="false" :bordered="false"/>-->
          <!--              </a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="总订单数" :content="titleAdminData.totalNum" :center="false"/>-->
          <!--              </a-col>-->
          <!--            </a-row>-->
          <!--            <a-row class="more-info" v-if="user.roleId == 76">-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4"></a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="总收益" :content="titleData.totalPrice" :center="false" :bordered="false"/>-->
          <!--              </a-col>-->
          <!--              <a-col :span="4">-->
          <!--                <head-info title="总订单数" :content="titleData.totalNum" :center="false"/>-->
          <!--              </a-col>-->
          <!--            </a-row>-->
          <!--          </div>-->
        </a-col>
      </a-card>
    </a-row>
    <a-row :gutter="8" class="count-info" style="margin-top: 15px">
      <!-- 作业票类型统计 -->
      <a-col :span="5">
        <a-card title="作业票类型分布" size="small" hoverable>
          <a-skeleton v-if="loading" active :paragraph="{ rows: 4 }" />
          <apexchart v-else type="pie" height="250" :options="ticketTypeChartOptions" :series="ticketTypeSeries" />
        </a-card>
      </a-col>
      <!-- 风险等级统计 -->
      <a-col :span="5">
        <a-card title="风险等级分布" size="small" hoverable>
          <a-skeleton v-if="loading" active :paragraph="{ rows: 4 }" />
          <apexchart v-else type="donut" height="250" :options="riskLevelChartOptions" :series="riskLevelSeries" />
        </a-card>
      </a-col>
      <!-- 近期票据趋势 -->
      <a-col :span="9">
        <a-card title="近期票据趋势" size="small" hoverable>
          <a-skeleton v-if="loading" active :paragraph="{ rows: 4 }" />
          <apexchart v-else type="area" height="203" :options="recentTicketChartOptions" :series="recentTicketSeries" />
        </a-card>
      </a-col>
      <!-- 综合统计 -->
      <a-col :span="5">
        <a-card title="综合统计" size="small" hoverable>
          <a-skeleton v-if="loading" active :paragraph="{ rows: 3 }" />
          <div v-else class="summary-stats">
            <div class="stat-item">
              <div class="stat-label">总票据数</div>
              <div class="stat-value">{{ totalRecentTickets }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">统计周期</div>
              <div class="stat-value">{{ startDate }} 至 {{ endDate }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">平均安全分</div>
              <div class="stat-value">{{ avgSafetyScore }}</div>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="8" class="count-info" style="margin-top: 15px">
      <a-col :span="8" class="visit-count-wrapper">
        <a-card class="visit-count" hoverable>
          <apexchart ref="count" type=bar height=300 :options="chartOptions" :series="series" />
        </a-card>
      </a-col>
      <a-col :span="16" class="bulletin-wrapper">
        <a-card title="公告信息" class="bulletin-card" hoverable>
          <a-skeleton v-if="loading" active :paragraph="{ rows: 4 }" />
          <a-list v-else class="bulletin-list" :data-source="bulletinList" item-layout="vertical" size="large">
            <a-list-item slot="renderItem" slot-scope="item" key="item.id">
              <a-list-item-meta>
                <a slot="title" @click="viewBulletin(item)">{{ item.title }}</a>
                <span slot="description">
                  <a-icon type="user" /> {{ item.publisher }}
                  <a-icon type="clock-circle" style="margin-left: 16px;" /> {{ item.date }}
                </span>
              </a-list-item-meta>
              <div class="bulletin-content">
                {{ item.content.length > 100 ? item.content.substring(0, 100) + '...' : item.content }}
              </div>
<!--              <div v-if="item.images" class="bulletin-image">-->
<!--                <img :src="'http://127.0.0.1:9527/imagesWeb/' + item.images" alt="公告图片" style="max-width: 100%; max-height: 200px;" />-->
<!--              </div>-->
            </a-list-item>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>import HeadInfo from '@/views/common/HeadInfo'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'HomePage',
  components: {HeadInfo},
  data () {
    return {
      loading: true,
      titleData: {
        memberNum: 0,
        staffNum: 0,
        totalPrice: 0,
        totalNum: 0
      },
      titleAdminData: {
        merchantNum: 0,
        staffNum: 0,
        totalPrice: 0,
        totalNum: 0
      },
      series: [],
      chartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '35%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        fill: {
          opacity: 1
        }
      },
      // 作业票类型统计图表配置
      ticketTypeChartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        labels: [],
        legend: {
          position: 'bottom'
        },
        colors: ['#008FFB', '#00E396', '#FEB019', '#FF4560', '#775DD0']
      },
      ticketTypeSeries: [],
      // 风险等级统计图表配置
      riskLevelChartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        labels: [],
        legend: {
          position: 'bottom'
        },
        colors: ['#00E396', '#FEB019', '#FF4560']
      },
      riskLevelSeries: [],
      // 近期票据趋势图表配置
      recentTicketChartOptions: {
        chart: {
          toolbar: {
            show: false
          }
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'smooth'
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
        colors: ['#008FFB']
      },
      recentTicketSeries: [],
      // 统计数据
      totalRecentTickets: 0,
      avgSafetyScore: 0,
      startDate: '',
      endDate: '',
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      bulletinList: [],
      homeData: null
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    queryHomeData () {
      this.loading = true
      this.$get(`/cos/work-ticket/homeDate`).then((r) => {
        this.homeData = r.data
        // 处理统计数据
        this.processStatisticsData(r.data)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 处理统计数据
    processStatisticsData (data) {
      if (data.bulletin) {
        this.bulletinList = data.bulletin
      }
      // 作业票类型统计
      if (data.ticketTypeStats) {
        const ticketTypes = Object.keys(data.ticketTypeStats)
        const ticketValues = Object.values(data.ticketTypeStats)
        console.log(ticketTypes)
        console.log(ticketValues)
        this.ticketTypeChartOptions.labels = ticketTypes
        this.ticketTypeSeries = ticketValues
      }

      // 风险等级统计
      if (data.riskLevelStats) {
        const riskLevels = Object.keys(data.riskLevelStats)
        const riskValues = Object.values(data.riskLevelStats)
        this.riskLevelChartOptions.labels = riskLevels
        this.riskLevelSeries = riskValues
      }

      // 近期票据趋势
      if (data.recentTicketStats) {
        const dates = Object.keys(data.recentTicketStats)
        const counts = Object.values(data.recentTicketStats)
        this.recentTicketChartOptions.xaxis.categories = dates.map(date =>
          moment(date).format('MM-DD')
        )
        this.recentTicketSeries = [{
          name: '票据数量',
          data: counts
        }]
      }

      // 综合统计数据
      this.totalRecentTickets = data.totalRecentTickets || 0
      this.avgSafetyScore = data.avgSafetyScore || 0
      this.startDate = data.startDate ? moment(data.startDate).format('YYYY-MM-DD') : ''
      this.endDate = data.endDate ? moment(data.endDate).format('YYYY-MM-DD') : ''
    },
    cleanMessage (id) {
      this.$get(`/cos/notify-info/setReadStatus/${id}`).then((r) => {
        this.getNewList()
      })
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      return `${time}，${this.user.username}`
    },
    setTitleData (titleData) {
      this.titleData = titleData
    },
    setAdminTitle (titleData) {
      this.titleAdminData = titleData
    },
    viewBulletin (bulletin) {
      this.$message.info(`查看公告: ${bulletin.title}`)
      // 这里可以添加跳转到公告详情页面的逻辑
    }
  },
  mounted () {
    this.queryHomeData()
    this.welcomeMessage = this.welcome()
    this.$get(`index/${this.user.username}`).then((r) => {
      let data = r.data.data
      this.todayIp = data.todayIp
      this.todayVisitCount = data.todayVisitCount
      this.totalVisitCount = data.totalVisitCount
      let sevenVisitCount = []
      let dateArr = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenVisitCount) {
          if (o.days === time) {
            contain = true
            sevenVisitCount.push(o.count)
          }
        }
        if (!contain) {
          sevenVisitCount.push(0)
        }
        dateArr.push(time)
      }
      let sevenUserVistCount = []
      for (let i = 6; i >= 0; i--) {
        let time = moment().subtract(i, 'days').format('MM-DD')
        let contain = false
        for (let o of data.lastSevenUserVisitCount) {
          if (o.days === time) {
            contain = true
            sevenUserVistCount.push(o.count)
          }
        }
        if (!contain) {
          sevenUserVistCount.push(0)
        }
      }
      this.$refs.count.updateSeries([
        {
          name: '您',
          data: sevenUserVistCount
        },
        {
          name: '总数',
          data: sevenVisitCount
        }
      ], true)
      this.$refs.count.updateOptions({
        xaxis: {
          categories: dateArr
        },
        title: {
          text: '近七日系统访问记录',
          align: 'left'
        }
      }, true, true)
    }).catch((r) => {
      console.error(r)
      this.$message.error('获取首页信息失败')
    })
  }
}
</script>
<style lang="less">.home-page {
  .head-info {
    margin-bottom: .5rem;
    .head-info-card {
      padding: .5rem;
      border-color: #f1f1f1;
      .head-info-avatar {
        display: inline-block;
        float: left;
        margin-right: 1rem;
        img {
          width: 5rem;
          border-radius: 2px;
        }
      }
      .head-info-count {
        display: inline-block;
        float: left;
        .head-info-welcome {
          font-size: 1.05rem;
          margin-bottom: .1rem;
        }
        .head-info-desc {
          color: rgba(0, 0, 0, 0.45);
          font-size: .8rem;
          padding: .2rem 0;
          p {
            margin-bottom: 0;
          }
        }
        .head-info-time {
          color: rgba(0, 0, 0, 0.45);
          font-size: .8rem;
          padding: .2rem 0;
        }
      }
    }
  }
  .count-info {
    .visit-count-wrapper {
      padding-left: 0 !important;
      .visit-count {
        padding: .5rem;
        border-color: #f1f1f1;
        .ant-card-body {
          padding: .5rem 1rem !important;
        }
      }
    }
    .bulletin-wrapper {
      padding-right: 0 !important;
      .bulletin-card {
        height: 100%;
        .ant-card-body {
          padding: 12px;
          max-height: 295px;
          overflow-y: auto;
        }
        .bulletin-list {
          .ant-list-item {
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;
            &:last-child {
              border-bottom: none;
            }
            .ant-list-item-meta {
              margin-bottom: 8px;
              .ant-list-item-meta-title {
                margin-bottom: 4px;
                font-size: 16px;
                font-weight: 500;
                a {
                  color: #1890ff;
                  &:hover {
                    color: #40a9ff;
                  }
                }
              }
              .ant-list-item-meta-description {
                font-size: 12px;
                color: #8c8c8c;
                .anticon {
                  margin-right: 4px;
                }
              }
            }
            .bulletin-content {
              font-size: 14px;
              line-height: 1.5;
              color: #595959;
              margin-bottom: 8px;
            }
            .bulletin-image {
              text-align: center;
              img {
                border-radius: 4px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
              }
            }
          }
        }
      }
    }
    // 添加统计卡片样式
    .ant-card {
      margin-bottom: 16px;
      .ant-card-head {
        min-height: 40px;
        .ant-card-head-title {
          font-size: 14px;
          font-weight: 600;
        }
      }
      .ant-card-body {
        padding: 12px;
      }
    }
    // 综合统计样式
    .summary-stats {
      .stat-item {
        text-align: center;
        margin-bottom: 35px;
        &:last-child {
          margin-bottom: 0;
        }
        .stat-label {
          font-size: 12px;
          color: rgba(0, 0, 0, 0.45);
          margin-bottom: 4px;
        }
        .stat-value {
          font-size: 18px;
          font-weight: 600;
          color: #1890ff;
        }
      }
    }
    .project-wrapper {
      padding-right: 0 !important;
      .project-card {
        border: none !important;
        .ant-card-head {
          border-left: 1px solid #f1f1f1 !important;
          border-top: 1px solid #f1f1f1 !important;
          border-right: 1px solid #f1f1f1 !important;
        }
        .ant-card-body {
          padding: 0 !important;
          table {
            width: 100%;
            td {
              width: 50%;
              border: 1px solid #f1f1f1;
              padding: .6rem;
              .project-avatar-wrapper {
                display:inline-block;
                float:left;
                margin-right:.7rem;
                .project-avatar {
                  color: #42b983;
                  background-color: #d6f8b8;
                }
              }
            }
          }
        }
        .project-detail {
          display:inline-block;
          float:left;
          text-align:left;
          width: 78%;
          .project-name {
            font-size:.9rem;
            margin-top:-2px;
            font-weight:600;
          }
          .project-desc {
            color:rgba(0, 0, 0, 0.45);
            p {
              margin-bottom:0;
              font-size:.6rem;
              white-space:normal;
            }
          }
        }
      }
    }
  }
}
</style>
