import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/engine/oshi',
    method: 'get',
    data
  })
}

