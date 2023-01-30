import { USER_KEY } from '@/constants/user'
import emitter from '@/utils/event'
// 设置存储
export function setStore (key, value) {
  if (!key) {
    console.warn('本地存储的key不能为空')
    return
  }
  if (value) {
    value = typeof value !== 'string' ? JSON.stringify(value) : value
  }
  localStorage.setItem(key, value)
}
// 获取存储
export function getStore (key) {
  if (!key) {
    console.warn('本地存储的key不能为空')
    return
  }
  let data = localStorage.getItem(key)
  if (data) {
    data = JSON.parse(data)
  }
  return data
}
// 清空存储
export function clearStore (key) {
  if (!key) {
    console.warn('本地存储的key不能为空')
    return
  }
  localStorage.removeItem(key)
}
// 设置用户信息
export function setUser (value) {
  setStore(USER_KEY, value) // 由于获取用户资料接口没有token 所以需要将两者合并
  emitter.$emit('userChange') // 触发用户信息修改
}
// 获取用户信息
export function getUser () {
  return getStore(USER_KEY)
}
// 清除用户存储的信息
export function clearUser () {
  clearStore(USER_KEY)
}
