import Request from '@/utils/request'
import { API_COMMON_LIST, API_COMMON_UPDATE, API_COMMON_DELETE } from '@/constants/api'

export function loadList (data) {
  return new Request({
    url: API_COMMON_LIST,
    method: 'post',
    data
  })
}

export function updateData (data) {
  return new Request({
    url: API_COMMON_UPDATE,
    method: 'post',
    data
  })
}

export function deleteData (data) {
  return new Request({
    url: API_COMMON_DELETE,
    method: 'post',
    data
  })
}
