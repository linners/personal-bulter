/**
 * 经营看板, 标品看板api
 */
import instance from './index';

export default {
  // 图表数据查询
  getKanBanByCondition: {
    p: '/kanban/getKanBanByCondition',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 昨日经营水平概览
  getKanBanAllYes: {
    p: '/kanban/getKanBanAllYes',
    r: params => {
      return instance.post(p, param)
    }
  }
}

