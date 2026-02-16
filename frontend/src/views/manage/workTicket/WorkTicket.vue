<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="标题"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.title"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="内容"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.content"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
<!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->

      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :scroll="{ x: 900 }"
        @change="handleTableChange"
        :expandedRowRender="expandedRowRender"
      >
        <!-- 原有插槽保持不变 -->
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-badge status="processing" v-if="record.rackUp === 1" />
            <a-badge status="error" v-if="record.rackUp === 0" />
            <a-tooltip>
              <template slot="title">{{ record.title }}</template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">{{ record.content }}</template>
              {{ record.content.slice(0, 40) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="eye" theme="twoTone" twoToneColor="#1890ff" @click="viewDetail(record)" title="查看详情" style="margin-right: 8px;"></a-icon>
<!--          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>-->
        </template>
      </a-table>
    </div>
    <work-ticket-detail @close="handleViewClose"
                        v-if="ticketView.visiable"
                        :ticketShow="ticketView.visiable"
                        :ticketData="ticketView.data"/>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
import WorkTicketDetail from './WorkTicketView.vue'
moment.locale('zh-cn')

export default {
  name: 'Bulletin',
  components: {WorkTicketDetail, RangeDate},
  data () {
    return {
      ticketView: {
        visiable: false,
        data: null
      },
      advanced: false,
      bulletinAdd: {
        visiable: false
      },
      bulletinEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '作业类型',
        dataIndex: 'type',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '风险等级',
        dataIndex: 'riskLevel',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            // 根据风险等级显示不同颜色
            let color = 'blue'
            if (text === '高') color = 'red'
            else if (text === '中') color = 'orange'
            else if (text === '低') color = 'green'
            return <a-tag color={color}>{text}</a-tag>
          } else {
            return '- -'
          }
        }
      }, {
        title: '作业内容',
        dataIndex: 'workContent',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null && text.length > 20) {
            return <a-tooltip title={text}>{text.substring(0, 20)}...</a-tooltip>
          } else if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '作业地点',
        dataIndex: 'location',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '申请人',
        dataIndex: 'staffName',
        customRender: (text, record, index) => {
          if (!text) return '- -'
          return (
            <div style="display: flex; align-items: center;">
              <a-avatar
                size="72"
                src={ record.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + record.staffImages : null }
                icon={ record.staffImages ? null : 'user' }
                style="margin-right: 15px;"
              />
              <div>
                <div>{text}</div>
                <div style="color: #999; font-size: 12px;margin-top: 5px">{record.deptName}</div>
                <div style="color: #777; font-size: 12px;margin-top: 5px">{record.positionName}</div>
              </div>
            </div>
          )
        },
        width: 200
      }, {
        title: '开始时间',
        dataIndex: 'startTime',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '结束时间',
        dataIndex: 'endTime',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: 'AI审核分数',
        dataIndex: 'aiAuditScore',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            // 根据分数显示不同颜色
            let color = 'blue'
            if (text >= 90) color = 'green'
            else if (text >= 70) color = 'orange'
            else color = 'red'
            return <a-tag color={color}>{text}分</a-tag>
          } else {
            return '- -'
          }
        }
      }, {
        title: '状态',
        dataIndex: 'status',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            // 1:正在初审, 2:正在复审, 3:正在终审, 4:已驳回 5:安全措施 6:措施落实
            let statusText = ''
            let statusColor = ''
            switch (text) {
              case 1:
                statusText = '正在初审'
                statusColor = 'orange'
                break
              case 2:
                statusText = '正在复审'
                statusColor = 'blue'
                break
              case 3:
                statusText = '正在终审'
                statusColor = 'purple'
                break
              case 4:
                statusText = '已驳回'
                statusColor = 'red'
                break
              case 5:
                statusText = '安全措施'
                statusColor = 'gold'
                break
              case 6:
                statusText = '措施落实'
                statusColor = 'green'
                break
              default:
                statusText = '未知'
                statusColor = 'gray'
            }
            return <a-tag color={statusColor}>{statusText}</a-tag>
          } else {
            return '- -'
          }
        }
      }, {
        title: '创建时间',
        dataIndex: 'createTime',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' }
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    handleViewClose () {
      this.ticketView.visiable = false
    },
    viewDetail (record) {
      this.ticketView.data = record
      this.ticketView.visiable = true
    },
    expandedRowRender (record) {
      return (
        <div>
          <h4>作业详细内容：</h4>
          {record.workContent}
        </div>
      )
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.bulletinAdd.visiable = true
    },
    handleBulletinAddClose () {
      this.bulletinAdd.visiable = false
    },
    handleBulletinAddSuccess () {
      this.bulletinAdd.visiable = false
      this.$message.success('新增模板流程成功')
      this.search()
    },
    edit (record) {
      this.$refs.bulletinEdit.setFormValues(record)
      this.bulletinEdit.visiable = true
    },
    handleBulletinEditClose () {
      this.bulletinEdit.visiable = false
    },
    handleBulletinEditSuccess () {
      this.bulletinEdit.visiable = false
      this.$message.success('修改模板流程成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/work-ticket/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      this.$get('/cos/work-ticket/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
.ant-list-item {
  padding: 8px 16px;
}

.ant-list-item strong {
  color: #1890ff;
}
</style>
