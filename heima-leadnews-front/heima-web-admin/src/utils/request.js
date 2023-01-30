import axios from 'axios'
import BigInt from 'json-bigint'
import Router from '@/router'
import { Message, MessageBox } from 'element-ui'
import { getUser, setUser, clearUser } from '@/utils/store'
// import LoadingManage from './loading'
// create an axios instance
const service = axios.create({
  baseURL: process.env.BASEURL, // api 的 base_url
  timeout: 10000, // request timeout
  transformResponse (data) {
    if (data) { return BigInt.parse(data) } // 由于后端的数据库对id进行了变更 所以这里必须采用json-bigint插件来进行处理 保证数据正确
  }
})
// request
service.interceptors.request.use(
  config => {
    // LoadingManage.openLoading(); //打开弹层
    const Authorization = getUser()
    if (Authorization && Authorization.token) {
      // 如果当前缓存中存在用户令牌 后台接口的参数格式
      config.headers['Content-Type'] = 'application/json'
      config.headers.token = Authorization.token
    }
    // 让每个请求携带token-- ['X-Token']为自定义key
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response
service.interceptors.response.use(
  /**
   * 监控拦截 如果出现 异常 则直接终止请求链
   */
  response => {
    if (response.headers && response.headers.refresh_token) {
      const user = getUser()
      if (user) {
        user.token = response.headers.refresh_token
        setUser(user)
      }
    }
    // LoadingManage.closeLoading() //关闭loading弹层
    // 直接返回返回体中的结构
    if (response.data && response.data.code) {
      const code = response.data.code
      if (code > 49 && code < 70) {
        Router.replace({ path: '/login' })
      }
    }
    return response.data
  },
  error => {
    // LoadingManage.closeLoading() // 关闭loading弹层
    let message = ''
    const code = error.response ? error.response.status : ''
    switch (code) {
      case 400:
        message = '请求参数错误'
        break
      case 401:
        message = 'token过期或未传'
        break
      case 403:
        message = '操作失败'
        break
      case 404:
        message = '网络错误，请稍后重试'
        break
      case 500:
        message = '服务器异常'
        break
      case 507:
        message = '服务器数据库异常'
        break
      default :
        message = '网络错误，请稍后重试'
    }
    // message = message + ':' +  error.response ? error.response.data.message : error.response.data.message
    // TODO: 可能会出现多个确认框
    if (code === 401) {
      MessageBox.confirm('登录超时，请重新登录', '温馨提示', {
        confirmButtonText: '确定',
        confirmButtonClass: 'el-button--danger',
        cancelButtonText: '取消',
        cancelButtonClass: 'el-button--warning'
      }).then(() => {
        clearUser() // 退出前要清除掉用户的信息
        Router.replace({ path: '/login' })
      }).catch(() => {
        // this.$message({ type: 'info', message: '已取消退出登录' })
      })
    } else {
      Message({ message, type: 'warning' })
    }
    return new Promise(function () {}) // 终止当前的promise链
  }
)

export default service
