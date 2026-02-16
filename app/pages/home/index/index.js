const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        hidden: true,
        current: 0,
        lines: 0,
        swiperlist: [{
            id: 0,
            url: 'https://www.huolala.cn/rs/img/housemove/tab_1_bg.png',
            type: 1
        }, {
            id: 1,
            url: 'https://www.huolala.cn/rs/img/housemove/tab_3_bg.png',
            type: 2
        }, {
            id: 2,
            url: 'https://www.huolala.cn/rs/img/housemove/tab_4_bg.png',
            type: 3
        }],
        iconList: [{
            id: 1,
            icon: 'questionfill',
            color: 'red',
            name: '公告',
            type: 1
        }, {
            id: 3,
            icon: 'shopfill',
            color: 'yellow',
            name: '论坛',
            type: 3
        }],
        Headlines: [{
            id: 1,
            title: "配送员入驻",
            type: 1
        }, {
            id: 2,
            title: "配送大作战",
            type: 2
        }],
        shopInfo: [],
        postInfo: [],
        commodityHot: [],
        keys: '',
        videosrc: "http://wxsnsdy.tc.qq.com/105/20210/snsdyvideodownload?filekey=30280201010421301f0201690402534804102ca905ce620b1241b726bc41dcff44e00204012882540400&bizid=1023&hy=SH&fileparam=302c020101042530230204136ffd93020457e3c4ff02024ef202031e8d7f02030f42400204045a320a0201000400",
        addressList: [],
        startPoint: {
            show: false,
            id: null,
            address: '',
            point: null
        },
        endPoint: {
            show: false,
            id: null,
            address: '',
            point: null
        },
        userInfo: null,
        workType: '', // 当前选中的作业类型
        workContent: '', // 作业内容描述
        location: '', // 作业地点
        startTime: '', // 计划开始时间
        endTime: '', // 计划结束时间
        workTypeList: ['动火作业', '受限空间作业', '高处作业', '吊装作业', '临时用电作业', '动土作业', '断路作业', '盲板抽堵作业'],
        showWorkTypePicker: false,
        showStartTimePicker: false,
        showEndTimePicker: false,
        showEndTimePicker: false,
        libraryList: [], // 规则库列表
        matchedRules: [], // 匹配到的规则
        currentTime: new Date().getTime()
    },
    onLoad: function () {
        this.queryLibraryKeyword()
    },
    onShow() {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    userInfo: res.data
                })
            },
            fail: res => {

            }
        })
    },
    queryLibraryKeyword(key) {
        http.get('queryLibraryAll').then((r) => {
            this.setData({libraryList: r.data})
        })
    },
    // 检测作业内容中的关键字
    checkKeywords(workContent) {
        const {libraryList} = this.data;
        let matchedRules = [];

        if (!workContent || !libraryList || libraryList.length === 0) {
            this.setData({matchedRules: []});
            return;
        }

        // 遍历规则库
        libraryList.forEach(rule => {
            if (rule.isActive !== 1) return; // 只检查激活的规则

            const keywords = rule.keyword.split(',').map(k => k.trim());
            console.log(keywords)
            let isMatched = false;

            // 检查每个关键字是否在作业内容中
            for (let keyword of keywords) {
                if (keyword && workContent.includes(keyword)) {
                    isMatched = true;
                    break;
                }
            }

            if (isMatched) {
                matchedRules.push({
                    ruleName: rule.ruleName,
                    mandatoryMeasure: rule.mandatoryMeasure,
                    riskWarning: rule.riskWarning
                });
            }
        });

        this.setData({matchedRules: matchedRules});
    },
    onWorkContentChange(event) {
        const workContent = event.detail;
        this.setData({workContent: workContent});
        // 实时检测关键字
        this.checkKeywords(workContent);
    },
    selectWorkType() {
        this.setData({showWorkTypePicker: true});
    },
    onConfirmWorkType(event) {
        this.setData({
            workType: event.detail.value,
            showWorkTypePicker: false
        });
    },
    onCloseWorkTypePicker() {
        this.setData({showWorkTypePicker: false});
    },
    // onWorkContentChange(event) {
    //     this.setData({ workContent: event.detail.value });
    // },
    onLocationChange(event) {
        this.setData({location: event.detail});
    },
    selectStartTime() {
        this.setData({showStartTimePicker: true});
    },
    onConfirmStartTime(event) {
        const selectedTime = event.detail;
        this.setData({
            startTime: this.formatDateTime(selectedTime),
            showStartTimePicker: false
        });
        // 如果结束时间早于开始时间，则清空结束时间
        if (this.data.endTime && new Date(this.data.endTime) < new Date(selectedTime)) {
            this.setData({
                endTime: ''
            });
        }
    },
    onCloseStartTimePicker() {
        this.setData({showStartTimePicker: false});
    },
    selectEndTime() {
        this.setData({showEndTimePicker: true});
    },
    onConfirmEndTime(event) {
        this.setData({
            endTime: this.formatDateTime(event.detail),
            showEndTimePicker: false
        });
    },
    onCloseEndTimePicker() {
        this.setData({showEndTimePicker: false});
    },
    // 添加时间格式化函数
    formatDateTime(date) {
        if (!date) return '';
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    submit() {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    userInfo: res.data
                })
                // 表单校验
                if (!this.data.workType) {
                    wx.showToast({
                        title: '请选择作业类型',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                if (!this.data.workContent.trim()) {
                    wx.showToast({
                        title: '请输入作业内容描述',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                if (!this.data.location.trim()) {
                    wx.showToast({
                        title: '请输入作业地点',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                if (!this.data.startTime) {
                    wx.showToast({
                        title: '请选择计划开始时间',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                if (!this.data.endTime) {
                    wx.showToast({
                        title: '请选择计划结束时间',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                // 时间合理性校验
                const startDate = new Date(this.data.startTime.replace(/-/g, '/'));
                const endDate = new Date(this.data.endTime.replace(/-/g, '/'));

                if (startDate >= endDate) {
                    wx.showToast({
                        title: '结束时间必须晚于开始时间',
                        icon: 'none',
                        duration: 2000
                    });
                    return;
                }

                // 提交表单逻辑
                console.log('提交数据:', {
                    applicantId: res.data.id,
                    type: this.data.workType,
                    workContent: this.data.workContent,
                    location: this.data.location,
                    startTime: this.data.startTime,
                    endTime: this.data.endTime
                });
                let params = {
                    applicantId: res.data.id,
                    type: this.data.workType,
                    workContent: this.data.workContent,
                    location: this.data.location,
                    startTime: this.data.startTime,
                    endTime: this.data.endTime
                }
                http.post('saveWorkTicket', params).then((r) => {
                    // 这里添加实际的提交逻辑
                    wx.showToast({
                        title: '提交成功',
                        icon: 'success',
                        duration: 2000
                    });
                    // 提交成功后清空表单
                    this.setData({
                        workType: '',
                        workContent: '',
                        location: '',
                        startTime: '',
                        endTime: '',
                        matchedRules: [] // 同时清空匹配的规则
                    });
                })
            },
            fail: res => {
                wx.showToast({
                    title: '请先进行登录',
                    icon: 'none',
                    duration: 2000
                })
            }
        })
    },
    /**
     * 选择位置
     */
    startChooseLocation() {
        const _this = this;
        wx.chooseLocation({
            success(res) {
                console.log(res)
                _this.setData({
                    ['startPoint.startAddress']: res.address,
                    ['startPoint.point']: {
                        latitude: res.latitude,
                        longitude: res.longitude
                    },
                })
            },
            fail(e) {
                console.log(e);
            }
        })
    },
    /**
     * 选择位置
     */
    endChooseLocation() {
        const _this = this;
        wx.chooseLocation({
            success(res) {
                console.log(res)
                _this.setData({
                    ['endPoint.endAddress']: res.address,
                    ['endPoint.point']: {
                        latitude: res.latitude,
                        longitude: res.longitude
                    },
                })
            },
            fail(e) {
                console.log(e);
            }
        })
    },
    openPopup(e) {
        if (e.currentTarget.dataset.type == 1) {
            this.setData({
                'startPoint.show': true,
            })
        } else if (e.currentTarget.dataset.type == 2) {
            this.setData({
                'endPoint.show': true,
            })
        }
    },
    // submit() {
    //     console.log(this.data.startPoint)
    //     console.log(this.data.endPoint)
    //     if (this.data.startPoint.id == null || this.data.endPoint.id == null) {
    //         wx.showToast({
    //             title: '请选择地址',
    //             icon: 'error',
    //             duration: 1000
    //         })
    //         return false;
    //     }
    //     let param = {
    //         startPoint: this.data.startPoint,
    //         endPoint: this.data.endPoint
    //     }
    //     wx.navigateTo({
    //         url: '/pages/scar/order/index?address=' + JSON.stringify(param)
    //     });
    // },
    timeFormat(time) {
        var nowTime = new Date();
        var day = nowTime.getDate();
        var hours = parseInt(nowTime.getHours());
        var minutes = nowTime.getMinutes();
        // 开始分解付入的时间
        var timeday = time.substring(8, 10);
        var timehours = parseInt(time.substring(11, 13));
        var timeminutes = time.substring(14, 16);
        var d_day = Math.abs(day - timeday);
        var d_hours = hours - timehours;
        var d_minutes = Math.abs(minutes - timeminutes);
        if (d_day <= 1) {
            switch (d_day) {
                case 0:
                    if (d_hours == 0 && d_minutes > 0) {
                        return d_minutes + '分钟前';
                    } else if (d_hours == 0 && d_minutes == 0) {
                        return '1分钟前';
                    } else {
                        return d_hours + '小时前';
                    }
                    break;
                case 1:
                    if (d_hours < 0) {
                        return (24 + d_hours) + '小时前';
                    } else {
                        return d_day + '天前';
                    }
                    break;
            }
        } else if (d_day > 1 && d_day < 10) {
            return d_day + '天前';
        } else {
            return time;
        }
    },
    shopDeatil(e) {
        wx.navigateTo({
            url: '/pages/shop/index/index?shopId=' + e.currentTarget.dataset.shopid + ''
        });
    },
    home() {
        http.get('home').then((r) => {
            console.log(r)

            // r.shopInfo.forEach(item => {
            // 	if (item.images) {
            // 		item.image = item.images.split(',')[0]
            // 	}
            // });
            r.postInfo.forEach(item => {
                if (item.images) {
                    item.image = item.images.split(',')[0]
                }
                item.days = this.timeFormat(item.createDate)
            });
            this.setData({
                shopInfo: r.shopInfo,
                postInfo: r.postInfo,
                commodityHot: r.commodityHot
            })
        })
    },
    postDetail(event) {
        wx.navigateTo({
            url: '/pages/coupon/detail/index?postId=' + event.currentTarget.dataset.postid + ''
        });
    },
    swiperchange: function (e) {
        this.setData({
            current: e.detail.current
        });
    },
    swipclick: function (e) {
        let that = this;
        var swip = that.data.swiperlist[that.data.current];
        console.log(swip);
        if (swip.type === 1) {
            wx.navigateTo({
                url: '/pages/home/doc/index?id=' + swip.id
            });
        }
    },
    lineschange: function (e) {
        this.setData({
            lines: e.detail.current
        });
    },
    linesclick: function (e) {
        let that = this;
        var swip = that.data.Headlines[that.data.current];
        console.log(swip);
        if (swip.type === 1) {
            wx.navigateTo({
                url: '/pages/home/doc/index?id=' + swip.id
            });
        }
    },
    itemckcred: function (e) {
        let that = this;
        var item = e.currentTarget.dataset;
        console.log(item.index, item.itemtype)
        if (item.itemtype === 1) {
            wx.navigateTo({
                url: '/pages/home/bulletin/index'
            });
        }
        if (item.itemtype === 2) {
            wx.navigateTo({
                url: '/pages/home/groom/index'
            });
        }
        if (item.itemtype === 3) {
            wx.navigateTo({
                url: '/pages/coupon/index/index'
            });
        }
        if (item.itemtype === 4) {
            wx.navigateTo({
                url: '/pages/home/search/index?key=生鲜'
            });
        }
    },
    getKeysValue: function (e) {
        this.setData({
            keys: e.detail.value
        })
    },
    search: function () {
        wx.navigateTo({
            url: '/pages/home/search/index?key=' + this.data.keys + ''
        });
    }
});
