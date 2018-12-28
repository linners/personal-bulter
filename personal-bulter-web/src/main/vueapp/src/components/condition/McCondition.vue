<template>
  <div class="search-condition">
    <div class="condition-header">
      <span>查询条件</span>
    </div>
    <div class="box boxH" style="margin-bottom: -15px; padding: 25px 15px">
      <div class="flex1 box boxH" style="flex-wrap: wrap" ref="mycondtions">
        <slot></slot>
      </div>
      <div class="btnDiv box boxV">
        <div style="margin-bottom: 20px; justify-content: space-between" class="box boxH">
          <el-button size="mini" type="primary" @click="clickSearch">查询</el-button>
          <el-button size="mini" type="warning" plain @click="resetSearch">重置</el-button>
        </div>
        <div>
          <slot name="botton">
          </slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
		name: "mc-condition",
    components: { },
		props: {

    },
		data() {
			return {

			}
		},
		mounted() {
		  var vm = this
      this.setItemRent(document.body.clientWidth)
      window.onresize = function windowResize() {
        vm.setItemRent(window.innerWidth)
      }
		},
    computed: {

    },
		methods: {
		  // 动态item宽度计算
      setItemRent(innerWidth) {
        var conditionItem = this.$refs.mycondtions.children
        var mianWidth = innerWidth - 200 -20 -15 -150
        var count = mianWidth /300
        count = count>1?Math.floor(count): 1
        if(count > conditionItem.length){
          count = conditionItem.length
        }
        var widthRent = Math.floor(100/count-1.5)+'%'
        // var widthRent = Math.floor(mianWidth / count) + "px"
        for(var item of conditionItem){
          item.style.width = widthRent
        }
      },
      // 查询按钮
      clickSearch() {
        this.$emit("search")
      },
      // 重置查询条件
      resetSearch() {
        this.$emit("reset")
      }
    }
	}
</script>

<style scoped>
  .search-condition{
    border: 1px solid #ddd;
    border-radius: 3px;
  }
  .condition-header{
    padding: 10px 15px;
    background: #f3f4f5;
    border-bottom: 1px solid #ddd;
    font-size: 14px;
    font-weight: bold;
    color: #716e6e;
  }
  .btnDiv{
    width: 150px;
    margin-bottom: 10px;
    padding-bottom: 15px;
    align-items: center;
  }
</style>
