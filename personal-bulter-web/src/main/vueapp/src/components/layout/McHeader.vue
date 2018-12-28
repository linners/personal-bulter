<template>
	<div class="header box boxH">
		<div class="header-logo box boxH">
			<img class="log" src="@/assets/img/logo.png"/>
			<img class="spector"
			     src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxzdmcgd2lkdGg9IjRweCIgaGVpZ2h0PSI2NHB4IiB2aWV3Qm94PSIwIDAgNCA2NCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4NCiAgICA8IS0tIEdlbmVyYXRvcjogU2tldGNoIDQxLjIgKDM1Mzk3KSAtIGh0dHA6Ly93d3cuYm9oZW1pYW5jb2RpbmcuY29tL3NrZXRjaCAtLT4NCiAgICA8dGl0bGU+bGluZTwvdGl0bGU+DQogICAgPGRlc2M+Q3JlYXRlZCB3aXRoIFNrZXRjaC48L2Rlc2M+DQogICAgPGRlZnM+PC9kZWZzPg0KICAgIDxnIGlkPSJQYWdlLTEiIHN0cm9rZT0ibm9uZSIgc3Ryb2tlLXdpZHRoPSIxIiBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIG9wYWNpdHk9IjAuNSI+DQogICAgICAgIDxnIGlkPSLlupTnlKjmpoLop4giIHRyYW5zZm9ybT0idHJhbnNsYXRlKC0xMjguMDAwMDAwLCAtNDAuMDAwMDAwKSI+DQogICAgICAgICAgICA8ZyBpZD0ibG9nbyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoNDAuMDAwMDAwLCAzMC4wMDAwMDApIj4NCiAgICAgICAgICAgICAgICA8ZyBpZD0ibGluZSIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoODguMDAwMDAwLCAxMC4wMDAwMDApIj4NCiAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTIsMCBMMiwyMCIgaWQ9IkxpbmUiIHN0cm9rZT0iI0ZGRkZGRiIgc3Ryb2tlLXdpZHRoPSIyIj48L3BhdGg+DQogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik0yLDQ0IEwyLDY0IiBpZD0iTGluZS1Db3B5IiBzdHJva2U9IiNGRkZGRkYiIHN0cm9rZS13aWR0aD0iMiI+PC9wYXRoPg0KICAgICAgICAgICAgICAgICAgICA8Y2lyY2xlIGlkPSJPdmFsIiBmaWxsPSIjRkZGRkZGIiBjeD0iMiIgY3k9IjMyIiByPSIyIj48L2NpcmNsZT4NCiAgICAgICAgICAgICAgICA8L2c+DQogICAgICAgICAgICA8L2c+DQogICAgICAgIDwvZz4NCiAgICA8L2c+DQo8L3N2Zz4="/>
			<div class="header-logo-title">
				<span>个人管家平台</span>
			</div>
		</div>
		<div class="flex1 box boxH">
			<div class="header-items" @click="changeMenu(1)">
				<a :href="pcpDomain" class="svg-icon-a" :class="curMenuIndex==1?'active':''">
					<i class="el-icon-share"></i>
					<span>商品监控</span></a>
			</div>
			<div class="header-items" @click="changeMenu(2)"
					 v-has="permission(['CityDealApi','DealConfigApi','DealdashboardApi','DealResultApi','LowerVolumeApi','RateOfPinApi','StockofflineApi','ZombieApi'])">
				<a href="/#/ssuStockOffline" class="svg-icon-a" :class="curMenuIndex==2?'active':''">
					<i class="el-icon-share"></i>
					<span>动销治理</span></a>
			</div>
			<div class="header-items" @click="changeMenu(3)"  v-has="permission(['KanbanApi','SkuReplaceApi'])">
				<a href="/businessboard.html#/kanban" class="svg-icon-a" :class="curMenuIndex==3?'active':''">
					<i class="el-icon-share"></i>
					<span>经营看板</span></a>
			</div>

		</div>
		<div class="header-aside box boxH">
			<div id="userInfo" style="margin-right:20px;">欢迎&nbsp;&nbsp;{{userName}}（{{roleName}}）</div>
			<div><a @click="logout"><span>退出</span></a></div>
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
		name: "mc-header",
		components: {},
		props: ['userInfo'],
		mixins: [myMixin],
		data() {
			return {
				userName: '',
				roleName: '',
        pcpDomain: '',			// 老版商品指挥平台域名
				curMenuIndex: 2
			}
		},
		mounted() {
			var route_path = this.$route.path
			if(route_path == '/kanban' || route_path == '/frushkanban' || route_path == '/skureplace') {
			  this.curMenuIndex = 3
			}else {
			  this.curMenuIndex = 2
			}
		  console.log(this.curMenuIndex)
      localStorage.setItem("menuIndex", this.curMenuIndex)
		},
		watch: {
			userInfo:function () {
				if (this.userInfo){
					this.userName = this.userInfo.userName;
					this.roleName = this.userInfo.roleName;
					this.pcpDomain = this.userInfo.pcpDomain
				}
			}
		},
		methods: {
			// 转换菜单
			changeMenu(menuIndex) {
			  this.curMenuIndex = menuIndex
				localStorage.setItem("menuIndex", this.curMenuIndex)
			},

			// 退出
			logout() {
        this.$confirm('确定要退出系统吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          window.location.href = "goToLogoutUrl"
        }).catch(() => {
					console.log("发生异常")
        });
			}
		}
	}
</script>

<style scoped>
	.header {
		width: 100%;
		height: 100%;
		background-color: #2b83f9;
		background-image: linear-gradient(143deg, #424f63 20%, #424f63 81%, #424f63);
	}
	.header-logo {
		min-width: 200px;
		height: 100%;
		align-items: center;
		background: #424f63;
	}
	.header-logo .log {
		height: 27px;
		margin-left: 5px;
	}
	.header-logo .spector {
		height: 27px;
		margin-left: 10px;
	}
	.header-logo > .header-logo-line {
		margin-left: 0
	}
	.header-logo-title {
		margin: 0 auto;
	}
	.header-logo span {
		color: #f2f2f2;
		font-size: 17px;
	}
	.header-logo:hover {
		cursor: pointer
	}
	.header-items {
		width: 115px;
		border-left: 1px solid #495972;
	}
	.header-items a {
		text-decoration: none;
		height: 100%;
		display: flex;
		-webkit-box-align: center;
		align-items: center;
		font-size: 14px;
		color: #fff;
		opacity: .69;
		cursor: pointer;
		transition: all .2s ease-in-out;
	}
	.header-items a i{
		margin: 0px 10px;
	}
	.header-items a.active {
		background: #353f4e;
		opacity: 1
	}
	.header-items a.on, .header-items a:hover {
		color: #fff;
		opacity: 1
	}
	.header-aside {
		align-items: center;
		margin-right: 15px;
		color: #fff;
		font-size: 14px
	}
	.header-aside a {
		color: #fff;
		opacity: .69;
		cursor: pointer;
		text-decoration: none;
		transition: all .2s ease-in-out
	}
	.header-aside a.active {
		background: #353f4e;
		opacity: 1
	}
	.header-aside a:hover {
		color: #fff;
		opacity: 1
	}
</style>
