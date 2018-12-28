<template>
  <div>
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>商品汰换</el-breadcrumb-item>
      <el-breadcrumb-item>商品汰换</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="split"></div>

    <!-- 查询条件 -->
      <mc-condition @search="searchList" @reset="reset">
        <mc-select label="选择城市" :options="allCitys" :value.sync="queryData.cityId" @change="cityChange"></mc-select>
        <mc-select label="仓库名称" :options="allHouses" :value.sync="queryData.houseId"></mc-select>
        <mc-cascader label="采销分类" :options="allCategorys" :value.sync="queryData.classId"></mc-cascader>
        <mc-select label="商品状态" :options="allSsuStatus" :value.sync="queryData.ssuStatus"></mc-select>
      </mc-condition>
    <!--</el-card>-->

    <!-- table列表 -->
    <table-list style="margin-top: 10px;" ref="tableList"></table-list>
  </div>
</template>

<script>
  import McCondition from '@/components/condition/McCondition'
  import McSelect from '@/components/condition/McSelect'
  import McInput from '@/components/condition/McInput'
  import McCascader from '@/components/condition/McCascader'
  import TableList from './TableList'
  export default {
    name: 'SkuReplaceVO',
    components: { McCondition, McSelect, McInput, McCascader,TableList },
    data() {
      return {
        queryData: {
          cityId: "",
          houseId: "",
          classId: [],
          ssuStatus: ""
        },
        // 所有的城市
        allCitys: [],
        // 城市下的所有仓库
        allHouses: [],
        // 所有的采销分类
        allCategorys: [],
        // 所有商品状态
        allSsuStatus: [
          {id: 1, name: "正常商品"},
          {id: 2, name: "汰换中"}
        ]
      }
    },
    mounted() {
      // 查询所有的城市
      this.searchAllCity()
      // 查询所有的采销分类
      this.searchAllCategoryInfo()


      this.searchList()
    },
    methods: {
      // 重置条件
      reset() {
        this.queryData = {}
      },
      // 查询列表
      searchList(){
        console.log(this.queryData)
        this.$refs.tableList.reloadData()
      },
      // 查询所有的城市
      searchAllCity: function () {
        this.allCitys = []
        this.$http.post("/common/searchAllCitys", {}).then(res => {
          var allCity = res.data.data
          for(var city of allCity) {
            this.allCitys.push({id: city.cityId, name: city.cityName})
          }
        });
      },
      // 城市变化事件
      cityChange(cityId) {
        this.$http.post("/common/searchHouseByCityId", {cityId: cityId}).then(res=>{
          var resData = res.data
          if(resData && resData.ret == 1) {
            this.allHouses = resData.data
            this.queryData.houseId = ""
          }
        })
      },
      // 查询所有的采销分类
      searchAllCategoryInfo() {
        this.$http.post("/common/searchAllCategoryInfo", {}).then(res=>{
          var resData = res.data
          if(resData && resData.ret == 1) {
            this.allCategorys = resData.data
          }
        })
      }
    }
  }
</script>

<style scoped>
  .myflex {
    width: 30%;
  }
</style>