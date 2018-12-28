<template>
  <div>
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>有库存未上线</el-breadcrumb-item>
      <el-breadcrumb-item>商品治理</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="ss-content">
      <el-tabs v-model="activeName" type="card" style="font-size: 12px;">
        <el-tab-pane :label="'待治理商品列表（'+waitDealTotal+'）'" name="first">
          <deal-sku-table v-if="activeName=='first'" :table-height="contentHeight" :deal-type="2" @callback="flushTotal"></deal-sku-table>
        </el-tab-pane>
        <el-tab-pane :label="'延期治理商品（'+delayDealTotal+'）'" name="second">
          <deal-sku-table v-if="activeName=='second'" :table-height="contentHeight" :deal-type="3" @callback="flushTotal"></deal-sku-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import Lodash from 'lodash'
  import DealSkuTable from "./DealSkuTable"

  export default {
    name: "CityDeal",
    components: {DealSkuTable},
    data() {
      return {
        activeName: 'first',
        contentHeight: 0,
        waitDealTotal: 0,     // 等待治理个数
        delayDealTotal: 0     // 延期治理个数
      }
    },
    watch: {},
    mounted() {
      this.getHeight()
      const that = this
      // _.debounce 是一个通过 lodash 限制操作频率的函数。
      window.onresize = _.debounce(that.getHeight, 100)
    },
    methods: {
      getHeight(){
        this.$nextTick(() => {
          this.contentHeight = window.innerHeight - 238
        })
      },
      flushTotal(item) {
        if(item.type == 2){
          this.waitDealTotal = item.total
          this.delayDealTotal = item.extend
        }else {
          this.waitDealTotal = item.extend
          this.delayDealTotal = item.total
        }
      }
    }
  }
</script>

<style>
  .el-tabs__item {
    font-size: 12px;
    font-weight: bold;
  }
</style>