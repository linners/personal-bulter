<template>
  <div style="margin-top:-10px;position:relative;border: 1px solid #d8dfe6;
    border-radius: 4px;">
    <el-table
      v-loading="loadingShow"
      element-loading-text="拼命加载中"
      height="550px"
      ref="singleTable"
      :data="tableData"
      size="small"
      tooltip-effect="dark"
      border
      :highlight-current-row="true"
      :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
      style="width: 100%;border-radius: 4px;border: 0px solid #C0CCDA;">
      <el-table-column
        prop="groupName"
        label="商品分类"
        :show-overflow-tooltip="true"
        align="center"
        width="150">
      </el-table-column>
      <el-table-column
        prop="cityName"
        label="城市"
        :show-overflow-tooltip="true"
        align="center"
        width="130">
      </el-table-column>
      <el-table-column
        label="责任人"
        :show-overflow-tooltip="true"
        align="center"
        width="260">
        <template slot-scope="scope">
          <div>
            {{scope.row.staffName}}({{scope.row.staffDept}})</br>
            {{scope.row.staffMail}}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="purchaseNames"
        label="普通城市采购"
        :show-overflow-tooltip="true"
        align="center"
        min-width="200"
        sortable>
      </el-table-column>

      <el-table-column
        fixed="right"
        align="center"
        label="操作"
        width="120">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="editConfig(scope.row)"
            type="text"
            size="small">
            编辑
          </el-button>
          <el-button
            @click.native.prevent="delConfig(scope.row)"
            type="text"
            size="small">
            删除
          </el-button>
        </template>
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
</template>

<script>
  export default {
    name: "cityUserConfig",
    props: {
      tableData: {
        type: Array,
        required: true
      },
      loadingShow:Boolean,
      errorShow:Boolean
    },
    data() {
      return {}
    },
    watch: {
      tableData: function () {
      }
    },
    methods: {
      editConfig (item) {
        this.$emit("edit-config", item)
      },
      delConfig (item) {
        this.$confirm('此操作将删除记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.delDbData(item)
        })
      },
      // 数据库删除
      delDbData(item) {
        item.status = 1
        this.$http.post("/dealconfig/delCityUser", item).then(res => {
          this.saveButtonLoading = false
          var resData = res.data
          if(resData.ret==1){
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success'
            });
            this.$emit("reflush")
          }else {
            this.$notify({
              title: '警告',
              message: resData.error,
              type: 'warning'
            });
          }
        }).catch(res => {
          this.saveButtonLoading = false
        })
      }
    }
  }
</script>

<style scoped>

</style>