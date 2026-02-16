
<template>
  <a-modal
    title="作业单详情"
    :visible="ticketShow"
    :width="1100"
    :footer="null"
    @cancel="handleClose"
    wrapClassName="work-ticket-detail-modal"
  >
    <a-spin :spinning="loading">
      <div v-if="ticketData">
        <!-- 基本信息 -->
        <a-card title="基本信息" size="small" class="detail-section" :bordered="false">
          <a-row :gutter="16">
            <a-col :span="8">
              <div class="info-item">
                <span class="label">作业类型：</span>
                <span class="value">{{ ticketData.type || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="label">风险等级：</span>
                <span class="value">
                  <a-tag :color="getRiskLevelColor(ticketData.riskLevel)">
                    {{ ticketData.riskLevel || '- -' }}
                  </a-tag>
                </span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="label">当前状态：</span>
                <span class="value">
                  <a-tag :color="getStatusColor(ticketData.status)">
                    {{ getStatusText(ticketData.status) }}
                  </a-tag>
                </span>
              </div>
            </a-col>
          </a-row>

          <a-row :gutter="16" style="margin-top: 16px;">
            <a-col :span="8">
              <div class="info-item">
                <span class="label">申请人：</span>
                <span class="value">{{ ticketData.staffName || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="label">所属部门：</span>
                <span class="value">{{ ticketData.deptName || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="label">岗位：</span>
                <span class="value">{{ ticketData.positionName || '- -' }}</span>
              </div>
            </a-col>
          </a-row>
        </a-card>

        <!-- 作业详情 -->
        <a-card title="作业详情" size="small" class="detail-section" style="margin-top: 16px;" :bordered="false">
          <a-row :gutter="16">
            <a-col :span="24">
              <div class="info-item">
                <span class="label">作业内容：</span>
                <span class="value">{{ ticketData.workContent || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8" style="margin-top: 16px">
              <div class="info-item">
                <span class="label">作业地点：</span>
                <span class="value">{{ ticketData.location || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8" style="margin-top: 16px">
              <div class="info-item">
                <span class="label">开始时间：</span>
                <span class="value">{{ ticketData.startTime || '- -' }}</span>
              </div>
            </a-col>
            <a-col :span="8" style="margin-top: 16px">
              <div class="info-item">
                <span class="label">结束时间：</span>
                <span class="value">{{ ticketData.endTime || '- -' }}</span>
              </div>
            </a-col>
          </a-row>

          <a-row :gutter="16" style="margin-top: 16px;">
            <a-col :span="8">
              <div class="info-item">
                <span class="label">AI审核分数：</span>
                <span class="value">
                  <a-tag :color="getScoreColor(ticketData.aiAuditScore)">
                    {{ ticketData.aiAuditScore ? ticketData.aiAuditScore + '分' : '- -' }}
                  </a-tag>
                </span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="info-item">
                <span class="label">创建时间：</span>
                <span class="value">{{ ticketData.createTime || '- -' }}</span>
              </div>
            </a-col>
          </a-row>
        </a-card>

        <!-- 审批流程 -->
        <a-card title="审批流程" size="small" class="detail-section" style="margin-top: 16px;" :bordered="false">
          <a-steps :current="getCurrentStep()" size="small" style="margin-top: 16px">
            <!-- 初审步骤 -->
            <a-step title="初审">
              <div slot="description" class="step-detail">
                <div class="reviewers-list">
                  <div
                    v-for="(reviewer, index) in getReviewersByStep('初审')"
                    :key="index"
                    class="reviewer-item"
                  >
                    <div class="reviewer-info">
                      <a-avatar
                        :size="24"
                        :src="reviewer.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + reviewer.staffImages : null"
                        :icon="reviewer.staffImages ? null : 'user'"                        style="margin-right: 6px;"
                      />
                      <span class="reviewer-name">{{ reviewer.staffName }}</span>
                      <a-tag
                        :color="getActionTagColor(reviewer.action)"
                        style="margin-left: 8px; font-size: 12px;"
                      >
                        {{ getActionDisplayText(reviewer.action) }}
                      </a-tag>
                    </div>
                    <div v-if="reviewer.comments && reviewer.comments !== '- -'" class="reviewer-comment">
                      备注：{{ reviewer.comments }}
                    </div>
                    <div class="reviewer-time" style="font-size: 12px; color: #999; margin-top: 2px;">
                      {{ reviewer.createDate }}
                    </div>
                  </div>
                </div>
              </div>
            </a-step>

            <!-- 复审步骤 -->
            <a-step title="复审">
              <div slot="description" class="step-detail">
                <div class="reviewers-list">
                  <div
                    v-for="(reviewer, index) in getReviewersByStep('复审')"
                    :key="index"
                    class="reviewer-item"
                  >
                    <div class="reviewer-info">
                      <a-avatar
                        :size="24"
                        :src="reviewer.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + reviewer.staffImages : null"
                        :icon="reviewer.staffImages ? null : 'user'"                        style="margin-right: 6px;"
                      />
                      <span class="reviewer-name">{{ reviewer.staffName }}</span>
                      <a-tag
                        :color="getActionTagColor(reviewer.action)"
                        style="margin-left: 8px; font-size: 12px;"
                      >
                        {{ getActionDisplayText(reviewer.action) }}
                      </a-tag>
                    </div>
                    <div v-if="reviewer.comments && reviewer.comments !== '- -'" class="reviewer-comment">
                      备注：{{ reviewer.comments }}
                    </div>
                    <div class="reviewer-time" style="font-size: 12px; color: #999; margin-top: 2px;">
                      {{ reviewer.createDate }}
                    </div>
                  </div>
                  <div v-if="getReviewersByStep('复审').length === 0" class="no-reviewer">
                    暂无复审人员
                  </div>
                </div>
              </div>
            </a-step>

            <!-- 终审步骤 -->
            <a-step title="终审">
              <div slot="description" class="step-detail">
                <div class="reviewers-list">
                  <div
                    v-for="(reviewer, index) in getReviewersByStep('终审')"
                    :key="index"
                    class="reviewer-item"
                  >
                    <div class="reviewer-info">
                      <a-avatar
                        :size="24"
                        :src="reviewer.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + reviewer.staffImages : null"
                        :icon="reviewer.staffImages ? null : 'user'"                        style="margin-right: 6px;"
                      />
                      <span class="reviewer-name">{{ reviewer.staffName }}</span>
                      <a-tag
                        :color="getActionTagColor(reviewer.action)"
                        style="margin-left: 8px; font-size: 12px;"
                      >
                        {{ getActionDisplayText(reviewer.action) }}
                      </a-tag>
                    </div>
                    <div v-if="reviewer.comments && reviewer.comments !== '- -'" class="reviewer-comment">
                      备注：{{ reviewer.comments }}
                    </div>
                    <div class="reviewer-time" style="font-size: 12px; color: #999; margin-top: 2px;">
                      {{ reviewer.createDate }}
                    </div>
                  </div>
                  <div v-if="getReviewersByStep('终审').length === 0" class="no-reviewer">
                    暂无终审人员
                  </div>
                </div>
              </div>
            </a-step>

            <!-- 安全措施确认 -->
            <a-step title="安全措施">
              <div slot="description" class="step-detail">
                <div class="reviewers-list">
                  <div
                    v-for="(reviewer, index) in getReviewersByStep('安全措施确认')"
                    :key="index"
                    class="reviewer-item"
                  >
                    <div class="reviewer-info">
                      <a-avatar
                        :size="24"
                        :src="reviewer.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + reviewer.staffImages : null"
                        :icon="reviewer.staffImages ? null : 'user'"                        style="margin-right: 6px;"
                      />
                      <span class="reviewer-name">{{ reviewer.staffName }}</span>
                      <a-tag
                        :color="getActionTagColor(reviewer.action)"
                        style="margin-left: 8px; font-size: 12px;"
                      >
                        {{ getActionDisplayText(reviewer.action) }}
                      </a-tag>
                    </div>
                    <div v-if="reviewer.comments && reviewer.comments !== '- -'" class="reviewer-comment">
                      备注：{{ reviewer.comments }}
                    </div>
                    <div class="reviewer-time" style="font-size: 12px; color: #999; margin-top: 2px;">
                      {{ reviewer.createDate }}
                    </div>
                  </div>
                  <div v-if="getReviewersByStep('安全措施确认').length === 0" class="no-reviewer">
                    暂无安全措施确认人员
                  </div>
                </div>
              </div>
            </a-step>

            <!-- 措施落实 -->
            <a-step title="措施落实">
              <div slot="description" class="step-detail">
                <div class="reviewers-list">
                  <div
                    v-for="(reviewer, index) in getReviewersByStep('安全复查确认')"
                    :key="index"
                    class="reviewer-item"
                  >
                    <div class="reviewer-info">
                      <a-avatar
                        :size="24"
                        :src="reviewer.staffImages ? 'http://127.0.0.1:9527/imagesWeb/' + reviewer.staffImages : null"
                        :icon="reviewer.staffImages ? null : 'user'"                        style="margin-right: 6px;"
                      />
                      <span class="reviewer-name">{{ reviewer.staffName }}</span>
                      <a-tag
                        :color="getActionTagColor(reviewer.action)"
                        style="margin-left: 8px; font-size: 12px;"
                      >
                        {{ getActionDisplayText(reviewer.action) }}
                      </a-tag>
                    </div>
                    <div v-if="reviewer.comments && reviewer.comments !== '- -'" class="reviewer-comment">
                      备注：{{ reviewer.comments }}
                    </div>
                    <div class="reviewer-time" style="font-size: 12px; color: #999; margin-top: 2px;">
                      {{ reviewer.createDate }}
                    </div>
                  </div>
                  <div v-if="getReviewersByStep('安全复查确认').length === 0" class="no-reviewer">
                    暂无措施落实确认人员
                  </div>
                </div>
              </div>
            </a-step>
          </a-steps>
        </a-card>

        <!-- 审批建议 -->
        <a-card
          title=""
          size="small"
          class="detail-section"
          style="margin-top: 16px;"
          :bordered="false"
          v-if="ticketData && ticketData.aiAuditSuggestion"
        >
          <div class="audit-suggestion-content">
            <a-alert
              message="AI审批建议" type="info"
            />
            <div class="suggestion-details" v-if="ticketData.aiAuditSuggestion">
              <h4 style="margin: 16px 0 8px 0; color: #333;">详细分析：</h4>
              <div v-html="renderMarkdown(ticketData.aiAuditSuggestion)"></div>
            </div>
          </div>
        </a-card>

        <!-- 如果没有审批建议，显示提示 -->
        <a-card
          title="AI审批建议"
          size="small"
          class="detail-section"
          style="margin-top: 16px;"
          :bordered="false"
          v-else
        >
          <a-empty description="暂无AI审批建议" />
        </a-card>
        <!-- 操作按钮 -->
<!--        <div class="modal-footer" style="text-align: center; margin-top: 24px;">-->
<!--          <a-button @click="handleClose">关闭</a-button>-->
<!--          <a-button type="primary" @click="printDetail" style="margin-left: 8px;">打印</a-button>-->
<!--        </div>-->
      </div>

      <div v-else>
        <a-empty description="暂无数据" />
      </div>
    </a-spin>
  </a-modal>
</template>

<script>export default {
  name: 'WorkTicketDetail',
  props: {
    ticketShow: {
      type: Boolean,
      default: false
    },
    ticketData: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      approvalRecords: [],
      loading: false
    }
  },
  mounted () {
    this.queryTicketDetail(this.ticketData.id)
    // 确保加载marked.js库
    if (typeof window.marked === 'undefined') {
      const script = document.createElement('script')
      script.src = 'https://cdn.jsdelivr.net/npm/marked/marked.min.js'
      document.head.appendChild(script)
    }
  },
  methods: {
    // 获取操作状态标签颜色
    getActionTagColor (action) {
      const colorMap = {
        '已通过': 'green',
        '已驳回': 'red',
        '未审核': 'orange',
        '审核中': 'blue',
        '- -': 'default',
        '通过': 'green',
        '驳回': 'red'
      }
      return colorMap[action] || 'default'
    },

    // 获取操作状态显示文本
    getActionDisplayText (action) {
      // 可以在这里做文本转换，比如统一显示格式
      if (action === 'approve') return '已通过'
      if (action === 'reject') return '已驳回'
      if (action === 'pending') return '审核中'
      if (action === 'not_reviewed') return '未审核'
      return action || '- -'
    },

    // 根据步骤获取审核人员
    getReviewersByStep (step) {
      return this.approvalRecords.filter(record => {
        // 处理步骤名称映射
        const stepMapping = {
          '初审': '初审',
          '复审': '复审',
          '终审': '终审',
          '安全措施': '安全措施确认',
          '措施落实': '安全复查确认'
        }

        return record.step === (stepMapping[step] || step)
      })
    },

    // 获取步骤标签颜色
    getStepTagColor (step) {
      const colorMap = {
        '初审': 'blue',
        '复审': 'purple',
        '终审': 'red',
        '安全措施确认': 'gold',
        '安全复查确认': 'orange',
        '措施落实': 'green'
      }
      return colorMap[step] || 'default'
    },

    // 获取步骤显示文本
    getStepDisplayText (step) {
      return step || '- -'
    },
    queryTicketDetail (id) {
      this.$get(`/cos/work-ticket/queryTicketDetail`, {
        id: id
      }).then((r) => {
        this.approvalRecords = r.data.data
      })
    },
    renderMarkdown (content) {
      if (!content) {
        return ''
      }
      if (window.marked) {
        try {
          return window.marked.parse(content)
        } catch (error) {
          console.error('Markdown解析错误:', error)
          return content
        }
      }
      return 'Marked.js 库未加载！'
    },
    handleClose () {
      this.$emit('close')
    },

    // 获取风险等级颜色
    getRiskLevelColor (level) {
      switch (level) {
        case '高': return 'red'
        case '中': return 'orange'
        case '低': return 'green'
        default: return 'blue'
      }
    },

    // 获取状态文本
    getStatusText (status) {
      const statusMap = {
        1: '正在初审',
        2: '正在复审',
        3: '正在终审',
        4: '已驳回',
        5: '安全措施',
        6: '措施落实'
      }
      return statusMap[status] || '未知状态'
    },

    // 获取状态颜色
    getStatusColor (status) {
      const colorMap = {
        1: 'orange',
        2: 'blue',
        3: 'purple',
        4: 'red',
        5: 'gold',
        6: 'green'
      }
      return colorMap[status] || 'gray'
    },

    // 获取分数颜色
    getScoreColor (score) {
      if (score >= 90) return 'green'
      if (score >= 70) return 'orange'
      return 'red'
    },

    // 获取当前步骤
    getCurrentStep () {
      if (!this.ticketData) return 0
      const status = this.ticketData.status
      const stepMap = {
        1: 0, // 初审
        2: 1, // 复审
        3: 2, // 终审
        4: 0, // 驳回回到初审
        5: 3, // 安全措施
        6: 4 // 措施落实
      }
      return stepMap[status] || 0
    },

    // 打印详情
    printDetail () {
      window.print()
    }
  }
}
</script>

<style lang="less" scoped>.work-ticket-detail-modal {
  .detail-section {
    margin-bottom: 16px;

    .info-item {
      display: flex;
      margin-bottom: 8px;

      .label {
        font-weight: bold;
        min-width: 80px;
        color: #666;
      }

      .value {
        flex: 1;
      }
    }
  }

  .modal-footer {
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}

// 打印样式
@media print {
  .work-ticket-detail-modal {
    .ant-modal-content {
      box-shadow: none;
    }

    .ant-modal-header,
    .modal-footer {
      display: none;
    }
  }
}

.work-ticket-detail-modal {
  .detail-section {
    margin-bottom: 16px;

    .info-item {
      display: flex;
      margin-bottom: 8px;

      .label {
        font-weight: bold;
        min-width: 80px;
        color: #666;
      }

      .value {
        flex: 1;
      }
    }
  }

  .step-detail {
    margin-top: 8px;

    .reviewers-list {
      .reviewer-item {
        padding: 8px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .reviewer-info {
          display: flex;
          align-items: center;
          margin-bottom: 4px;

          .reviewer-name {
            font-weight: 500;
            color: #333;
          }
        }

        .reviewer-comment {
          font-size: 12px;
          color: #666;
          margin: 4px 0;
          //padding-left: 30px;
        }

        .reviewer-time {
          font-size: 11px;
          color: #999;
          //padding-left: 30px;
        }
      }

      .no-reviewer {
        color: #999;
        font-style: italic;
        padding: 8px 0;
      }
    }
  }

  .modal-footer {
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}
</style>
