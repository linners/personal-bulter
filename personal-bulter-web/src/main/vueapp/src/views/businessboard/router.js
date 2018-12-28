import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/welcome'
import Nopromission from '@/components/Nopromission'
import KanBan from './views/kanban/Index'
import FrushKanban from './views/kanban/FrushKanban'
import SkuReplace from './views/sku_replace/Index'


Vue.use(Router)

let router = new Router({
  routes: [
    {
      path: "/",
      name: 'Welcome',
      component: Welcome
    },
    {
      path: '/KanBan',
      name: 'KanBan',
      component: KanBan
    },
    {
      path: '/frushKanban',
      name: 'frushKanban',
      component: FrushKanban
    },
    {
      path: '/skureplace',
      name: 'SkuReplace',
      component: SkuReplace
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