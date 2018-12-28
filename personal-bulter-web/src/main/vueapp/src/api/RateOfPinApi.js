/**
 * 动销率看板查询api
 */
import instance from './index';

export default {
  // 动销率图表和列表数据查询
  pageList: {
    p: '/rateOfPin/listRateOfPinPage',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 导出列表
  exportList: {
    p: '/rateOfPin/exportRateOfPin',
    r: params => {
      return instance.post(p, param)
    }
  }
}

