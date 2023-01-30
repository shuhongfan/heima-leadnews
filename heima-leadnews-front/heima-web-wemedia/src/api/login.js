import request from '@/utils/request'
import { setUser } from '@/utils/store'
import { API_GETPHONECODE, API_USERAUTH, API_CAPTCHAS } from '@/constants/api'

export function loginByUsername (name, password) {
  const data = {
    name, password
  }
  return request({
    url: API_USERAUTH,
    method: 'post',
    data
  }).then(result => {
    if (result.code === 0) {
      const temp = result.data
      setUser({ name: temp.user.name, photo: null, token: temp.token }) // 设置用户的个人数据
    }
    return result
  })
}

export function getMobileCode (mobile, params) {
  // 获取短信验证码
  return request({
    url: API_GETPHONECODE + mobile,
    method: 'get',
    params
  })
}
// 获取人机验证码
export function getCaptchas (mobile) {
  return request({
    url: API_CAPTCHAS + mobile,
    method: 'get'
  })
}
