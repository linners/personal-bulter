/**
 * auth相关工具类
 * @param menu_name
 */
import allApi from '../api/AllApi'

export default {
  // 获得某个菜单拥有的所有权限集合
  getPermissionsByMenu(menu_name, method_name) {
    var permissions = []
    if(menu_name && method_name) {        // 数组
      if(Array.isArray(menu_name)){
        for(var menuname of menu_name) {
          var menuPermissionObj = allApi[menuname]
          if(menuPermissionObj){
            permissions.push(menuPermissionObj[method_name].p)
          }
        }
      }else {                              // 具体菜单
        var menuPermissionObj = allApi[menu_name]
        if(menuPermissionObj) {
          permissions.push(menuPermissionObj[method_name].p)
        }
      }
    }else if(menu_name) {
      if(Array.isArray(menu_name)){        // 数组
        for(var menuname of menu_name) {
          var menuPermissionObj = allApi[menuname]
          if(menuPermissionObj) {
            for(var method in menuPermissionObj) {
              permissions.push(menuPermissionObj[method].p)
            }
          }
        }
      }else {                              // 具体菜单
        var menuPermissionObj = allApi[menu_name]
        if(menuPermissionObj) {
          for(var method in menuPermissionObj) {
            permissions.push(menuPermissionObj[method].p)
          }
        }
      }
    }else {
      for(var menu in allApi) {
        var menuPermissionObj = allApi[menu]
        if(menuPermissionObj) {
          for (var method in menuPermissionObj) {
            permissions.push(menuPermissionObj[method].p)
          }
        }
      }
    }
    return permissions
  }
}