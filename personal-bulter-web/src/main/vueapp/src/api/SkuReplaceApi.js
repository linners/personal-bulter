/**
 * 商品汰换 api
 */
import instance from './index';

export default {
  // 分页查询
  pageList: {
    p: '/skureplace/pageList',
    r: params => {
      return instance.post(p, param)
    }
  }
}

