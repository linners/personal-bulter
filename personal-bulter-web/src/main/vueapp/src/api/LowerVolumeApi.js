/**
 * 低动销商品查询api
 */
import instance from './index';

export default {
  // 查询列表
  pageList: {
    p: '/lowerVolume/pageListLowerVolume',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 导出列表
  exportList: {
    p: '/lowerVolume/exportLowerVolume',
    r: params => {
      return instance.post(p, param)
    }
  }
}

