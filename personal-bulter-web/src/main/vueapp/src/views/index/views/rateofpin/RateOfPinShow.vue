<template>
  <div id="RateOfPinShow">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>商品监控</el-breadcrumb-item>
      <el-breadcrumb-item>动销率看板查询</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="split"></div>
    <main>
      <!-- 查询条件 -->
      <el-row :gutter="20" style="position: relative;">
        <el-col :span="21">
          <el-row :gutter="20">
            <el-col :span="7">
              <span class="search-label_2">选择一级采销分类：</span>
              <treeselect
                v-model="class1Id"
                :multiple="true"
                :options="pms_c1s"
                :default-expand-level="1"
                :show-count="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="暂无数据"
                placeholder="一级品类"/>
            </el-col>
            <el-col :span="7">
              <span class="search-label_2">选择二级采销分类：</span>
              <treeselect
                v-model="class2Id"
                :multiple="true"
                :options="pms_c2s"
                :show-count="true"
                :disable-branch-nodes="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="请选择一级品类"
                placeholder="二级品类"/>
            </el-col>
            <el-col :span="7">
              <span class="search-label_2">选择三级采销分类：</span>
              <treeselect
                v-model="class3Id"
                :multiple="true"
                :options="pms_c3s"
                :show-count="true"
                :disable-branch-nodes="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="请选择二级品类"
                placeholder="三级品类"/>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="7">
              <span class="search-label_2">时间维度：</span>
              <treeselect
                v-model="queryData.computePeriod"
                :multiple="true"
                :options="weidus"
                :show-count="true"
                :clearable="false"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="暂无数据"
                placeholder="选择时间维度"/>
            </el-col>
            <el-col :span="7">
              <span class="search-label_2">查询城市：</span>
              <el-select v-model="queryData.cityId" placeholder="请选择" size="medium" filterable style="width: 100%;">
                <el-option
                  size="small"
                  v-for="item in citys"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <span class="search-label_2">选择时间：</span>
              <div>
                <el-date-picker
                  style="width: 100%;"
                  size="small"
                  value-format="yyyy-MM-dd"
                  v-model="dateTime"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
                </el-date-picker>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="1">
          <el-button type="primary" icon="el-icon-search" size="medium" @click="searchList" :loading="searchBtnLoading" style="position: absolute; right: 10px; bottom: 50%;">查询</el-button>
        </el-col>
      </el-row>

      <!-- 折线图展示 -->
      <div class="line_chart">
        <EchartShow v-loading="loadDataing"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgb(250, 250, 250)"
                    :option="chartOptionChart">
        </EchartShow>
        <div v-show="echartShow" class="errorMask">暂无数据</div>
      </div>

      <div style="float: right;margin-top: 15px;">
        <el-button size="mini" @click="exportData">导出列表</el-button>
      </div>

      <!-- 列表展示 -->
      <div style="padding-top: 20px;">
        <el-table
          v-loading="loadDataing" element-loading-text="拼命加载中"
          :data="tableData"
          stripe
          border
          style="width: 100%; min-height: 400px">
          <el-table-column label="动销率统计表格" header-align="center">
            <el-table-column
              prop="dt"
              label="日期"
              width="160">
            </el-table-column>
            <el-table-column
              prop="computePeriod1"
              label="1天动销率">
            </el-table-column>
            <el-table-column
              prop="computePeriod7"
              label="7天动销率">
            </el-table-column>
            <el-table-column
              prop="computePeriod14"
              label="14天动销率">
            </el-table-column>
            <el-table-column
              prop="computePeriod28"
              label="28天动销率">
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <div style="padding-top: 20px; float:right;">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryData.pageNum"
          :page-sizes="[20, 50, 100, 200, 300, 400]"
          :page-size="queryData.numPerPage"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount">
        </el-pagination>
      </div>
    </main>
  </div>
</template>

<script>
// import the component
import Treeselect from '@riophae/vue-treeselect'
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import EchartShow from './EchartShow'

// //引入echarts所需组件
// let echarts = require('echarts/lib/echarts')
// // 引入柱状图组件
// require('echarts/lib/chart/bar')
// // 引入提示框和title组件
// require('echarts/lib/component/tooltip')
// require('echarts/lib/component/title')


