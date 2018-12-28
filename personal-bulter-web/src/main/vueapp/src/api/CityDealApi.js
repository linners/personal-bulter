/**
 * 有库存未治理,城市处理api
 */
import instance from './index';

export default {
  // 城市端可见处理商品列表
  queryList: {
    p: '/citydeal/queryList',
    r: params => {
      return instance.post(p, param)
    }
  }
}

