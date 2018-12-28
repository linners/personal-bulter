<template>
  <div>
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>有库存未上线</el-breadcrumb-item>
      <el-breadcrumb-item>数据概览</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="ss-content">
      <div style="text-align: center; margin-top: 10px;">
        <h5>有库存未上线商品数量排名</h5>
      </div>
      <div class="ss-condition">
        <div style="flex: 1; display: flex; flex-wrap:wrap">
          <div class="item">
            <div class="label">排名维度：</div>
            <el-select v-model="sortType" placeholder="请选择" size="mini">
              <el-option label="城市" :value="1"></el-option>
              <el-option label="一级分类" :value="2"></el-option>
            </el-select>
          </div>
        </div>
        <div class="search-div">
          <el-button type="text" @click="showStockOfflineSkuDetail">查看商品明细</el-button>
        </div>
      </div>
      <div id="zhu_chart" style="position: relative;">
        <EchartShow v-loading="loading1"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgb(250, 250, 250)"
                    :option="chartOption"></EchartShow>
        <div v-show="echartErrorShow" class="errorMask">暂无数据</div>
      </div>
    </div>

    <div class="ss-content">
      <div style="text-align: center; margin-top: 10px;">
        <h5>有库存未上线商品数量变化趋势</h5>
      </div>
      <div class="ss-condition">
        <div style="flex: 1; display: flex; flex-wrap:wrap">
          <div class="item1">
            <div class="label">日期：</div>
            <el-date-picker
              size="mini"
              type="daterange"
              value-format="yyyy-MM-dd"
              v-model="queryData.date"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </div>
          <div class="item1">
            <div class="label">一级分类：</div>
            <el-select v-model="queryData.class1Id" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allClass1s"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </div>
          <div class="item1">
            <div class="label">城市：</div>
            <el-select v-model="queryData.cityId" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allCitys"
                :key="item.cityId"
                :label="item.cityName"
                :value="item.cityId">
              </el-option>
            </el-select>
          </div>
        </div>
        <div class="search-div">
          <el-button size="mini" type="primary" @click="generateChartOption2">查询</el-button>
        </div>
      </div>
      <div class="line_chart" style="border: 0px;position: relative">
        <EchartShow  v-loading="loading2"
                     element-loading-text="拼命加载中"
                     element-loading-spinner="el-icon-loading"
                     element-loading-background="rgb(250, 250, 250)"
                     :option="chartOption2"></EchartShow>
        <div v-show="echartErrorShow1" class="errorMask">暂无数据</div>
      </div>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'
  import EchartShow from './EchartShow'

  export default {
    name: "DealResult",
    components: {EchartShow},
    data() {
      return {
        queryData: {
          date: [],       // 日期
          class1Id: [],    // 一级分类
          cityId: []      // 城市
        },
        // 排名维度, 1:城市  2：一级分类
        sortType: 1,
        // 所有的一级分类
        allClass1s: [],
        // 所有的城市
        allCitys: [],
        options: [
          {label: "a", value: "b"}
        ],
        echartErrorShow: true,
        echartErrorShow1: true,
        loading1: false,
        chartOption: null,      // 柱状图
        loading2: false,
        chartOption2: null,     // 折线图

        // table
        contentHeight: 400
      }
    },
    watch: {
      sortType(type) {
        this.generateChartOption()
      }
    },
    mounted() {
      // 查询所有的一级分类
      this.searchAllClass1s()
      // 查询所有的城市
      this.searchAllCitys()

      // 初始化
      this.initMethod()
      this.$nextTick(() => {
        // 查询第一个图表
        this.chartOption = this.generateChartOption()
      })
      this.$nextTick(() => {
        // 查询第二个图表
        this.chartOption2 = this.generateChartOption2()
      })
    },
    methods: {
      // 初始化信息
      initMethod() {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
        const begintime = moment(start).format("YYYY-MM-DD")
        const endtime = moment(end).format("YYYY-MM-DD")
        this.queryData.date.push(begintime)
        this.queryData.date.push(endtime)
      },
      // 查询所有的一级采销分类
      searchAllClass1s () {
        this.$http.post("/common/searchAllClass1s", {}).then(res => {
          this.allClass1s = res.data.data
        }).catch(err => {
          console.log(err);
        })
      },
      // 查询所有的城市
      searchAllCitys () {
        this.$http.post("/common/searchAllCitys", {}).then(res => {
          this.allCitys = res.data.data
        }).catch(err => {
          console.log(err);
        })
      },

      // 组装第一个图表
      generateChartOption() {
        var option = {
          legend: {
            data:['']
          },
          //x轴显示
          xAxis: {
            data: []
          },
          //y轴显示
          yAxis: {
            type: 'value',
            splitLine: {
              show: true,
              lineStyle: {
                color: '#E8E8E8',
              },
            },
            name: '未治理商品数',
          },
          series: [
            {
              type: "bar",
              name:"未治理商品数",
              data: [],
              barWidth : 10,
              //显示颜色
              itemStyle:{
                normal:{color:"#45A9FE"}
              }
            }
          ]
        }
        this.loading1 = true
        let vm = this
        this.$http.post("/dealboard/getStockOfflineCountSort", {type: this.sortType}).then(res => {
          this.loading1 = false
          var resData = res.data.data
          if(resData && resData.xAxis){
            option.xAxis.data = resData.xAxis
          }
          if(resData && resData.data && resData.data.length>0){
            // this.echartErrorShow = false
            option.series[0].data = resData.data
          }else{
            // this.echartErrorShow = true
          }
          this.echartErrorShow = true
          this.chartOption = option
        }).catch(res=>{

        })
      },
      // 组装第二个图表
      generateChartOption2() {
        var option = {
          legend: {
            data:['']
          },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {
            type: 'value',
            name: '未治理商品数',
            splitLine: {
              show: true,
              lineStyle: {
                color: '#E8E8E8',
              },
            },
          },
          series: [{
            data: [],
            name: "未治理商品数",
            type: 'line'
          }]
        }
        var param = {
          class1Id: [],
          cityId: [],
          beginTime: "",
          endTime: ""
        }
        param.class1Id = this.queryData.class1Id
        param.cityId = this.queryData.cityId
        param.beginTime = this.queryData.date?this.queryData.date[0]: ""
        param.endTime = this.queryData.date?this.queryData.date[1]:""
        this.loading2 = true
        this.$http.post("/dealboard/getTrendOfChange", param).then(res => {
        this.loading2 = false
          var resData = res.data.data
          if(resData && resData.xAxis){
            this.echartErrorShow1 = false
            option.xAxis.data = resData.xAxis
          }else{
            this.echartErrorShow1 = true
          }
          if(resData && resData.data){
            option.series[0].data = resData.data
          }
          this.chartOption2 = option
        }).catch(res=>{

        })
      },

      // 查询商品详情
      showStockOfflineSkuDetail(){
        // var domain = localStorage.getItem("pcpDomain")
        // var url = domain + "/#/ssuStockOffline"
        // window.open(url)
        let routeData = this.$router.resolve({
          name: "SsuStockOffline"
        });
        window.open(routeData.href, '_blank');
      }
    }
  }
</script>

<style scoped>
  .ss-content {
    border: 1px solid #ddd;
    min-height: 300px;
    margin-bottom: 30px;
    border-radius: 4px;
    justify-content: space-between;
  }

  .ss-condition {
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    margin-top: 5px;
    margin-left: -5px;
  }

  .item {
    display: flex;
    align-items: center;
    flex: 1;
    min-width: 220px;
    margin: 5px 20px;
  }
  .item1 {
    display: flex;
    align-items: center;
    margin: 5px 20px;

  }
  .item .label{
    font-size: 13px;
    min-width: 80px;
    text-align: right;
  }
  .item1 .label{
    font-size: 13px;
    min-width: 80px;
    text-align: right;
  }

  .search-div {
    width: 150px;
    display: flex;
    justify-content: center;
    align-items: flex-end;
    padding: 10px
  }

</style>