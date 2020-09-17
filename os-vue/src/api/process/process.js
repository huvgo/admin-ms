import request from '@/utils/request'

const url = '/process'

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

