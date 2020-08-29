import request from '@/utils/request'

const url = '/layout'

export function sidebar(data) {
  return request({
    url: url + '/sidebar',
    method: 'get',
    params: data
  })
}
