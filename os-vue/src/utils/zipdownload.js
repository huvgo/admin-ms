import axios from 'axios'
import { getToken } from '@/utils/auth'

const mimeMap = {
  xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  zip: 'application/zip'
}

const baseUrl = process.env.VUE_APP_BASE_API

export function downLoadZip(str, data) {
  var url = baseUrl + str
  axios({
    method: 'post',
    url: url,
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json; charset=utf-8',
      'X-Token': getToken()
    },
    data: JSON.stringify(data)
  }).then(res => {
    resolveBlob(res, mimeMap.zip)
  })
}
/**
 * 解析blob响应内容并下载
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 */
function resolveBlob(res, mimeType) {
  const aLink = document.createElement('a')
  const blob = new Blob([res.data], { type: mimeType })
  const patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*')
  const contentDisposition = decodeURI(res.headers['content-disposition'])
  const result = patt.exec(contentDisposition)
  let fileName = result[1]
  fileName = fileName.replace(/\"/g, '')
  aLink.href = URL.createObjectURL(blob)
  aLink.setAttribute('download', fileName) // 设置下载文件名称
  document.body.appendChild(aLink)
  aLink.click()
  document.body.appendChild(aLink)
}
