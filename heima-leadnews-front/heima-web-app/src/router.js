/*global Vue*/
import Router from 'vue-router'
import routes from '@/routers/index'
import navigator from '@/utils/navigator'

Vue.use(Router)
const router = new Router({
    routes:routes
});
// 注册导航器属性
router.$navigator = navigator
// 注册返回方法
router.back = function(){
    let to = this.$navigator.back()
    if(to){
        this.push(to)
    }
}
// 路由之前记录路由处理
router.beforeResolve((to, from, next) => {
    router.$navigator.push(to, from, next)
})
export {router}
