import Vue from 'vue'
import App from './App.vue'
import './permission' // permission control

import router from './router'

import './style/theme/index.css'
import Element from 'element-ui'
import './styles/index.scss' // global css
import Meta from 'vue-meta'
import '@/assets/iconfont/iconfont.js'
import '@/assets/iconfont/icon.css'
import moment from 'moment'

Vue.use(Meta)
Vue.config.productionTip = false
Vue.use(Element)

moment.locale('zh-cn')
Vue.filter('dateTimeFormat', function (value) {
  return moment(value).format('YYYY-MM-DD HH:mm:ss')
})

Vue.prototype.dateTimeFormatWithBr = function (value) {
  return moment(value).format('YYYY-MM-DD<br />HH:mm:ss')
}

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
