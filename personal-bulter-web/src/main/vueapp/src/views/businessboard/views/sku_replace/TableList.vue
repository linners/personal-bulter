<template>
  <div style="margin-bottom: 62px;">
    <div class="box boxH" style="margin-bottom: 10px;">
      <el-button size="mini" type="primary" plain @click="batchSkuReplace" style="margin-right: 10px;" :disabled="multipleSelection.length==0">批量标注</el-button>
      <el-upload class="upload-demo"
        :show-file-list="false"
        action="/skureplace/upload"
        :on-exceed="handleExceed"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <el-button size="mini" type="warning" plain>excel导入</el-button>
      </el-upload>
    </div>

    <div class="alert-msg"><i class="el-icon-info" style="color: #3fb1e3; margin-right: 8px;"></i><span v-html="alertTitle"></span></div>

    <!-- 列表页面 -->
    <mc-table :curpage.sync="queryData.curPage" :pagesize.sync=queryData.pageSize :totalcount="totalCount" @reload="reloadData">
      <el-table
        v-loading="loadingShow"
        element-loading-text="拼命加载中"
        :data="tableData"
        tooltip-effect="dark"
        :highlight-current-row="true"
        :header-cell-style="{'background-color': '#f3f4f5',textAlign:'center'}"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          align="center"
          width="35">
        </el-table-column>
        <el-table-column
          label="skuID"
          align="center"
          width="100">
          <template slot-scope="scope">{{ scope.row.skuId }}</template>
        </el-table-column>
        <el-table-column
          prop="skuName"
          label="sku名称"
          align="center"
          show-overflow-tooltip
          width="120">
        </el-table-column>
        <el-table-column
          prop="cityName"
          label="城市"
          align="center"
          show-overflow-tooltip
          width="80">
        </el-table-column>
        <el-table-column
          prop="warehouseName"
          label="仓库"
          align="center"
          show-overflow-tooltip
          width="100">
        </el-table-column>
        <el-table-column
          prop="class1Name"
          label="采销一级分类"
          align="center"
          show-overflow-tooltip
          width="120">
        </el-table-column>
        <el-table-column
          prop="class2Name"
          label="采销二级分类"
          width="120"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="class3Name"
          label="采销三级分类"
          width="120"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          label="是否按库存售卖"
          width="120"
          align="center"
          show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.isStockSale }}</template>
        </el-table-column>
        <el-table-column
          prop="thirtySaleVolumeAvg"
          label="30日平均销量"
          width="120"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="replaceStatus"
          label="状态"
          width="80"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="replaceProgress"
          label="状态进展"
          width="120"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="ssuIsOnline"
          label="商品上下线状态"
          width="120"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="suggestDesc"
          label="建议"
          align="center"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          fixed="right"
          align="center"
          label="操作"
          width="140">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="editConfig(scope.row)"
              type="text"
              size="small">
              更改状态
            </el-button>
            <el-button
              @click.native.prevent="showLog(scope.row)"
              type="text"
              size="small">
              操作日志
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </mc-table>

    <!-- 更换状态页面 -->
    <el-dialog title="更改状态" :visible.sync="showReplaceDialog" :close-on-click-modal="false" @close="showReplaceDialog=false" width="600px">
      <sku-replace-mark :items="multipleSelection" v-if="showReplaceDialog" @close="closeMark" @success="reloadData"></sku-replace-mark>
    </el-dialog>

    <!-- 查看操作日志 -->
    <el-dialog title="操作日志" :visible.sync="showReplaceLogDialog" @close="showReplaceLogDialog=false">
      <sku-replace-log :sku-id="curItem.skuId" v-if="showReplaceLogDialog"></sku-replace-log>
    </el-dialog>
  </div>
</template>

<script>
  import SkuReplaceMark from './SkuReplaceMark'
  import SkuReplaceLog from './SkuReplaceLog'
  import McTable from "@/components/condition/McTable";
  export default {
    name: "table-list",
    components: {McTable, SkuReplaceMark, SkuReplaceLog },
    props: {

    },
    data() {
      return {
        queryData: {
          curPage: 1,            // 当前查询页数
          pageSize: 20           // 每页展示数
        },
        loadingShow: false,      // 加载中
        totalCount: 0,
        tableData: [],
        showReplaceDialog: false,   // 状态变更页面
        multipleSelection: [],
        // 操作日志界面
        curItem: {},
        showReplaceLogDialog: false
      }
    },
    computed: {
      alertTitle() {
        return "<span style='font-size: 12px'>已选择 <span style='color: red; padding: 0px 5px;'>" + this.multipleSelection.length + "</span>个</span>"
      }
    },
    mounted(){

    },
    methods: {
      // 重新加载数据
      reloadData() {
        this.loadingShow = true
        this.$http.post("/skureplace/pageList", this.queryData).then(res=>{
          this.loadingShow = false
          let resData = res.data
          if(resData.ret==1) {
            this.totalCount = resData.data.total
            this.tableData = resData.data.list
          }
        }).catch(res=>{
          this.loadingShow = false
          console.log(res)
        })
      },
      // 选择要标注的sku
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      // 批量标记汰换状态
      batchSkuReplace() {
        this.showReplaceDialog = true
      },
      // 关闭标记dialog
      closeMark() {
        this.showReplaceDialog = false
        this.reloadData()
      },
      // 展示操作日志
      showLog(item) {
        this.curItem = item
        this.showReplaceLogDialog = true
      },
      // 变更状态
      editConfig(item) {
        this.showReplaceDialog = true
        this.multipleSelection=[item]
      },
      // 文件上传
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      // 文件上传
      handleAvatarSuccess(res, file) {
        this.$message.success("上传成功!")
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      // 文件上传
      beforeAvatarUpload(file) {
        const isCsv = file.type === 'application/vnd.ms-excel';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isCsv) {
          this.$message.error('上传文件只能是 csv 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传文件大小不能超过 2MB!');
        }
        return isCsv && isLt2M;
      }
    }
  }
</script>

<style scoped>
  .paginate_div {
    position: fixed;
    bottom: 0px; padding: 20px 0px; background: #fff;
    z-index: 999; border-top: 1px solid #eee;
    width: 100%;
  }
  .alert-msg {
    padding: 8px 10px;
    margin-bottom: 10px;
    background: #edf3f9;
    border-radius: 3px;
  }
</style>