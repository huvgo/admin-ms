import request from '@/utils/request'

const url = '/audit/process'

export function getList(data) {
  return request({
    url: url,
    method: 'get',
    params: data
  })
}

export function activate(data) {
  return request({
    url: url + '/activate/' + data,
    method: 'get'
  })
}

export function suspend(data) {
  return request({
    url: url + '/suspend/' + data,
    method: 'get'
  })
}

export function del(data) {
  return request({
    url: url,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}
