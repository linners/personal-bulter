<template>
  <div>
    <div style="margin-top:5px;position:relative;border: 1px solid #d8dfe6;border-radius: 4px;">
      <el-table
        v-loading="loadingShow"
        show-summary
        border
        element-loading-text="拼命加载中"
        ref="singleTable"
        :data="tableData"
        size="small"
        tooltip-effect="dark"
        :highlight-current-row="true"
        :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
        style="width: 100%;border-radius: 4px;border: 0px solid #C0CCDA;">
        <el-table-column
          prop="class1_name"
          fixed="left"
          label="一级分类"
          :show-overflow-tooltip="true"
          align="center"
          width="100">
        </el-table-column>
        <el-table-column
          prop="target_1"
          sortable
          label="上线商品数"
          align="center"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="target_2"
          sortable
          label="7天有库存未上线"
          align="center"
          width="200">
          <template slot-scope="scope">
            <div class="hrefA" @click="showSkuDetail(2, scope.row)">{{ scope.row.target_2 }}</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="target_3"
          sortable
          label="上线库0商品数"
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <div class="hrefA" @click="showSkuDetail(3, scope.row)">{{ scope.row.target_3 }}</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="target_4"
          sortable
          label="7天不动销"
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <div class="hrefA" @click="showSkuDetail(4, scope.row)">{{ scope.row.target_4 }}</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="target_5"
          label="畅缺商品数"
          sortable
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <div class="hrefA" @click="showSkuDetail(5, scope.row)">{{ scope.row.target_5 }}</div>
          </template>
        </el-table-column>
        <el-table-column
          label="商品数合计"
          :show-overflow-tooltip="true"
          align="center"
          min-width="200">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatTotal(scope.row) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="经营水平"
          align="center"
          min-width="150">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ formatLevel(scope.row) }}</span>
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

    <!-- 商品详情查看 -->
    <el-dialog :title="detailTitle" :visible.sync="skuDetailShow" width="70%" :close="skuDetailShow">
      <kanban-sku-detail-table v-if="skuDetailShow" :target="curDetailTarget" :class1id="curDetailclass1Id"></kanban-sku-detail-table>
    </el-dialog>
  </div>
</template>

<script>
  import moment from 'moment'
  import kanbanSkuDetailTable from './KanbanSkuDetailTable'

  export default {
    name: "KanbanSkuTable",
    components: {kanbanSkuDetailTable},
    props: {
      tableHeight: {
        type: Number,
        default: 300
      },
      categoryType: {
        type: Number,
        default: 1
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
        queryData: {},
        // detail
        skuDetailShow: false,
        curDetailTarget: 0,
        curDetailclass1Id: 0,
        curDetailclass1Name: ""
      }
    },
    computed: {
      detailTitle() {
        if(this.curDetailTarget==2){
          return this.curDetailclass1Name + " - 7天有库存未上线 - SKU详情"
        }else if(this.curDetailTarget==3) {
          return this.curDetailclass1Name + " - 上线库存为0 - SKU详情"
        }else if(this.curDetailTarget==4) {
          return this.curDetailclass1Name + " - 7天不动销 - SKU详情"
        }else if(this.curDetailTarget==5) {
          return this.curDetailclass1Name + " - 畅缺商品 - SKU详情"
        }
        return "SKU详情"
      }
    },
    watch: {
      categoryType(val) {
        this.queryTableData()
      }
    },
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
      // 格式化商品总计
      formatTotal(item) {
        return item.target_1 + item.target_2
      },
      // 格式化经营水平
      formatLevel(item) {
        var total = item.target_1 + item.target_2
        var tmp = item.target_1 - item.target_3 - item.target_4 - item.target_5
        return ((tmp/total)*100).toFixed(2)
      },
      // 分页查询
      queryTableData() {
        this.queryData.pageNum = this.curPage
        this.queryData.numPerPage = this.pageSize
        this.queryData.categoryType = this.categoryType
        this.loadingShow = true;
        this.$http.post("/kanban/getKanBanAllYes", this.queryData).then(res => {
          this.loadingShow = false;
          var resData = res.data
          var ret = resData.ret
          this.tableData = resData.data
        }).catch(err => {
          this.loadingShow = false;
          console.log(err);
        })
      },
      // 查看详情
      showSkuDetail(target_type, item){
        this.curDetailTarget = target_type
        this.curDetailclass1Id = item.class1_id
        this.curDetailclass1Name = item.class1_name
        this.skuDetailShow = true
      }
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