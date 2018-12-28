<template>
  <div class="mc-app box boxV">
    <div id="wm" ref="wrapper"></div>
    <div class="mc-header">
      <mc-header :user-info="userInfo"></mc-header>
    </div>
    <div class="mc-container flex1 box boxH">
      <div class="mc-navigate">
        <mc-navigation v-cloak :user-info="userInfo"></mc-navigation>
      </div>
      <div class="mc-main flex1" ref="mcMain">
        <transition name="el-fade-in-linear">
          <router-view></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
  import McHeader from './McHeader'
  import McNavigation from './McNavigation'

  export default {
    name: "mc-layout",
    components: { McHeader, McNavigation },
    data() {
      return {
        userInfo: null
      }
    },
    mounted() {
      // 获得人员信息
      this.getUserInfo()

    },
    methods: {
      getUserInfo() {
        this.$http.post('/common/getUserInfo', {}).then(res => {
          var resData = res.data.data.userInfo;
          var pcpDomain = res.data.data.pcpDomain;
          localStorage.setItem("pcpDomain", pcpDomain);
          localStorage.setItem("userInfo", resData);
          // 添加水印
          var userinfo = {}
          userinfo.userName = resData.user.name
          userinfo.roleName = resData.role.name
          userinfo.pcpDomain = pcpDomain
          this.userInfo = userinfo
          __punch.to(
          	this.$refs.wrapper,
            resData.user.name + resData.user.id,  //此处替换成当前用户+用户id
          	{
          		width: 250,	//水印单元宽度
          		height: 80,	//水印单元高度
          		bg: 'transparent',	//水印背景
          		bgAlpha: 1,		//水印背景透明度
          		color: '#dedede',	//水印字体颜色
          		alpha: 0.4,	//水印字体颜色透明度
          		font: '16px Arial'	//水印字体
          	}
          )
        }).catch(err => {
          console.error(err);
        })
      },
    }
  }
</script>

<style scoped>
  .mc-app {
    height: 100%;
    width: 100%;
  }
  .mc-header {
    height: 50px;
    z-index: 1000;
  }
  .mc-container {
    height: 100%;
    overflow-y: hidden;
  }
  .mc-navigate {
    z-index: 1000;
    height: 100%;
  }
  .mc-main {
    padding: 10px;
    overflow: auto;
    text-align: left;
  }
  #wm {
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 99;
    pointer-events: none;
  }
  .content {
    position: relative;
    height: 100%;
    padding: 10px;
    text-align: left;
  }
  .content-out-visit {
    position: relative;
    margin-left: 0px;
    margin-top: 0px;
    height: 100%;
    padding: 10px;
    text-align: left;
  }
</style>
