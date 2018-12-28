/**
 * 有库存未治理,数据概览api
 */
import instance from './index';

export default {
  // 有库存未上线商品数量排名
  getStockOfflineCountSort: {
    p: '/dealboard/getStockOfflineCountSort',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 有库存未上线商品数量变化趋势
  getTrendOfChange: {
    p: '/dealboard/getTrendOfChange',
    r: params => {
      return instance.post(p, param)
    }
  }
}

