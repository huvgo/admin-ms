import request from '@/utils/request'

const url = '/dev/code'

export function getList(data) {
  return request({
    url: url,
    method: 'get',
    data
  })
}

export function generator(data) {
  return request({
    url: url + '/generator',
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}

