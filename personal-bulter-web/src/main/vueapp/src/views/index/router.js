import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/welcome'
import Nopromission from '@/components/Nopromission'
import DealConfig from './views/deal-config/Index'
import CityDeal from './views/city-deal/Index'
import DealResult from './views/deal-result/Index'
import DealDashboard from './views/deal-dashboard/Index'

import JssjShow from './views/zombie/JssjShow'
import SsuStockOffline from './views/stockoffline/SsuStockOffline'
import RateOfPinShow from './views/rateofpin/RateOfPinShow'
import LowerVolumeShow from './views/lowervolume/LowerVolumeShow'


Vue.use(Router)

let router = new Router({
  routes: [
    {
      path: "/",
      name: 'Welcome',
      component: Welcome
    },
    {
        path: '/jssjShow',
        name: 'JssjShow',
        component: JssjShow
    },{
        path: '/ssuStockOffline',
        name: 'SsuStockOffline',
        component: SsuStockOffline
    }
    // 动销率看板功能   huahan
    ,{
        path: '/rateOfPinShow',
        name: 'RateOfPinShow',
        component: RateOfPinShow
    }
    // 低动销商品信息查询   huahan
    ,{
        path: '/lowerVolumeShow',
        name: 'LowerVolumeShow',
        component: LowerVolumeShow
    },
    {
      path: '/dealdashboard',
      name: 'deal-dashboard',
      component: DealDashboard
    },
    {
      path: '/dealresult',
      name: 'deal-result',
      component: DealResult
    },
	  {
		  path: '/dealconfig',
		  name: 'deal-config',
		  component: DealConfig
	  },
    {
      path: '/citydeal',
      name: 'city-deal',
      component: CityDeal
    },
    {
      path: '*',
      redirect: '/'
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.query.out_visit != undefined && to.query.out_visit == "true"){
    localStorage.setItem("out_visit","true");
  }else {
    localStorage.setItem("out_visit","false");
  }
  next();
});

export default router