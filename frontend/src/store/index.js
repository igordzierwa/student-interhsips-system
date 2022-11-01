import { createStore } from 'vuex'

export default createStore({
  state: {
    userRole: 0,
    userData: {},
    session: {
      token: localStorage.getItem('swops-bearer-token') || ''
    }
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
