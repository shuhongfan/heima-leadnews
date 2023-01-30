import Request from '@/utils/request'
import { API_AUTH_LIST, API_AUTH_PASS, API_AUTH_FAIL } from '@/constants/api'

export function findAuthList (data) {
  return new Request({
    url: API_AUTH_LIST,
    method: 'post',
    data
  })
}

export function authPass (data) {
  return new Request({
    url: API_AUTH_PASS,
    method: 'post',
    data
  })
}

export function authFail (data) {
  return new Request({
    url: API_AUTH_FAIL,
    method: 'post',
    data
  })
}
