import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/views/layout/Layout.vue'
Vue.use(Router)
export const asyncRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: '/login', // 默认子路由
    name: 'mainIndex',
    children: [
      {
        path: '/statistics/index',
        component: () => import('./views/news-statistics/index.vue'),
        meta: {
          path: '/statistics/index'
        }
      },
      {
        path: '/statistics/detail/:id',
        component: () => import('./views/news-statistics/detail.vue'),
        meta: {
          path: '/statistics/index'
        }
      },
      {
        path: '/news/publish',
        component: () => import('@/views/news/publish.vue'),
        meta: {
          path: '/news/publish'
        }
      },
      {
        path: '/news/index',
        component: () => import('./views/news/index.vue'),
        meta: {
          path: '/news/index'
        }
      },
      {
        path: '/material/index',
        component: () => import('./views/material/index.vue'),
        meta: {
          path: '/material/index'
        }
      },
      {
        path: '/fans/index',
        component: () => import('./views/fans-statistics/index.vue'),
        meta: {
          path: '/fans/index'
        }
      },
      {
        path: '/fans/portrait',
        component: () => import('./views/fans-portrait/index.vue'),
        meta: {
          path: '/fans/index'
        }
      },
      {
        path: '/fans/list',
        component: () => import('./views/fans-list/index.vue'),
        meta: {
          path: '/fans/index'
        }
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index.vue')
  },
  {
    path: '*',
    component: () => import('@/views/404.vue')
  }
]
var myRouter = new Router({
  routes: asyncRouterMap
})
export default myRouter
