<template>
  <div>
    <div style="margin-top:5px;position:relative;border: 1px solid #d8dfe6;
      border-radius: 4px;">
      <el-table
        v-loading="loadingShow"
        element-loading-text="拼命加载中"
        ref="singleTable"
        :height="tableHeight+'px'"
        :data="tableData"
        size="small"
        tooltip-effect="dark"
        border
        :highlight-current-row="true"
        :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
        style="width: 100%;border-radius: 4px;border: 0px solid #C0CCDA;">
        <el-table-column
          prop="skuId"
          label="SKU_ID"
          :show-overflow-tooltip="true"
          align="center"
          width="100">
        </el-table-column>
        <el-table-column
          prop="skuName"
          label="商品名称"
          :show-overflow-tooltip="true"
          align="center"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="cityName"
          label="城市"
          :show-overflow-tooltip="true"
          align="center"
          width="200">
        </el-table-column>
        <el-table-column
          prop="warehouseName"
          label="仓库"
          :show-overflow-tooltip="true"
          align="center"
          width="200">
        </el-table-column>
        <el-table-column
          prop="class1Name"
          label="一级品类"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="inventoryNumber"
          label="库存数量"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
        </el-table-column>
        <el-table-column
          label="开始治理时间"
          align="center"
          :show-overflow-tooltip="true"
          min-width="150">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatDate(scope.row.governStartTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="推迟治理原因"
          align="center"
          min-width="150">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatReason(scope.row.delayReason) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="预计治理日期"
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatDate(scope.row.governEstimateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="实际治理日期"
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatDate(scope.row.finishTime) }}</span>
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

    <!-- 延期处理 -->
    <div v-if="delayDealShow">
      <el-dialog title="延期处理" :visible.sync="delayDealShow" width="600px" :close-on-click-modal="false" @close="closeDialog">
        <el-form ref="ruleForm" :model="form" label-width="180px" :rules="rules">
          <el-form-item label="SKU_ID：">
            <el-input v-model="curItem.skuId" style="width: 250px;" disabled></el-input>
          </el-form-item>
          <el-form-item label="商品名：">
            <el-input v-model="curItem.skuName" style="width: 250px;" disabled></el-input>
          </el-form-item>
          <el-form-item label="仓库：">
            <el-input v-model="curItem.warehouseName" style="width: 250px;" disabled></el-input>
          </el-form-item>
          <el-form-item label="原因" prop="delayReason">
            <el-select v-model="form.delayReason" placeholder="请选择原因" style="width: 250px;">
              <el-option label="等待报损" value="1"></el-option>
              <el-option label="等待退供" value="2"></el-option>
              <el-option label="赠品" value="3"></el-option>
              <el-option v-show="curItem.categoryType==2" label="虚库存" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="预计治理时间" prop="governEstimateTime">
              <el-date-picker type="date" placeholder="选择日期" v-model="form.governEstimateTime"
                              style="width: 250px;"></el-date-picker>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer" style="text-align: center">
          <el-button size="small" @click="closeDialog">取 消</el-button>
          <el-button size="small" type="primary" :loading="saveButtonLoading" @click="delayDealSubmit('ruleForm')">保 存</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'

  export default {
    name: "DealResultSkuTable",
    props: {
      queryData: {
        type: Object,
        default: {}
      },
      tableHeight: {
        type: Number,
        required: true
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
        // 延期处理
        delayDealShow: false,
        curItem: "",
        // 表单
        form: {
          reason: "",
          dealDate: ""
        },
        // 校验规则
        rules: {
          delayReason: [
            {required: true, message: '请选择原因', trigger: 'change'}
          ],
          governEstimateTime: [
            {type: 'date', required: true, message: '请选择日期', trigger: 'change'}
          ]
        },
        // 保存loading
        saveButtonLoading: false
      }
    },
    watch: {},
    mounted() {

    },
    methods: {
      reflushData() {
        this.curPage = 1
        this.queryTableData()
      },
      // 格式化时间
      formatDate(time) {
        if(time==null || time == 0) {
          return "无"
        }
        return moment(time).format('YYYY-MM-DD')
      },
      // 格式化时间
      formatReason(reason) {
        if(reason==null || reason == '') {
          return "无"
        }
        if(reason==1){
          return "等待报损"
        }else if(reason==2){
          return "等待退供"
        }else if(reason==3){
          return "赠品"
        }else if(reason==4){
          return "虚库存"
        }
      },
      // 分页查询
      queryTableData() {
        this.queryData.pageNum = this.curPage
        this.queryData.numPerPage = this.pageSize
        this.queryData.status = this.dealType
        this.loadingShow = true;
        // this.$http.post("/dealresult/getTableData", this.queryData).then(res => {
        this.$http.post("/dealresult/queryList", this.queryData).then(res => {
          this.loadingShow = false;
          var resData = res.data
          var ret = resData.ret
          this.tableData = resData.data.items
          this.total = resData.data.totalCount
          this.$emit("callback", {total: this.total, type: this.dealType})
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
      // 延期处理
      delayDeal(item) {
        this.delayDealShow = true
        this.curItem = item
      },
      // 保存延期处理
      delayDealSubmit(formName) {
        this.saveButtonLoading = true
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.saveDelayConfig()
          } else {
            this.saveButtonLoading = false
            return false;
          }
        })
      },
      // 关闭dialog
      closeDialog() {
        this.delayDealShow = false
        this.form = {}
      }
    }
  }
</script>

<style scoped>
  .hrefA{
    color: #20A0FF;
    cursor: pointer;
  }
</style>