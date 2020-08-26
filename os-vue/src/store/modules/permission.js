import { asyncRoutes, constantRoutes } from '@/router'
import Layout from '@/layout'
import { tree } from "@/api/menu"

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

// 将本地routerMap映射到ajax获取到的serverRouterMap;
function generateAsyncRouter(serverRouterMap) {
  serverRouterMap.forEach(function (item, index) {
    if (item.type === 0) {
      item.component = Layout
    } else {
      item.component = require('@/views' + item.path).default
    }
    if (item.children && item.children.length > 0) {
      generateAsyncRouter(item.children)
    }
  })
  return serverRouterMap;
}


/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {

    return new Promise(resolve => {

      tree().then(response => {
        let asyncRouterMap = generateAsyncRouter(response.data)
        commit('SET_ROUTES', asyncRouterMap)
        resolve(asyncRouterMap)
      })

      /* let accessedRoutes
     if (roles.includes('admin')) {
       accessedRoutes = asyncRoutes || []
     } else {
       accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
     }
     commit('SET_ROUTES', accessedRoutes)
     resolve(accessedRoutes)  */

    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
