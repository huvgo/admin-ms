import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/monitor/oshi',
    method: 'get',
    data
  })
}

