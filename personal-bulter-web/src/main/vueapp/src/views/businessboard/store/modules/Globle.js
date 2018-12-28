
const state = {
  permissions: [],
  userInfo: null
}

const mutations = {
  INIT_PERMISSIONS (state, permissions) {
    state.permissions = permissions
  }
}

const actions = {
  initPermissions({ commit }, permissions) {
    commit('INIT_PERMISSIONS', permissions)
  }
}

export default {
  state,
  mutations,
  actions
}
