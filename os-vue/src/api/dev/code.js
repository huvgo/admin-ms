import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/dev/code',
    method: 'get',
    data
  })
}

