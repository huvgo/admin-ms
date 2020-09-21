import request from '@/utils/request'

const url = '/process/instance'

export function getMyToDo(data) {
  return request({
    url: url + '/my-to-do',
    method: 'get',
    params: data
  })
}

export function apply(data) {
  return request({
    url: url + '/apply',
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}

export function approve(data) {
  return request({
    url: url + '/approve',
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}
