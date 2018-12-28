<template>
  <div class="nav scrollbar">
    <el-menu
      class="el-menu-vertical-demo"
      :default-active="$route.path"
      :default-openeds="menuOpeneds"
      router
      background-color="#304156"
      text-color="#fff"
      active-text-color="#409eff"
      @open="handleOpen"
      @close="handleClose"
      :collapse="isCollapse"
      style="border: 0px"
      :unique-opened="false"
    >
      <el-submenu v-for="menu in menuList" :key="menu.index" :index="menu.index" v-has="permission(menu.permission)">
        <template slot="title">
          <i :class="menu.icon"></i>
          <span>{{menu.title}}</span>
        </template>
        <el-menu-item v-for="submenu in menu.subMenu" :key="submenu.url" :index="submenu.url" v-has="permission(submenu.permission)">{{submenu.title}}</el-menu-item>
      </el-submenu>
    </el-menu>
    <div :class="isCollapse?'side-close':'side-open'" @click="collapseChange">
      <img v-if="!isCollapse" src="@/assets/img/close-icon.png" width="16"/>
      <img v-if="isCollapse" src="@/assets/img/open-icon.png" width="16"/>
    </div>
  </div>
</template>
<script>
  import AuthUtils from '@/utils/AuthUtils';

  let myMixin = {
    data: function() {
      return { permission: AuthUtils.getPermissionsByMenu }
    }
  }
  export default {
    name: "mc-navigation",
    props: ['userInfo'],
    mixins: [myMixin],
    data() {
      return {
        menuDisabled: true,
        userName: '',
        roleName: '',
        isCollapse: true,
        menuOpeneds: ['1','2','3'],
        menuList: []
      }
    },
    mounted() {
      // this.$bus.$on('menu-type', (val) => {
      //   this.menuType = val
      // })
      var curMenuIndex = localStorage.getItem("menuIndex")
      console.log(curMenuIndex)
      this.menuList = this.getMenuList(curMenuIndex)
    },
    watch: {
      userInfo: function () {
        if (this.userInfo) {
          this.userName = this.userInfo.userName;
          this.roleName = this.userInfo.roleName;
        }
      }
    },
    methods: {
      // 取得要展示的菜单
      getMenuList(curMenuIndex) {
        if(curMenuIndex==2) {
          return [
            { index: '1',
              title: '动销商品查询',
              icon: 'el-icon-goods',
              permission: ['LowerVolumeApi', 'RateOfPinApi', 'StockofflineApi','ZombieApi'],
              subMenu: [{
                title: '有库存未上线商品查询',
                permission: 'StockofflineApi',
                url: '/ssuStockOffline'
              }, {
                title: '滞销商品查询',
                permission: 'ZombieApi',
                url: '/jssjShow'
              },{
                title: '低动销商品信息查询',
                permission: 'LowerVolumeApi',
                url: '/lowerVolumeShow'
              },{
                title: '动销率看板查询',
                permission: 'RateOfPinApi',
                url: '/rateOfPinShow'
              }]
            },
            { index: '2',
              title: '有库存未上线治理',
              icon: 'el-icon-news',
              permission: ['CityDealApi','DealConfigApi','DealdashboardApi','DealResultApi'],
              subMenu: [{
                title: '数据概览',
                permission: 'DealdashboardApi',
                url: '/dealdashboard'
              }, {
                title: '治理结果',
                permission: 'DealResultApi',
                url: '/dealresult'
              },{
                title: '开启治理',
                permission: 'DealConfigApi',
                url: '/dealconfig'
              },{
                title: '治理商品',
                permission: 'CityDealApi',
                url: '/citydeal'
              }]
            }
          ]
        }else if(curMenuIndex==3) {
          return [
            { index: '1',
              title: '经营看板',
              icon: 'el-icon-picture-outline',
              permission: ['KanbanApi','FrushKanbanApi'],
              subMenu: [{
                title: '标品看板',
                permission: 'KanbanApi',
                url: '/kanban'
              }, {
                title: '生鲜看板',
                permission: 'FrushKanbanApi',
                url: '/frushkanban'
              }]
            },
            { index: '2',
              title: '商品汰换',
              icon: 'el-icon-news',
              permission: ['SkuReplaceApi'],
              subMenu: [{
                title: '商品汰换',
                permission: 'SkuReplaceApi',
                url: '/skureplace'
              }]
            }
          ]
        }
      },
      collapseChange() {
        if(this.isCollapse){  // 收缩
          this.isCollapse = false
        }else {   // 展开
          this.isCollapse = true
        }
        this.$bus.$emit('menu-collapse', this.isCollapse)
      },
      handleOpen() {
      },
      handleClose() {
      }
    }
  }
</script>

<style>
  [v-cloak] {
    display: none
  }
  .nav .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
  }
  .side-open {
    position: absolute;
    top: 50%;
    right: -2px;
    z-index: 999;
    cursor: pointer;
    margin-top: -18px;
  }
  .side-close {
    position: absolute;
    top: 50%;
    left: -1px;
    z-index: 999;
    cursor: pointer;
    margin-top: -18px;
  }
  .nav {
    position: relative;
    text-align: left;
    overflow-y: auto;
    overflow-x: hidden;
    height: 100%;
    background: #304156;
    border-right: 1px solid #eee;
  }
  .scrollbar::-webkit-scrollbar {
    width: 2px
  }
  .scrollbar::-webkit-scrollbar-track, .scrollbar::-webkit-scrollbar-track-piece {
    background-color: #fff
  }
  .scrollbar::-webkit-scrollbar-thumb {
    background-color: #d4d8e2
  }
  .scrollbar::-webkit-scrollbar-button {
    background-color: #fff;
    display: none
  }
  .nav .el-menu-item {
    font-size: 13px !important;
  }
  .nav .el-submenu .el-menu-item, #app .nest-menu .el-submenu > .el-submenu__title {
    background-color: #1f2d3d !important;
  }
  .nav .el-submenu .el-menu-item:hover, #app .nest-menu .el-submenu > .el-submenu__title:hover {
    background-color: #001528 !important;
  }
  .nav .el-menu--collapse {
    width: 35px !important;
  }
  .nav .el-submenu__title {
    padding-left: 5px !important;
  }
  .nav .el-submenu__title i {
    color: #fff !important;
  }
</style>
