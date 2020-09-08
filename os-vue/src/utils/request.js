import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    // if the custom code is not 20000, it is judged as an error.
    if (!res.success) {
      let type = 'warning'
      if (res.errorCode != null) {
        type = 'error'
        console.log(res.errorMessage)
      }
      Message({
        message: res.userTips || 'Error',
        type: type,
        duration: 5 * 1000
      })
      if (res.errorCode === 'B0001') {
        let timeout = 0
        for (let i = 0; i < res.data.length; i++) {
          const errorMessage = res.data[i]

          setTimeout(() => {
            // 这里就是处理的事件
            Message({
              message: errorMessage,
              type: 'warning',
              duration: 3 * 1000
            })
          }, timeout)
          timeout = timeout + 200
        }
      }
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.errorCode === 'A0001' || res.errorCode === 'A0002' || res.errorCode === '"A0003"') {
        // to re-login
        MessageBox.confirm(res.message, '登录过期', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
