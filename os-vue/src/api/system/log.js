import request from '@/utils/request'

const url = '/system/log'

export function getList(data) {
  return request({
    url: url,
    method: 'get',
    params: data
  })
}

export function add(data) {
  return request({
    url: url,
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
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

export function update(data) {
  return request({
    url: url,
    method: 'put',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}
