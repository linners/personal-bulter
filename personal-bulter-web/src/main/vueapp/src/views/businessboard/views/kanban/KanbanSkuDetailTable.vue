<template>
  <div>
    <div style="margin-top:5px;position:relative;border: 1px solid #d8dfe6;
      border-radius: 4px;">
      <el-table
        v-loading="loadingShow"
        element-loading-text="拼命加载中"
        ref="singleTable"
        :height="500"
        :data="tableData"
        size="small"
        tooltip-effect="dark"
        border
        :highlight-current-row="true"
        :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
        style="width: 100%;border-radius: 4px;border: 0px">
        <el-table-column
          prop="skuId"
          label="SKU_ID"
          align="center"
          width="100">
        </el-table-column>
        <el-table-column
          prop="skuName"
          label="sku名称"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="cityName"
          label="城市名称"
          align="center"
          width="200">
        </el-table-column>
        <el-table-column
          prop="warehouseName"
          label="仓库名称"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="class1Name"
          label="一级分类"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="class2Name"
          label="二级分类"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==2 || target==4"
          prop="class3Name"
          label="三级分类"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="bigAreaName"
          label="大区名称"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==2"
          prop="inventoryNumber"
          label="库存量"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==2"
          prop="minOnlineTime"
          label="首次上线日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==2"
          prop="maxOnlineTime"
          label="最后上线日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==2"
          prop="maxOfflineTime"
          label="最后下线日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==3 || target==5"
          prop="splitNumber"
          label="分拣量"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==3 || target==5"
          prop="intimePrice"
          label="实时成本价"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==3 || target==5"
          prop="orderArrDate"
          label="最近订单到货日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==3 || target==5"
          prop="ownerName"
          label="货主名称"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==4"
          prop="averageSaleNumPer30days"
          label="30天平均销量"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==4"
          prop="amount"
          label="可用库存量"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==4"
          prop="enterWarehouseDate"
          label="最后入库日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==4"
          prop="goodsdt"
          label="第一次上线时间"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          v-if="target==4"
          prop="maxpmsdate"
          label="最后采购下单日期"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
      </el-table>
      <div class="el-loading-mask" v-show="errorShow">
        <div class="el-loading-spinner">
          <p class="el-loading-text" style="color: #e68555;">
            未查询到相关数据
          </p>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div style="margin-top: 5px;">
      <el-pagination background
         @size-change="handleSizeChange"
         @current-change="handleCurrentChange"
         :current-page="curPage"
         :page-sizes="[15, 25, 50, 100]"
         :page-size="pageSize"
         layout="total, sizes, prev, pager, next"
         :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    name: "kanban-sku-detail-table",
    props: {
      target: {
        type: Number,
        default: 0
      },
      class1id: {
        type: Number,
        default: 0
      }
    },
    data() {
      return {
        curPage: 1,
        pageSize: 15,
        total: 0,
        tableData: [],
        loadingShow: false,
        errorShow: false,
        queryData: {}
      }
    },
    watch: {},
    mounted() {
      this.queryTableData()
    },
    methods: {
      // 格式化时间
      formatDate(time) {
        if(time==null || time == 0) {
          return "无"
        }
        return moment(time).format('YYYY-MM-DD')
      },
      // 分页查询
      queryTableData() {
        this.queryData.curPage = this.curPage
        this.queryData.pageSize = this.pageSize
        this.queryData.target = this.target
        this.queryData.classList = [this.class1id]
        this.loadingShow = true;
        this.$http.post("/kanban/getKanbanDetail", this.queryData).then(res => {
          this.loadingShow = false;
          var resData = res.data
          if(resData.ret == 1) {
            var ret = resData.ret
            this.tableData = resData.data.list
            this.total = resData.data.total
          }
        }).catch(err => {
          this.loadingShow = false;
          console.log(err);
        })
      },
      // pageSize变化
      handleSizeChange(val) {
        this.curPage = 1
        this.pageSize = val
        this.queryTableData()
      },
      // 当前页数变化
      handleCurrentChange(val) {
        this.curPage = val
        this.queryTableData()
      },
    }
  }
</script>

<style scoped>
  .hrefA{
    color: #20A0FF;
    cursor: pointer;
    width: 100%;
  }
</style>