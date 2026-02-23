
const app = getApp();
let http = require('../../../utils/request')

Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    TabbarBot: app.globalData.tabbar_bottom,
    ticketInfo: null,
    ticketDetail: []
  },

  onLoad: function (options) {
    // 如果有传入的ID，则查询详情
    if (options.id) {
      this.queryTicketDetail(options.id)
    } else {
      // 使用您提供的模拟数据
      this.setMockData()
    }
  },

  // 设置模拟数据
  setMockData() {
    const mockData = {
      "msg": "success",
      "code": 0,
      "ticket": {
        "id": 6,
        "ticketCode": "WT-1771812346812",
        "type": "受限空间作业",
        "status": 5,
        "applicantId": 3,
        "workContent": "必须严格实行作业审批制度，严禁擅自进入。  必须做到\"先通风、再检测、后作业\"，严禁通风、检测不合格作业。  必须配备个人防中毒窒息等防护装备，设置安全警示标识。  必须对作业人员进行安全培训。  必须制定应急预案，并定期演练。  必须在外部设专人监护。  必须保持作业现场空气流通。  必须严禁盲目施救。",
        "location": "二期工地",
        "startTime": "2026-02-23 09:26:00",
        "endTime": "2026-02-24 09:26:00",
        "riskLevel": "高",
        "aiAuditScore": 85.00,
        "aiAuditSuggestion": "【风险等级：高】  \n依据《工贸企业受限空间作业安全管理与监督暂行规定》及GB 30871-2022《化学品生产单位特殊作业安全规范》，受限空间作业属高风险作业类别。本作业持续24小时（跨昼夜），涉及密闭/半密闭环境、潜在有毒有害气体积聚、缺氧窒息、救援难度大等固有风险；且方案中虽列明\"先通风、再检测、后作业\"等八项强制要求，但未提供具体通风参数（如风量、换气次数）、气体检测数据（O₂、H₂S、CO、可燃气体实测值及频次）、监护人员资质证明、应急装备清单（如正压式空气呼吸器型号及气瓶压力）等关键执行证据，风险管控存在形式化隐患，符合\"高风险\"判定标准（国家应急管理部《工贸行业重大事故隐患判定标准》第5条）。\n\n【算法审核得分：不适用】  \n该作业为现场安全作业管理行为，不涉及算法设计、模型训练、代码开发或数据处理等算法相关活动，无算法逻辑、输入输出、性能指标等可评估要素。故此项不适用，不予评分。\n\n【算法改进建议：不适用】  \n同上，本次作业属于传统工业安全管理体系范畴，不涉及算法应用环节。建议聚焦于**作业方案实质化提升**：① 补充检测记录模板（含时间戳、仪器编号、六类气体实测值及合格阈值）；② 明确监护人持证情况（如特种作业操作证编号及有效期）；③ 细化应急预案响应流程（如3分钟内启动气体复测+5分钟撤离机制）；④ 增加作业前JSA（工作安全分析）表及签字确认页。以上改进可将方案合规性从\"条款罗列\"升级为\"过程可控\"。（字数：398）",
        "createTime": "2026-02-23 10:05:46",
        "updateTime": "2026-02-23 12:00:41",
        "staffName": "悲伤的橘子树",
        "staffImages": "SA1771165538124.jpg",
        "deptName": "工程部",
        "positionName": "项目经理"
      },
      "ticketDetail": [
        {
          "staffName": "丁真",
          "deptName": "市场部",
          "positionName": "部门经理",
          "staffImages": "SA1770991139189.jpg",
          "step": "初审",
          "action": "通过",
          "comments": "丁真初审通过",
          "createDate": "2026-02-23 10:05:56"
        },
        {
          "staffName": "孙笑川",
          "deptName": "经理部",
          "positionName": "项目经理",
          "staffImages": "SA1771156168736.jpg",
          "step": "复审",
          "action": "通过",
          "comments": "孙笑川2026-02-23 10:05:56复审通过",
          "createDate": "2026-02-23 10:05:56"
        },
        {
          "staffName": "丁真",
          "deptName": "市场部",
          "positionName": "部门经理",
          "staffImages": "SA1770991139189.jpg",
          "step": "终审",
          "action": "通过",
          "comments": "丁真2026-02-23 10:05:56终审通过",
          "createDate": "2026-02-23 10:05:56"
        },
        {
          "staffName": "悲伤的橘子树",
          "deptName": "工程部",
          "positionName": "项目经理",
          "staffImages": "SA1771165538124.jpg",
          "step": "安全措施确认",
          "action": "未确认",
          "comments": null,
          "createDate": null
        },
        {
          "staffName": "管理员",
          "deptName": "- -",
          "positionName": "- -",
          "step": "安全复查确认",
          "action": "未处理",
          "staffImages": "女性-管理员.png",
          "comments": "- -",
          "createDate": "- -"
        }
      ]
    }

    this.setData({
      ticketInfo: mockData.ticket,
      ticketDetail: mockData.ticketDetail
    })
  },

  // 查询作业单详情
  queryTicketDetail(id) {
    http.get('queryTicketDetail', { id }).then((r) => {
      if (r.code === 0) {
        this.setData({
          ticketInfo: r.ticket,
          ticketDetail: r.ticketDetail
        })
      } else {
        wx.showToast({
          title: r.msg || '获取详情失败',
          icon: 'none',
          duration: 2000
        })
      }
    }).catch(error => {
      console.error('查询作业单详情失败:', error)
      wx.showToast({
        title: '网络请求失败',
        icon: 'none',
        duration: 2000
      })
    })
  }
})
