<template>
  <mc-layout id="app" v-if="showLayout"></mc-layout>
</template>

<script>
  import Vue from 'vue';
  import McLayout from "@/components/layout/McLayout";

  export default {
    name: 'app',
    components: {McLayout},
    data() {
      return {
        outVisit: false,
        showLayout: false
      }
    },
    created() {
      let _this = this;
      if (localStorage.getItem("out_visit") == "true") {
        this.outVisit = true;
      } else {
        this.outVisit = false
      }
      // 获得所有的权限
      this.getListPermissions()
    },
    methods: {
      inArrays(url, list) {
        for (var item of list) {
          if (url.trim() == item.trim()) {
            return true
          }
        }
        return false
      },
      getListPermissions() {
        var vm = this
        this.$http.post('/auth/listPermissions', {}).then(res => {
          var resData = res.data
          if (resData.ret == 1) {
            var permissions = resData.data
            this.$store.dispatch("initPermissions", permissions)
            this.showLayout = true
            // 实现自定义指令v-has逻辑, permission_url是like this: '/a/b' 或 '/a/b, /a/c'
            Vue.prototype.$_has = function (permissionUrls) {
              for(var url of permissionUrls) {
                if(vm.inArrays(url, permissions)){
                  return true
                }
              }
              return false
            }
          } else {
            console.log(resData.error)
          }
        }).catch(err => {
          console.error(err);
        })
      }
    }
  }
</script>

<style>
  @import "../../assets/common.css";
  .kanban .el-form-item__label{
    color: #b5b2b2!important;
    font-weight: 700!important;
  }
  .kanban .el-form-item--mini.el-form-item{
    margin-bottom: 12px;
  }
  .kanban .el-checkbox__label{
    font-size: 12px;
  }
  .kanban .el-radio__label{
    font-size: 12px;
  }
  .kanban .el-form-item__label{
    font-size: 12px;
  }
  .kanban .el-radio-button--mini .el-radio-button__inner{
    padding: 5px 13px;
  }

</style>
