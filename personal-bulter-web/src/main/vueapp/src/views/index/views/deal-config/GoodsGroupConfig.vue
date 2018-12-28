<template>
    <div>
        <div class="coll-h4" style="margin-bottom: 20px">商品分类配置</div>
        <el-form :inline="true" :model="queryData" class="demo-form-inline">
            <el-form-item>
                <!--<el-input size="small" clearable v-model="queryData.staffMail" placeholder="员工邮箱"></el-input>-->
                <el-input v-model="queryData.key"  style="width: 300px;" size="small"
                           placeholder="请输入商品分组名称、二级分类名称" >
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button size="small" type="primary" icon="el-icon-search" plain @click="onSubmit">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button size="small" type="success" icon="el-icon-search" plain @click="newGoodsGroupView">新增</el-button>
            </el-form-item>
        </el-form>

        <!-- 列表展示 -->
        <div style="padding-bottom: 10px;">
            <el-table element-loading-text="拼命加载中"
                    :data="tableData"
                    stripe
                    border
                    :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
                    style="width: 100%;overflow-x: hidden">
                <el-table-column label="商品分组信息" header-align="center">
                    <el-table-column
                            prop="groupName"
                            label="商品分组名称">
                    </el-table-column>

                    <el-table-column
                            prop="class2Names"
                            label="二级分类名称">
                    </el-table-column>
                    <!-- 操作栏 -->
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
                </el-table-column>
            </el-table>
        </div>


        <!-- 分页区域 -->
        <div>
            <el-pagination background
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="curPage"
                           :page-sizes="[10, 20, 50, 100]"
                           :page-size="pageSize"
                           layout="total, sizes, prev, pager, next"
                           :total="total">
            </el-pagination>
        </div>


        <!-- 新增商品分组信息 -->
        <el-dialog :title="title" :visible.sync="showGoodsGroupAdd" :close-on-click-modal="false" @close="showGoodsGroupAdd=false" width="700px">
           <NewGoodsGroup v-if="showGoodsGroupAdd" :all-class2s="allClass2s" :editable="editable" :goods-group-info="goodsGroupInfo" @reflush="onSubmit" @close="showGoodsGroupAdd=false"></NewGoodsGroup>
        </el-dialog>
    </div>
</template>

<script>
  import NewGoodsGroup from './NewGoodsGroup'

  export default {
    name: "GoodsGroupConfig",
    components: {NewGoodsGroup},
      data() {
      return {
          queryData: {
              key: "" //商品分组名称 或二级分类名称
          },
          title: "新增商品分类配置",
          allClass2s: [], //所有的二级分类
          curPage: 1,
          pageSize: 10,
          total: 0,
          tableData: [],
          loadingShow: false,  //加载动画开关
          errorShow: false,   //错误提示开关
          curGoodsGroupInfo: {},
          // 新建窗口
          showGoodsGroupAdd: false,
          editable: false,
          // 当前选中行对象
          goodsGroupInfo:{}
      }
    },
    watch: {
      tableData: function () {
      }
    },
   mounted() {
          this.initContent();
      },
    methods: {
        initContent(){
            this.queryTableData();
            this.searchAllClass2s();
        },

        // 查询所有的二级采销分类
        searchAllClass2s () {
            this.$http.post("/common/searchAllClass2s", {}).then(res => {
                this.allClass2s = res.data.data
            }).catch(err => {
                console.log(err);
            })
        },
        // 查询按钮
        onSubmit() {
            this.curPage = 1
            this.queryTableData()
        },
        // 分页查询
        queryTableData() {
            this.queryData.pageNum = this.curPage
            this.queryData.numPerPage = this.pageSize
            this.loadingShow = true;
            this.$http.post("/goodsGroup/list", this.queryData).then(res => {
                this.loadingShow = false;
                var resData = res.data
                var ret = resData.ret
                this.tableData = resData.data.items
                this.total = resData.data.totalCount
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

        //----------------------------旧功能-----------------------------
        // 新增商品分组信息
        newGoodsGroupView() {
            this.goodsGroupInfo = null
            this.showGoodsGroupAdd = true
            this.editable = false
            this.title = "新增商品分类配置"
        },
      editConfig (item) {
          this.goodsGroupInfo = item;
          this.showGoodsGroupAdd = true
          this.editable = true
          this.title = "修改商品分类配置"
          console.log("edit------"+this.goodsGroupInfo)
          //this.$emit("edit-config", item)
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
        this.$http.post("/goodsGroup/delGroup", item).then(res => {
          this.saveButtonLoading = false
          var resData = res.data
          if(resData.ret==1){
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success'
            });
            this.onSubmit()
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
    .coll-h4 {
        margin: 20px 0px 0 0;
        color: #1F2D3D;
        font-size: 14px;
        font-family: 'PingFangSC-Medium', sans-serif;
        font-weight: bold;
    }
</style>