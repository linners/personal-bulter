<template>
<div class="kanban">
  <el-breadcrumb separator="/" class="breadcrumb">
    <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
    <el-breadcrumb-item>生鲜看板</el-breadcrumb-item>
    <el-breadcrumb-item>标品看板</el-breadcrumb-item>
  </el-breadcrumb>

  <el-form ref="form" :model="form" label-width="70px" size="mini" class="condition">
    <el-form-item label="时间：" style="margin-bottom: 10px">
      <el-radio-group v-model="form.dataType" @change="generateChartOption">
        <el-radio-button :label="7">近7天</el-radio-button>
        <el-radio-button :label="14">近14天</el-radio-button>
        <el-radio-button :label="30">近30天</el-radio-button>
        <el-radio-button :label="60">近60天</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <!--<el-form-item label="类型：" style="margin-bottom: 10px">-->
      <!--<el-radio-group v-model="form.categoryType" @change="categoryTypeChange">-->
        <!--<el-radio :label="1">标品</el-radio>-->
        <!--<el-radio :label="2">生鲜</el-radio>-->
      <!--</el-radio-group>-->
    <!--</el-form-item>-->
    <el-form-item label="品类：" style="margin-bottom: 0px">
      <el-checkbox-group v-model="form.classList" @change="changeClassList">
        <el-radio :label="true" style="margin-right: 20px;" v-model="allCategory" @change="changeAllCategory">全部</el-radio>
        <el-checkbox size="mini" v-for="item in allClass1s" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="区域：" style="margin-bottom: 0px">
      <el-checkbox-group v-model="form.areaList"  @change="changeAreaList">
        <el-radio :label="true" style="margin-right: 20px;" v-model="allArea" @change="changeAllArea">全部</el-radio>
        <el-checkbox v-for="item in allBigAreas" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="城市：" style="margin-bottom: 0px">
      <el-checkbox-group v-model="form.cityList"  @change="changeCityList">
        <el-radio :label="true" style="margin-right: 20px;" v-model="allCity" @change="changeAllCity">全部</el-radio>
        <el-checkbox v-for="item in allCitys" :label="item.cityId" :key="item.cityId">{{item.cityName}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="指标：">
      <el-radio-group v-model="form.target" @change="generateChartOption">
        <el-radio :label="1">在线商品数</el-radio>
        <el-radio :label="2">有库存未上线商品数</el-radio>
        <el-radio :label="3">上线库存为0商品数</el-radio>
        <el-radio :label="4">7天不动销商品数</el-radio>
        <el-radio :label="5">畅缺商品数</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>

  <!-- chart -->
  <div class="line_chart">
    <EchartShow v-loading="loadingChart"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgb(250, 250, 250)"
      :option="chartOptionChart">
    </EchartShow>
    <div v-show="echartShow" class="errorMask">暂无数据</div>
  </div>

  <!-- table -->
  <div>
    <div class="table-title">{{tableTitle}}</div>
    <div style="float: right;margin-top: -25px;">
      <el-button size="mini" type="primary" plain @click="exportData">导出列表</el-button>
    </div>
    <kanban-sku-table ref="kanbanSkuTable" :category-type="form.categoryType"></kanban-sku-table>
  </div>
</div>
</template>

<script>
  import moment from 'moment'
  import KanbanSkuTable from './KanbanSkuTable'
  import EchartShow from './EchartShow'

  export default {
    name: 'FrushKanban',
    components: {EchartShow, KanbanSkuTable},
    data(){
      return {
        // 所有的一级分类
        allClass1s: [],
        // 所有的大区
        allBigAreas: [],
        // 所有城市
        allCitys: [],
        // 条件
        allCategory: true,
        allArea: true,
        allCity: true,
        form: {
          dataType: 7,
          classList: [],
          areaList: [],
          cityList: [],
          target: 1,
          categoryType: 2
        },
        // chart
        echartShow: false,
        loadingChart: false,
        chartOptionChart: null,
        // table
        tableTitle: "",
        contentHeight: 600
      }
    },

    mounted() {
      this.getTableTitle()
      this.searchAllBigAreas()
    },
    methods: {
      // 查询条件rest
      restSearch() {
        this.form.dataType = 7
        this.form.classList = []
        this.form.areaList = []
        this.form.target = 1
        this.allCategory = true
        this.allArea = true
        this.searchAllClass1s()
        this.generateChartOption()
      },
      // 获得标题
      getTableTitle(){
        var yesToday = moment(new Date()).add(-1, 'days').format('YYYY-MM-DD');
        this.tableTitle = yesToday + "经营水平概览"
      },
      // 查询所有大区
      searchAllBigAreas () {
        this.$http.post("/common/searchBigAreas", {}).then(res => {
          this.allBigAreas = res.data.data
          this.searchAllCity()
        }).catch(err => {
          console.log(err);
        })
      },
      // 查询所有的城市
      searchAllCity: function () {
        this.$http.post("/common/searchAllCitys", {}).then(res => {
          var allCity = res.data.data
          this.allCitys = allCity
          this.searchAllClass1s()
        });
      },
      // 查询所有的标品一级采销分类
      searchAllClass1s () {
        this.$http.post("/common/searchClass1sByCategoryType", {categoryType: this.form.categoryType}).then(res => {
          this.allClass1s = res.data.data
          this.generateChartOption()
        }).catch(err => {
          console.log(err);
        })
      },
      // 生鲜或标品选择发生变化
      categoryTypeChange() {
        this.restSearch()
      },
      // 所有一级分类发生变化
      changeAllCategory(newVal){
        if(newVal){
          this.form.classList = []
        }
        this.generateChartOption()
      },
      // 一级分类选择发生变化
      changeClassList(newVal) {
        if(newVal && newVal.length==0){
          this.allCategory = true
        }else{
          this.allCategory = false
        }
        this.generateChartOption()
      },
      // 所有区域发生变化
      changeAllArea(newVal){
        if(newVal){
          this.form.areaList = []
        }
        this.generateChartOption()
      },
      // 所有城市发生变化
      changeAllCity(newVal){
        if(newVal){
          this.form.cityList = []
        }
        this.generateChartOption()
      },
      // 区域发生变化
      changeAreaList(newVal) {
        if(newVal && newVal.length==0){
          this.allArea = true
        }else{
          this.allArea = false
        }
        this.generateChartOption()
      },
      // 城市发生变化
      changeCityList(newVal) {
        if(newVal && newVal.length==0){
          this.allCity = true
        }else{
          this.allCity = false
        }
        this.generateChartOption()
      },
      // 查询echart数据
      generateChartOption() {
        var option = {
          legend: {
            data:[]
          },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {
            type: 'value',
            name: '商品数',
            splitLine: {
              show: true,
              lineStyle: {
                color: '#E8E8E8',
              },
            },
          },
          series: []
        }
        var param = {
          dataType: this.form.dataType,
          target: this.form.target,
          classList: [],
          areaList: [],
          cityList: [],
          categoryType: this.form.categoryType
        }
        // 分类选择
        if(this.allCategory){
          for(var item of this.allClass1s){
            param.classList.push(item.id)
            option.legend.data.push(item.name)
          }
        }else{
          param.classList = this.form.classList
          for(var item2 of this.form.classList){
            for(var item1 of this.allClass1s){
              if(item1.id == item2){
                option.legend.data.push(item1.name)
                break
              }
            }
          }
        }
        // 大区选择
        if(this.allArea){
          for(var item of this.allBigAreas){
            param.areaList.push(item.id)
          }
        }else{
          param.areaList = this.form.areaList
        }
        // 城市选择
        if(this.allCity){
          for(var item of this.allCitys){
            param.cityList.push(item.cityId)
          }
        }else{
          param.cityList = this.form.cityList
        }
        this.loadingChart = true
        this.$http.post("/kanban/getKanBanByCondition", param).then(res=>{
        this.loadingChart = false
          var resData = res.data.data
          if(resData.xAxisData){
            option.xAxis.data = resData.xAxisData
          }
          if(resData.series && resData.series.length>0){
            this.echartShow = false
            option.series = resData.series
          }else{
            this.echartShow = true
          }
          this.chartOptionChart = option
        }).catch(res=>{

        })
      },
      // 导出数据
      exportData: function () {
        var vm = this;
        this.$confirm('您确定要导出吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var param = {}
          param.categoryType = vm.form.categoryType
          vm.$http.download("/kanban/exportFrushKanban", param, vm.tableTitle+".csv")
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
      }
    }
  }
</script>

<style scoped>


  .tit{
    padding: 8px 0px;
    border-bottom: 2px solid #c5d3e9;
  }
  .tit span{
    background: #424f63;
    padding: 10px;
    color: #fff;
  }
  .condition{
    border: 1px solid #ddd;
    border-radius: 3px;
    padding-top: 15px;
  }
  .line_chart{
    position: relative;
    border: 1px solid #ddd;
    margin: 15px 0px;
    border-radius: 3px;
  }
  .table-title{
    color: #3fb1e3;
    margin-top: 25px;
  }
  .errorMask{
    /*background: #fafafa;*/
    position: absolute;
    top: 46px;
    bottom: 65px;
    left: 108px;
    right: 94px;
    padding-top: 132px;
    text-align: center;
    color: #909399;
    font-size: 12px;
  }
</style>