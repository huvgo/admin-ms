import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/dev/oshi',
    method: 'get',
    data
  })
}

