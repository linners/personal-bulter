/**
 * 经营看板, 生鲜看板api
 */
import instance from './index';

export default {
  // 生鲜标识url
  getFrushKanban: {
    p: '/kanban/getFrushKanban',
    r: params => {
      return instance.post(p, param)
    }
  }
}

