/**
 * 有库存未治理,开启治理api
 */
import instance from './index';

export default {
  // 有库存未上线商品数量排名
  getGovernSetting: {
    p: '/dealconfig/getGovernSetting',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 城市端负责人名单列表
  queryList: {
    p: '/dealconfig/queryList',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 保存
  newCityUser: {
    p: '/dealconfig/newCityUser',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 修改
  editCityUser: {
    p: '/dealconfig/editCityUser',
    r: params => {
      return instance.post(p, param)
    }
  },
  // 删除
  delCityUser: {
    p: '/dealconfig/delCityUser',
    r: params => {
      return instance.post(p, param)
    }
  }
}

