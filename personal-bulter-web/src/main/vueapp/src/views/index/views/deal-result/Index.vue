<template>
  <div>
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>有库存未上线</el-breadcrumb-item>
      <el-breadcrumb-item>治理结果</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="ss-content">
      <div style="text-align: center; margin-top: 10px;">
        <h5>治理商品排名</h5>
      </div>
      <div class="ss-condition">
        <div style="flex: 1; display: flex; flex-wrap:wrap">
          <div class="item">
            <div class="label">批次日期：</div>
            <el-select v-model="queryData.batchId" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allGovernBatchs"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <div class="label">一级分类：</div>
            <el-select v-model="queryData.class1Id" placeholder="全部" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allClass1s"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <div class="label">城市：</div>
            <el-select v-model="queryData.cityId" placeholder="全部" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allCitys"
                :key="item.cityId"
                :label="item.cityName"
                :value="item.cityId">
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <div class="label">城市负责人：</div>
            <el-select v-model="queryData.staffId" placeholder="全部" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allCityUsers"
                :key="item.staffId"
                :label="item.staffName"
                :value="item.staffId">
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <div class="label">展示数据：</div>
            <el-select v-model="queryData.type" placeholder="全部" size="mini" multiple>
              <el-option label="治理商品总数" :value="1"></el-option>
              <el-option label="未治理商品数" :value="2"></el-option>
              <el-option label="已治理商品数" :value="3"></el-option>
              <el-option label="延期治理商品数" :value="4"></el-option>
            </el-select>
          </div>
          <div class="item">
            <div class="label">分组维度：</div>
            <el-select v-model="queryData.groupType" placeholder="全部" size="mini">
              <el-option label="城市" :value="1"></el-option>
              <el-option label="一级分类" :value="2"></el-option>
              <el-option label="负责人" :value="3"></el-option>
              <el-option label="期数" :value="4"></el-option>
            </el-select>
          </div>
        </div>
        <div class="search-div">
          <el-button size="mini" type="primary" @click="searchChartData">查询</el-button>
        </div>
      </div>
      <div class="line_chart" style="border: 0px;">
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
        <h5>治理商品信息</h5>
      </div>
      <div class="ss-condition">
        <div style="flex: 1; display: flex; flex-wrap:wrap">
          <div class="item1">
            <div class="label">治理期数：</div>
            <el-select v-model="queryData1.batchId" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allGovernBatchs"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </div>
          <div class="item1">
            <div class="label">一级分类：</div>
            <el-select v-model="queryData1.class1Id" placeholder="请选择" size="mini" multiple clearable filterable>
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
            <el-select v-model="queryData1.cityId" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allCitys"
                :key="item.cityId"
                :label="item.cityName"
                :value="item.cityId">
              </el-option>
            </el-select>
          </div>
          <div class="item1">
            <div class="label">城市负责人：</div>
            <el-select v-model="queryData1.staffId" placeholder="请选择" size="mini" multiple clearable filterable>
              <el-option
                v-for="item in allCityUsers"
                :key="item.staffId"
                :label="item.staffName"
                :value="item.staffId">
              </el-option>
            </el-select>
          </div>
        </div>
        <div class="search-div">
          <el-button size="mini" type="primary" @click="searchTableData">查询</el-button>
          <el-button size="mini" type="primary" @click="exportData">导出</el-button>
        </div>
      </div>
      <div style="padding: 0px 12px;">
        <sku-list-table ref="table_data_ref" :table-height="contentHeight" :query-data="queryData1" :deal-type="3"></sku-list-table>
      </div>
    </div>
  </div>
</template>

