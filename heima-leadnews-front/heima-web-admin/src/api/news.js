import Request from '@/utils/request'
import { API_NEWS_LIST, API_NEWS_ONE, API_NEWS_PASS, API_NEWS_FAIL, API_NEWS_PUBLISHED, API_NEWS_DOWN_OR_UP } from '@/constants/api'

export function authList (data) {
  return new Request({
    url: API_NEWS_LIST,
    method: 'post',
    data
  })
}

export function authOne (id) {
  return new Request({
    url: API_NEWS_ONE + id,
    method: 'get'
  })
}

export function authPass (data) {
  return new Request({
    url: API_NEWS_PASS,
    method: 'post',
    data
  })
}

export function authFail (data) {
  return new Request({
    url: API_NEWS_FAIL,
    method: 'post',
    data
  })
}

export function findPublished (data) {
  return new Request({
    url: API_NEWS_PUBLISHED,
    method: 'post',
    data
  })
}

// 文章上架&下架
export function downOrUp (data) {
  return new Request({
    url: API_NEWS_DOWN_OR_UP,
    method: 'post',
    data
  })
}
