import Home from './home'

let routes = []

let concat = (router) => {
    routes = routes.concat(router)
}
// 合并‘主页’相关路由
routes = routes.concat(Home)

export default  routes;
