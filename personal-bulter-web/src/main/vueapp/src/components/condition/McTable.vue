<template>
  <div>
    <slot></slot>
    <!-- 分页 -->
    <div class="paginate_div box boxH" style="flex-wrap: wrap">
      <div>
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="myCurPage"
          :page-sizes="[20, 50, 100, 200, 300, 400]"
          :page-size="myPageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalcount">
        </el-pagination>
      </div>
      <div style="width:300px;margin-left: 50px;" v-show="tableScroll.rate>0">
        <el-slider v-model="progressVal" :show-tooltip="false"></el-slider>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
		name: "mc-table",
    components: { },
		props: {
      curpage: {
        type: Number,
        default: 1
      },
      pagesize: {
        type: Number,
        default: 20
      },
      totalcount: {
        type: Number,
        default: 0
      }
    },
		data() {
			return {
        // talbe滑动效果
        progressVal: 0,
        tableScroll: {
          dom: null,
          rate: 0
        }
			}
		},
    computed: {
		  myCurPage: {
		    get() {
		      return this.curpage
        },
        set(val) {
		      console.log(val)
		      this.$emit("update:curpage", val)
        }
      },
      myPageSize: {
        get() {
          return this.pagesize
        },
        set(val) {
		      console.log(val)
          this.$emit("update:pagesize", val)
        }
      }
    },
		mounted() {
      let vm = this
      // 窗口变化
      window.onresize = function windowResize() {
        vm.initTableScroll()
      }
      // 左侧菜单变化
      this.$bus.$on('menu-collapse', (val) => {
        setTimeout(()=>{
          vm.initTableScroll()
        }, 500)
      })
      // 初始化table滚动条
      this.$nextTick(()=> {
        setTimeout(()=>{
          vm.initTableScroll()
        }, 500)
      })
		},
    watch: {
      progressVal(val) {
        if(this.tableScroll.dom){
          this.tableScroll.dom.scrollTo(val * this.tableScroll.rate, 0)
        }
      }
    },
		methods: {
      // table滚动条
      initTableScroll() {
        // var tableObj = this.$refs.multipleTabletest.children[0]
        // // table body dom
        // var tableBodyWrapperDom = tableObj.bodyWrapper
        // // 真实table宽度
        // var tabelBodyWidthStyle = tableObj.bodyWidth
        // var tableBodyWidth = tabelBodyWidthStyle.substring(0,tabelBodyWidthStyle.length-2)
        // // 可见table宽度
        // var tableClientWidth = tableBodyWrapperDom.clientWidth
        var tableObj = {}
        var slotEl = this.$slots.default[0].elm
        for(var item of slotEl.children) {
          if(item.className.indexOf('el-table__body-wrapper')!=-1){
            tableObj = item
            break;
          }
        }
        // table body dom
        var tableBodyWrapperDom = tableObj
        // 真实table宽度
        var tableBodyWidth = tableObj.scrollWidth
        // 可见table宽度
        var tableClientWidth = slotEl.clientWidth
        this.tableScroll.dom = tableBodyWrapperDom
        this.tableScroll.rate = (tableBodyWidth-tableClientWidth)/100
      },
      // 改变查询页数
      handleCurrentChange(val) {
        this.myCurPage = val
        this.$emit("reload")
      },
      // 改变每页显示
      handleSizeChange(val) {
        this.myCurPage = 1
        this.myPageSize = val
        this.$emit("reload")
      },
    }
	}
</script>

<style scoped>
  .paginate_div {
    position: fixed;
    bottom: 0px; padding: 20px 0px; background: #fff;
    z-index: 999; border-top: 1px solid #eee;
    width: 100%;
  }
</style>
