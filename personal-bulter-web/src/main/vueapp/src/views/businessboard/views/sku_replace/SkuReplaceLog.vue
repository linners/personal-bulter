<template>
  <div>
    <el-table
      element-loading-text="拼命加载中"
      :data="tableData"
      tooltip-effect="dark"
      :highlight-current-row="true"
      :header-cell-style="{'background-color': '#f3f4f5',textAlign:'center'}"
      style="width: 100%"
      border>
      <el-table-column
        prop="skuId"
        label="skuID"
        align="center"
        width="100">
      </el-table-column>
      <el-table-column
        prop="skuName"
        label="sku名称"
        align="center"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        prop="beforeStatus"
        label="变更前状态"
        align="center"
        show-overflow-tooltip
        width="120">
      </el-table-column>
      <el-table-column
        prop="afterStatus"
        label="变更后状态"
        align="center"
        show-overflow-tooltip
        width="120">
      </el-table-column>
      <el-table-column
        prop="operateTime"
        label="操作时间"
        align="center"
        show-overflow-tooltip
        width="170">
      </el-table-column>
      <el-table-column
        prop="userName"
        label="操作人"
        align="center"
        show-overflow-tooltip
        width="120">
      </el-table-column>
    </el-table>
    <div style="margin-top: 12px;">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryData.curPage"
        :page-sizes="[20, 50, 100, 200, 300, 400]"
        :page-size="queryData.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="totalCount">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'sku-replace-log',
    props: {
      skuId: {
        type: Number,
        required: true
      }
    },
    data() {
      return {
        queryData: {
          curPage: 1,
          pageSize: 20
        },
        tableData: [],
        totalCount: 0
      }
    },
    mounted() {
      this.searchList()
    },
    methods: {
      // 查询列表
      searchList() {
        console.log(this.skuId)
        this.tableData = [
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" },
          { skuId: 1231, skuName: "黄心土豆(普通|斤)", beforeStatus: 1, afterStatus: 2, operateTime: "2018-12-13 21:22:23", userName: "王成林" }
        ]
        this.totalCount = 20
      },
    }
  }
</script>

<style>
  .skureplace .form-footer {
    text-align: right;
    margin-top: 40px;
  }
  .confirmClass {
    vertical-align: top;
    margin-top: 180px;
  }
</style>