/**
 * 有库存未治理,治理结果api
 */
import instance from './index';

export default {
  // 治理商品排名
  getChartData: {
    p: '/dealresult/getChartData',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 治理商品信息
  queryList: {
    p: '/dealresult/queryList',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 导出列表
  exportList: {
    p: '/dealresult/exportGovernResult',
    r: params => {
      return instance.post(p, param)
    }
  }
}

