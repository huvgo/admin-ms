import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/sys/user/login',
    method: 'post',
    data
  })
}
export function logout() {
  return request({
    url: '/sys/user/logout',
    method: 'post'
  })
}

export function getInfo(token) {
  return request({
    url: '/sys/user/token',
    method: 'get',
    params: { token }
  })
}

export function getList(data) {
  return request({
    url: '/sys/user',
    method: 'get',
    params: data
  })
}

export function add(data) {
  return request({
    url: '/sys/user',
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}

export function del(data) {
  return request({
    url: '/sys/user',
    method: 'delete',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}

export function update(data) {
  return request({
    url: '/sys/user',
    method: 'put',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    data: JSON.stringify(data)
  })
}