export default {
  name: 'RateOfPinShow',
	props: [],
	data() {
		return {
      queryData: {
        cityId: 1,
        computePeriod: [4],            // 查询维度
        pageNum: 1,                  // 当前查询页数
        numPerPage: 20                // 每页展示数
      },
      cityId: [13],
			citys: [],            // 所有城市
      weidus: [
        {id: 1, label: "1天动销率"},
        {id: 2, label: "7天动销率"},
        {id: 3, label: "14天动销率"},
        {id: 4, label: "28天动销率"}
      ],
      class1Id: [],
      class2Id: [],
      class3Id: [],
      allPmsClass: [],      // 所有的采销分类
      pms_c1s: [],          // 所有的一级采销分类
      pms_c2s: [],          // 所有的二级采销分类
      pms_c3s: [],          // 所有的三级采销分类
      loadDataing: false,     // table加载数据
      tableData: [],
      totalCount: 0,
      dateTime: "",       // 时间
      // 图表
      chartOptionChart: null,
      echartShow: false,
      // xAxis : [] , //x轴参数
      // contents : [], //内容
      // yMin : 0, // y轴坐标其实值

      searchBtnLoading: false,     //loading状态
      //限制日期框选择范围
       pickerOptions:{
        disabledDate:(time) => {
             if (this.endTime != "" && this.endTime != null) {
               let three = 90 * 24 * 3600 * 1000;
                return time.getTime() > Date.now()
                       || time.getTime() < 1533052800000 //最小时间：2018-08-01
                       || time.getTime() > this.endTime  //不能大于结束时间
                       || (this.endTime - time.getTime()) > three ; // 差值不能>3个月
            } else {
                return time.getTime() > Date.now() || time.getTime() < 1533052800000;  //最小时间：2018-08-01
            }
        }
      },
      pickerOptions0:{
        disabledDate:(time) => {
            if (this.beginTime != "") {
              let three = 90 * 24 * 3600 * 1000;
              return time.getTime() > Date.now()
                || time.getTime() < 1533052800000 //最小时间：2018-08-01
                || time.getTime() < this.beginTime  //不能小于开始时间
                || (time.getTime() - this.beginTime) > three ; // 差值不能>3个月
            } else {
              return time.getTime() > Date.now() || time.getTime() < 1533052800000
            }
        }
      },
		}
	},
	created(){
    //初始化select 城市默认选择值
    // 查询所有的城市
    this.getAllCity()
    // 查询所有的采销分类
    this.getPmsClass()
    // 查询列表数据
    this.searchList()
	},
	computed: {
    beginTime() {
      return this.dateTime&&this.dateTime.length==2?this.dateTime[0]:""
    },
    endTime() {
      return this.dateTime&&this.dateTime.length==2?this.dateTime[1]:""
    }
  },
	watch: {
    "class1Id": function (val) {
      this.class2Id = []
      this.pms_c2s = []
      var cl1_ids = []
      if(val && val.length==1 && val[0]==0){
        // 查询所有的一级分类
        var cl1s = this.getClassByParentIds(0, 1)
        for(var i=0; i<cl1s.length; i++){
          cl1_ids.push(cl1s[i].id)
        }
      }else {
        cl1_ids = val
      }
      // 组装二级分类
      this.pms_c2s = this.setCategory(cl1_ids, 2)
    },
    "class2Id": function (val) {
      this.class3Id = []
      this.pms_c3s = []
      // 组装三级分类
      this.pms_c3s = this.setCategory(val, 3)
    },
  },
	mounted(){

  },
	methods: {
        // 查询所有的城市
        getAllCity: function () {
            var vm = this
            this.$http.post("/common/searchPermissionCitys", {}).then(res => {
                var data = res.data.data
                var children = []
                for(var i=0; i<data.length; i++){
                children.push({id: data[i].cityId, label: data[i].cityName})
            }
            vm.citys  = children
        });
        },
        //请求采销分类数据
        getPmsClass() {
            this.$http.post("/common/searchAllClass", {}).then(res => {
            this.allPmsClass = res.data.data;
            this.pms_c1s = this.setCategory([0], 1);
        });
        },
        setCategory(parent_ids, level){
            var vm = this
            // 根据Ids,查询分类
            var classArr = this.getClassByIds(parent_ids)
            // 根据parentIds，查询分类
            for(var i=0; i<classArr.length; i++) {
                classArr[i].children = this.getClassByParentIds(classArr[i].id, level)
            }
            return classArr
        },
    // 根据id,查询分类
    getClassByIds: function (ids) {
      var idsArr = []
      if(ids && ids.length==1 && ids[0]==0){
        idsArr.push({id: 0, label: '全部一级分类'})
      }else {
        for(var i=0; i<ids.length; i++){
          for(var j=0; j<this.allPmsClass.length; j++) {
            if(this.allPmsClass[j].id == ids[i] && this.allPmsClass[j].name.indexOf("美家优享")==-1){
              idsArr.push({id: this.allPmsClass[j].id, label: this.allPmsClass[j].name})
              break
            }
          }
        }
      }
      return idsArr;
    },
    // 根据parentIds和level，查询分类
    getClassByParentIds: function (parentId, level) {
      var classArr = []
      this.allPmsClass.map((item, index)=>{
        if(item.level == level && parentId == item.parentId && item.name.indexOf("美家优享")==-1) {
          classArr.push({id: item.id, label: item.name})
        }
      })
      return classArr
    },
    // 分页查询
    searchList: function () {
      this.searchBtnLoading = true;
      this.queryData.pageNum = 1
      this.searchListData()
    },
    // 改变每页显示
    handleSizeChange(val) {
      this.queryData.pageNum = 1
      this.queryData.numPerPage = val
      this.searchListData()
    },
    // 查询后台数据
    searchListData: function(){
      var option = {
        legend: {
          data:['1天动销率(%)','7天动销率(%)','14天动销率(%)','28天动销率(%)']
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          name: '动销率',
          splitLine: {
            show: true,
            lineStyle: {
              color: '#E8E8E8',
            },
          },
          axisLabel: {
            show: true,
            interval: 'auto',
            formatter: '{value} %'
          },
          min : 0
        },
        series: []
      }
      this.loadDataing = true
      this.queryData.class1Id = this.class1Id
      this.queryData.class2Id = this.class2Id
      this.queryData.class3Id = this.class3Id
      this.queryData.beginTime = this.beginTime
      this.queryData.endTime = this.endTime
      this.$http.post("/rateOfPin/listRateOfPinPage", this.queryData).then(res => {
        this.tableData = res.data.data.list
        this.totalCount = res.data.data.total
        if(res.data.data.xAxis!=null && res.data.data.xAxis){
          option.xAxis.data = res.data.data.xAxis
        }else{
          this.echartShow = true
        }
        option.series = res.data.data.contents
        option.yAxis.min = res.data.data.yMin
        this.chartOptionChart = option
        this.loadDataing = false
        this.searchBtnLoading = false
        this.$message({
          type: 'success',
          message: '加载成功!'
        });
        // this.drawLine();
      }).catch(err=>{
        this.tableData = []
        this.loadDataing = false
      });
    },
    // 改变查询页数
    handleCurrentChange(val){
      this.queryData.pageNum = val
      this.searchListData()
    },
    // 导出数据
    exportData: function() {
      var vm = this;
      this.$confirm('您确定要导出吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          var param = {
              cityId:vm.queryData.cityId,
              computePeriod:vm.queryData.computePeriod,
              class1Id:vm.class1Id,
              class2Id:vm.class2Id,
              class3Id:vm.class3Id,
              beginTime: vm.beginTime,
              endTime:vm.endTime,
              pageNum:vm.queryData.pageNum
          };
          vm.$http.download("/rateOfPin/exportRateOfPin", param, "动销率看板数据导出.csv").then(function () {
              vm.$notify.success({
                  title: '消息',
                  message: '导出成功！'
              });
          })
        this.$notify.info({
              title: '消息',
              message: '正在导出中，请稍后。。。'
          });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },

    //huahan  画折线图
    drawLine(){
      // 基于准备好的dom，初始化echarts实例
      let myChart = echarts.init(document.getElementById('myChart'))
      // 绘制图表
      var vm = this

      // 清空缓存
      myChart.clear();

      myChart.setOption({
        title: { text: '动销率看板' },
        tooltip: {
           trigger: 'axis'
         },
        legend: {
          data:['1天动销率(%)','7天动销率(%)','14天动销率(%)','28天动销率(%)']
        },
        grid: {
          left: '10%',
          right: '10%',
          bottom: '5%',
          containLabel: true
        },
        toolbox: {
          feature: {
          //将图标保存为图片
          //saveAsImage: { show: true }
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: vm.xAxis
        },
        yAxis: {
            type: 'value',
           // 设置显示百分比
            axisLabel: {
              show: true,
              interval: 'auto',
              formatter: '{value} %'
            },
          min : vm.yMin
        },
        series:   vm.contents,
      });
    }

	},
	components: {
    Treeselect, EchartShow
  }
}



//格式化日期函数
Date.prototype.Format = function (fmt) { //author: huahan
  var o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "h+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}
</script>

<style scoped>
  .line_chart{
    position: relative;
    border: 1px solid #ddd;
    margin: 15px 0px;
    border-radius: 3px;
  }
  #RateOfPinShow {
    flex: 1;
    padding-bottom: 30px;
  }
  #RateOfPinShow .top-address {
    padding: 10px 20px;
    font-size: 14px;
  }
  .split {
    padding: 10px 0px;
    border-top: 1px solid #ecebeb;
  }
  main {
    display: inline-block;
    width: 100%;
    box-sizing: border-box;
  }

  main .search-label_2 {
    font-weight: bold;
    font-size: 13px;
    color: #ada7a7;
  }
  .vue-treeselect__list-item{
    padding: 15px 0px;
  }
  table thead th .t1_cls {
    background: red !important;
  }
</style>
