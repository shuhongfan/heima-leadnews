import Request from '@/utils/request'
import { API_SENSITIVE_LIST, API_SENSITIVE_SAVE, API_SENSITIVE_UPDATE, API_SENSITIVE_DELETE } from '@/constants/api'

export function loadList (data) {
  return new Request({
    url: API_SENSITIVE_LIST,
    method: 'post',
    data
  })
}

export function saveData (data) {
  return new Request({
    url: API_SENSITIVE_SAVE,
    method: 'post',
    data
  })
}

export function updateData (data) {
  return new Request({
    url: API_SENSITIVE_UPDATE,
    method: 'post',
    data
  })
}

export function deleteData (id) {
  return new Request({
    url: API_SENSITIVE_DELETE + id,
    method: 'delete'
  })
}
