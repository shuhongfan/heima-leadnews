/*global Vue*/
import lang from '@/langs/lang'
import conf from '@/common/conf'
import request from '@/common/request'
import store from '@/stores/store'
import date from '@/utils/date'

Vue.prototype.$date = date
Vue.prototype.$lang = lang
Vue.prototype.$config = conf
Vue.prototype.$store = store
request.setStore(store)
Vue.prototype.$request = request

/* weex initialized here, please do not move this line */
const { router } = require('./router');
const App = require('@/main.vue');
/* eslint-disable no-new */
new Vue(Vue.util.extend({el: '#root', router}, App));
router.push('/screen');

