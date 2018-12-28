import Vue from 'vue'
import '@/plugins/element.js'
import router from './router'
import store from './store'
import echarts from 'echarts'
import Axios from '@/utils/axios'
import App from './App.vue'

Vue.prototype.$echarts = echarts
Vue.prototype.$http = Axios
Vue.prototype.$bus = new Vue()

/**
 * v-has
 */
Vue.directive('has', {
  inserted: function(el, binding) {
    if (!Vue.prototype.$_has(binding.value)) {
      el.parentNode.removeChild(el);
    }
  }
});

Vue.config.productionTip = false

new Vue({
  store,
  router,
  render: h => h(App)
}).$mount('#app')
