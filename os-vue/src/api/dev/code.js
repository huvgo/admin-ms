import request from '@/utils/request'

const url = '/dev/code'

export function getList(data) {
  return request({
    url: url,
    method: 'get',
    data
  })
}

