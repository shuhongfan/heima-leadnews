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
        path: '/auth/index', // 用户审核
        component: () => import('./views/user-auth/index.vue'),
        meta: {
          path: '/auth/index'
        }
      },
      {
        path: '/auth/detail', // 实名认证
        component: () => import('@/views/user-auth/detail.vue'),
        meta: {
          path: '/auth/index'
        }
      },
      {
        path: '/channel/index', // 频道管理
        component: () => import('@/views/channel/index.vue'),
        meta: {
          path: '/channel/index'
        }
      },
      {
        path: '/news/index', // 内容审核
        component: () => import('@/views/news/index.vue'),
        meta: {
          path: '/news/index'
        }
      },
      {
        path: '/news/detail', // 内容审核
        component: () => import('@/views/news/detail.vue'),
        meta: {
          path: '/news/index'
        }
      },
      {
        path: '/news-published/index', // 内容管理
        component: () => import('@/views/news-published/index.vue'),
        meta: {
          path: '/news-published/index'
        }
      },
      {
        path: '/news-published/detail', // 内容审核
        component: () => import('@/views/news-published/detail.vue'),
        meta: {
          path: '/news-published/index'
        }
      },
      {
        path: '/sensitive/index', // 敏感词
        component: () => import('@/views/sensitive/index.vue'),
        meta: {
          path: '/sensitive/index'
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