<script>
  import EchartShow from './EchartShow'
  import SkuListTable from './SkuListTable'
  import moment from 'moment'

  export default {
    name: "DealResult",
    components: {EchartShow, SkuListTable},
    data() {
      return {
        queryData: {
          batchId: [],
          class1Id: [],
          cityId: [],
          staffId: [],
          type: [1,2,3,4],
          groupType: 1
        },
        queryData1: {
          batchId: [],
          class1Id: [],
          cityId: [],
          staffId: [],
        },
        // 所有的治理期数
        allGovernBatchs: [],
        // 所有的一级分类
        allClass1s: [],
        // 所有的城市
        allCitys: [],
        // 所有的城市端负责人
        allCityUsers: [],
        // chart loadding
        loading1: false,
        // chart option
        chartOption: null,
        echartErrorShow: true,

        // table
        contentHeight: 400
      }
    },
    watch: {},
    mounted() {
      // init
      this.initMethod()
    },
    methods: {
      initMethod() {
        this.searchAllBatchIds()
        this.searchAllClass1s()
        this.searchAllCitys()
        this.searchAllCityUsers()
      },
      // 查询所有的治理期数d
      searchAllBatchIds(){
        this.allGovernBatchs = []
        this.$http.post("/dealconfig/searchAllBatchs", {}).then(res => {
          let resData = res.data.data
          for (var i =0;i<resData.length;i++) {
              var batchId = moment(resData[i].batchBeginTime).format("YYYY-MM-DD")
              var batchname = moment(resData[i].batchBeginTime).format("YYYYMMDD")
              this.allGovernBatchs.push({
                  id: batchId,
                  name: batchname + "期"
              })
          }
          this.queryData.batchId.push(moment(resData[0].batchBeginTime).format("YYYY-MM-DD"))
          this.queryData1.batchId.push(moment(resData[0].batchBeginTime).format("YYYY-MM-DD"))
            this.searchChartData()
            this.searchTableData()
        }).catch(err => {
          console.log(err);
        })
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
      // 查询所有的城市端负责人
      searchAllCityUsers(){
        this.$http.post("/dealconfig/searchAllCityUsers", {}).then(res => {
          this.allCityUsers = res.data.data
        }).catch(err => {
          console.log(err);
        })
      },
      // 查询图表
      searchChartData() {
        var option = {
          legend: {
            data:[]
          },
          //x轴显示
          xAxis: {
            data: []
          },
          //y轴显示
          yAxis: {
            name: '数量',
          },
          series:  []
        }
        this.loading1 = true
        this.$http.post("/dealresult/getChartData", this.queryData).then(res => {
          this.loading1 = false
          var resData = res.data.data

          if(res.data.ret == 1){
              this.$notify({
                  title: '成功',
                  message: '加载成功',
                  type: 'success'
              });
              this.$emit("reflush")
          } else {
              this.$notify({
                  title: '失败！',
                  message: res.data.error,
                  type: 'warning'
              });
              this.echartErrorShow = true
          }
          if(resData && resData.contents && resData.contents.length>0){
            this.echartErrorShow = false
              option.series  = resData.contents
          }else{
            this.echartErrorShow = true
          }
          if (resData && resData.legend && resData.legend.length>0){
              option.legend = resData.legend
          }
          if(resData && resData.xAxis && resData.xAxis.length>0){
            this.echartErrorShow = false
            option.xAxis.data = resData.xAxis
          }else{
            this.echartErrorShow = true
          }

          this.chartOption = option
        }).catch(res=>{

        })
      },
      // 查询table数据
      searchTableData() {
        this.$refs.table_data_ref.reflushData()
      },
      // 导出数据
      exportData: function () {
          var vm = this;
          this.$confirm('您确定要导出吗?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              window.location.href="/dealresult/exportGovernResult?cityId="+vm.queryData1.cityId+
                  '&class1Id='+ vm.queryData1.class1Id+
                  '&staffId='+ vm.queryData1.staffId+
                  '&batchId='+ vm.queryData1.batchId ;
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
    width: 100px;
    display: flex;
    justify-content: center;
    align-items: flex-end;
    padding: 10px
  }

</style>