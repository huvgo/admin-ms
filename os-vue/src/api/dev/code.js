import request from '@/utils/request'
import { downLoadZip } from '@/utils/zipdownload'

const url = '/dev/code'

export function getList(data) {
  return request({
    url: url,
    method: 'get',
    data
  })
}

export function generate(data) {
  downLoadZip(url + '/generate', data)
}
