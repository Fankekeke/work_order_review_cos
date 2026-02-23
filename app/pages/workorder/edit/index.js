
const app = getApp();
let http = require('../../../utils/request')

Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    TabbarBot: app.globalData.tabbar_bottom,
    ticketInfo: null,
    originalTicketInfo: null, // 保存原始数据用于重置
    workTypes: [
      { id: "动火作业", name: "动火作业" },
      { id: "受限空间作业", name: "受限空间作业" },
      { id: "高处作业", name: "高处作业" },
      { id: "吊装作业", name: "吊装作业" },
      { id: "临时用电作业", name: "临时用电作业" },
      { id: "动土作业", name: "动土作业" },
      { id: "断路作业", name: "断路作业" },
      { id: "盲板抽堵作业", name: "盲板抽堵作业" }
    ],
    selectedTypeIndex: 0,
    editableContent: "",
    editableLocation: "",
    editableStartTime: "",
    editableEndTime: "",
    minDateTime: ""
  },

  onLoad: function (options) {
    // 设置最小时间（当前时间）
    this.setMinDateTime();

    // 如果有传入的ID，则查询详情
    if (options.id) {
      this.queryTicketDetail(options.id)
    } else {
      // 使用您提供的模拟数据
      this.setMockData()
    }
  },

  // 设置最小时间
  setMinDateTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    this.setData({
      minDateTime: `${year}-${month}-${day} ${hours}:${minutes}`
    });
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
        "status": 1, // 改为待审核状态以便测试编辑功能
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
      }
    };

    // 找到对应的作业类型索引
    const typeIndex = this.data.workTypes.findIndex(type => type.name === mockData.ticket.type);

    this.setData({
      ticketInfo: mockData.ticket,
      originalTicketInfo: JSON.parse(JSON.stringify(mockData.ticket)), // 深拷贝保存原始数据
      selectedTypeIndex: typeIndex >= 0 ? typeIndex : 0,
      editableContent: mockData.ticket.workContent,
      editableLocation: mockData.ticket.location,
      editableStartTime: mockData.ticket.startTime,
      editableEndTime: mockData.ticket.endTime
    });
  },


// 在 Page 对象中添加以下方法

// 获取状态样式类
  getStatusClass(status) {
    const statusMap = {
      1: 'status-pending',
      2: 'status-processing',
      3: 'status-approved',
      4: 'status-rejected',
      5: 'status-completed'
    };
    return statusMap[status] || 'status-default';
  },

// 获取状态文本
  getStatusText(status) {
    const statusMap = {
      1: '待审核',
      2: '审核中',
      3: '已批准',
      4: '已拒绝',
      5: '已完成'
    };
    return statusMap[status] || '未知状态';
  },

// 获取时间范围文本
  getTimeRangeText() {
    if (!this.data.editableStartTime && !this.data.editableEndTime) {
      return '未设置时间';
    }
    if (this.data.editableStartTime && this.data.editableEndTime) {
      return `${this.data.editableStartTime} 至 ${this.data.editableEndTime}`;
    }
    return `${this.data.editableStartTime || '未设置'} 至 ${this.data.editableEndTime || '未设置'}`;
  },

  // 查询作业单详情
  queryTicketDetail(id) {
    http.get('queryTicketDetail', { id }).then((r) => {
      if (r.code === 0) {
        // 找到对应的作业类型索引
        const typeIndex = this.data.workTypes.findIndex(type => type.name === r.ticket.type);

        this.setData({
          ticketInfo: r.ticket,
          originalTicketInfo: JSON.parse(JSON.stringify(r.ticket)), // 深拷贝保存原始数据
          selectedTypeIndex: typeIndex >= 0 ? typeIndex : 0,
          editableContent: r.ticket.workContent,
          editableLocation: r.ticket.location,
          editableStartTime: r.ticket.startTime,
          editableEndTime: r.ticket.endTime
        });
      } else {
        wx.showToast({
          title: r.msg || '获取详情失败',
          icon: 'none',
          duration: 2000
        });
      }
    }).catch(error => {
      console.error('查询作业单详情失败:', error);
      wx.showToast({
        title: '网络请求失败',
        icon: 'none',
        duration: 2000
      });
    });
  },

  // 作业类型选择变化
  onTypeChange(e) {
    this.setData({
      selectedTypeIndex: e.detail.value
    });
  },

  // 作业内容输入
  onContentInput(e) {
    this.setData({
      editableContent: e.detail.value
    });
  },

  // 作业地点输入
  onLocationInput(e) {
    this.setData({
      editableLocation: e.detail.value
    });
  },

  // 开始时间选择
  onStartTimeChange(e) {
    this.setData({
      editableStartTime: e.detail.value
    });
  },

  // 结束时间选择
  onEndTimeChange(e) {
    this.setData({
      editableEndTime: e.detail.value
    });
  },

  // 保存修改
  saveChanges() {
    // 验证必填字段
    if (!this.data.editableContent.trim()) {
      wx.showToast({
        title: '请输入作业内容',
        icon: 'none',
        duration: 2000
      });
      return;
    }

    if (!this.data.editableLocation.trim()) {
      wx.showToast({
        title: '请输入作业地点',
        icon: 'none',
        duration: 2000
      });
      return;
    }

    // if (!this.data.editableStartTime) {
    //   wx.showToast({
    //     title: '请选择开始时间',
    //     icon: 'none',
    //     duration: 2000
    //   });
    //   return;
    // }
    //
    // if (!this.data.editableEndTime) {
    //   wx.showToast({
    //     title: '请选择结束时间',
    //     icon: 'none',
    //     duration: 2000
    //   });
    //   return;
    // }
    //
    // // 验证时间逻辑
    // if (this.data.editableStartTime >= this.data.editableEndTime) {
    //   wx.showToast({
    //     title: '结束时间必须晚于开始时间',
    //     icon: 'none',
    //     duration: 2000
    //   });
    //   return;
    // }

    // 构造更新数据
    const updateData = {
      id: this.data.ticketInfo.id,
      type: this.data.workTypes[this.data.selectedTypeIndex].name,
      workContent: this.data.editableContent,
      location: this.data.editableLocation
    };

    // 显示加载提示
    wx.showLoading({
      title: '保存中...'
    });

    // 调用API保存修改
    http.post('updateTicket', updateData).then((r) => {
      wx.hideLoading();
      wx.showToast({
        title: '保存成功',
        icon: 'success',
        duration: 2000
      });

      // 返回上一页
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    })
  },

  // 重置修改
  resetChanges() {
    wx.showModal({
      title: '提示',
      content: '确定要重置所有修改吗？',
      success: (res) => {
        if (res.confirm) {
          this.setData({
            selectedTypeIndex: this.data.workTypes.findIndex(type => type.name === this.data.originalTicketInfo.type),
            editableContent: this.data.originalTicketInfo.workContent,
            editableLocation: this.data.originalTicketInfo.location,
            editableStartTime: this.data.originalTicketInfo.startTime,
            editableEndTime: this.data.originalTicketInfo.endTime
          });

          wx.showToast({
            title: '已重置',
            icon: 'success',
            duration: 1000
          });
        }
      }
    });
  }
});

// 扩展Date原型添加格式化方法
Date.prototype.format = function(fmt) {
  const o = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3),
    "S": this.getMilliseconds()
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (let k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
};
