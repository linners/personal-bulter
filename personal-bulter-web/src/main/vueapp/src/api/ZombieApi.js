/**
 * 滞销商品查询api
 */
import instance from './index';

export default {
  // 查询列表
  pageList: {
    p: '/zombie/pageListZombie',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 导出列表
  exportList: {
    p: '/zombie/exportZombieStatistic',
    r: params => {
      return instance.post(p, param)
    }
  }
}

