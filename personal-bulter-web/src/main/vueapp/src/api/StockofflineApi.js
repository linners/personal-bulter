/**
 * 有库存未上线api
 */
import instance from './index';

export default {
  // 查询列表
  pageList: {
    p: '/ssuStockOffline/getSsuStockOfflineStatistic',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 导出列表
  exportList: {
    p: '/ssuStockOffline/exportSsuStockOfflineStatistic',
    r: params => {
      return instance.post(p, param)
    }
  }
}

