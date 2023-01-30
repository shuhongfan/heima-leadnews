import Request from '@/utils/request'
import { API_USERPROFILE, API_USERUPDATEPROFILE, API_HEAD } from '@/constants/api'

// 获取用户个人信息
export function getUserProfile () {
  return Request({
    url: API_USERPROFILE,
    method: 'get'
  })
}

// 编辑用户信息
export function updateUserProfile (data) {
  return Request({
    url: API_USERUPDATEPROFILE,
    method: 'post',
    data
  })
}

// 修改用户的头像
export function updateUserHead (data) {
  return Request({
    url: API_HEAD,
    method: 'patch',
    data
  })
}
