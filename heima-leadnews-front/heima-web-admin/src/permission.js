import router, { asyncRouterMap } from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
// import { getUser } from '@/utils/store'

// import LoadingManage from '@/utils/loading'
NProgress.configure({ showSpinner: false })
router.beforeEach((to, from, next) => {
  // let user = getUser() //拉取个人用户信息
  // if(checkInPermission(to.path) && (!user || !user.id)) {
  //   next('/login')
  // }else{
  NProgress.start()
  // LoadingManage.openLoading()
  next()
  // }
})
router.afterEach(() => {
  // LoadingManage.closeLoading()
  NProgress.done() // finish progress bar
})
// 检查当前的地址是否在权限范围内
function checkInPermission (path) {
  const list = asyncRouterMap[0].children
  if (list.some(item => item.path.indexOf(path) > -1)) {
    return true
  }
  return false
}
