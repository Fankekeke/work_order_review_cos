const app = getApp();
let http = require('../../../utils/request')
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    TabbarBot: app.globalData.tabbar_bottom,
    bulletinList: [],
    bulletin: {},
    TabCur: 0,
    scrollLeft: 0
  },
  onLoad: function (options) {
    console.log(options.bulletinId)
    this.queryBulletinDetail(options.bulletinId)
  },
  tabSelect(e) {
    console.log(e.currentTarget.dataset.id)
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      scrollLeft: (e.currentTarget.dataset.id-1)*60
    })
  },
  queryBulletinDetail(id) {
    http.get('queryBulletinDetail', {id: id}).then((r) => {
      if (r.data.images) {
        r.data.imagesList = r.data.images.split(',')
      }
      this.setData({bulletin: r.data})
    })
  },
  getPostInfo() {
    http.get('getBulletinList').then((r) => {
      r.data.forEach(item => {
        if (item.images) {
          item.imagesList = item.images.split(',')
        }
      });
      this.setData({bulletinList: r.data})
    })
  }
});
