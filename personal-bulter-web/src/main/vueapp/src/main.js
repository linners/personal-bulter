import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import './plugins/element.js'

import Axios from './utils/axios'

import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/index.css'

Vue.use(VXETable)

Vue.config.productionTip = false

Vue.prototype.$http = Axios

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')
