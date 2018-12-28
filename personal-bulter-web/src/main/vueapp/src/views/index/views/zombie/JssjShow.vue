<template>
  <div id="JssjShow">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>商品监控</el-breadcrumb-item>
      <el-breadcrumb-item>滞销商品查询</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="split"></div>
    <main>
      <!-- 查询条件 -->
      <el-row :gutter="20" style="position: relative;">
        <el-col :span="21">
          <el-row :gutter="20">
            <el-col :span="21">
              <!--<span class="search-label_2">查询城市：</span>-->
              <treeselect
                v-model="queryData.cityId"
                :multiple="true"
                :options="citys"
                :default-expand-level="1"
                :show-count="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="暂无数据"
                placeholder="选择城市"/>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="7">
              <!--<span class="search-label_2">选择一级采销分类：</span>-->
              <treeselect
                v-model="class1Id"
                :multiple="true"
                :options="pms_c1s"
                :default-expand-level="1"
                :show-count="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="暂无数据"
                placeholder="选择一级采销分类"/>
            </el-col>
            <el-col :span="7">
              <!--<span class="search-label_2">选择二级采销分类：</span>-->
              <treeselect
                v-model="class2Id"
                :multiple="true"
                :options="pms_c2s"
                :show-count="true"
                :disable-branch-nodes="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="请选择一级采销分类"
                placeholder="选择二级采销分类"/>
            </el-col>
            <el-col :span="7">
              <!--<span class="search-label_2">选择三级采销分类：</span>-->
              <treeselect
                v-model="class3Id"
                :multiple="true"
                :options="pms_c3s"
                :show-count="true"
                :disable-branch-nodes="true"
                noResultsText="未查到相关数据"
                noChildrenText="没有子选项"
                noOptionsText="请选择二级采销分类"
                placeholder="选择三级采销分类"/>
            </el-col>
          </el-row>
          <!-- 库存，库龄 筛选 -->
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="7">
              <!--<span class="search-label_2">库存筛选：</span>-->
              <treeselect
                v-model="queryData.stockStatus"
                :multiple="true"
                :options="stockOptions"
                :show-count="true"
                :clearable="false"
                noResultsText="未查到相关数据"
                noOptionsText="暂无数据"
                placeholder="请选择库存"/>
            </el-col>
            <el-col :span="7">
              <!--<span class="search-label_2">库龄筛选：</span>-->
              <treeselect
                v-model="queryData.ageStatus"
                :multiple="true"
                :options="ageOptions"
                :show-count="true"
                :clearable="false"
                noResultsText="未查到相关数据"
                noOptionsText="暂无数据"
                placeholder="请选择库龄"/>
            </el-col>
            <el-col :span="7">
              <!--<span class="search-label_2">可用库存量：</span>-->
              <el-select v-model="queryData.stockFlag" placeholder="请选择可用库存量" size="medium" filterable style="width: 100%;">
                <el-option
                  v-for="item in weidus"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>

          <!-- 未动销天数条件 -->
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="2.5" style="margin-top: 5px;">
              <span class="search-label_2">滞销天数：</span>
              <span class="search-label_2">介于</span>
            </el-col>
            <el-col :span="2">
              <el-input v-model="queryData.computePeriodBegin" maxlength="2" size="mini"
                        @change="checkNoBegin(queryData.computePeriodBegin)">
              </el-input>
            </el-col>
            <el-col :span="1.5" style="margin-top: 5px;">
              <span class="search-label_2">天到</span>
            </el-col>
            <el-col :span="2">
              <el-input v-model="queryData.computePeriodEnd" maxlength="2" size="mini"
                        @change="checkNoEnd(queryData.computePeriodEnd)">
              </el-input>
            </el-col>
            <el-col :span="2" style="margin-top: 5px;">
              <span class="search-label_2">天之间</span>
              <el-tooltip placement="top">
                <div slot="content">
                  滞销天数的含义如下：<br/>
                  若该商品曾经在该城市售卖过，则滞销天数是指该商品最后一次在<br/>
                  该城市售卖的日期距离查询日期的天数，若该商品从未在该城市售卖<br/>
                  过，则滞销天数是指该商品第一次在该城市上线的日期距离查询日<br/>
                  期的天数。<br/>
                  滞销天数的筛选，包含所输入天数。
                </div>
                <i class="el-icon-question"></i>
              </el-tooltip>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="1">
          <el-button type="primary" icon="el-icon-search" size="medium" @click="searchList" :loading="loadDataing" style="position: absolute; right: 10px; bottom: 50%;">查询</el-button>
        </el-col>
      </el-row>

      <div class="title">
        {{title}}
        <span>
          <el-tooltip placement="top">
          <div slot="content">
            滞销商品含义是指全国主城（不包括卫星城）、<br/>
            自营（非POP、非合伙人、非大客户、非分销）商<br/>
            品中，查询时间前一天24点时处于上线状态的且查<br/>
            询日期前一天内没有售卖的商品。
          </div>
          <i class="el-icon-question" style="color: #0C1012"></i>
        </el-tooltip>
        </span>
      </div>
      <div style="float: right;margin-top: 15px;">
        <el-button size="mini" @click="exportData">导出列表</el-button>
      </div>

      <!-- 列表展示 -->
      <el-scrollbar style="height: 100%;width: 100%">
        <!--
              <div style="padding-top: 20px;padding-bottom: 20px;">
        -->
        <el-table
          v-loading="loadDataing" element-loading-text="拼命加载中"
          :data="tableData"
          stripe
          border
          style="width: 100%;">
          <el-table-column
            prop="cityId"
            label="城市ID"
            width="100">
          </el-table-column>
          <el-table-column
            prop="cityName"
            label="城市名称"
            width="100">
          </el-table-column>
          <el-table-column
            prop="skuId"
            label="skuID"
            width="100">
          </el-table-column>
          <el-table-column
            prop="skuName"
            label="sku名称"
            width="180">
          </el-table-column>
          <!--<el-table-column
            prop="class1Id"
            label="一级分类Id"
            width="90">
          </el-table-column>-->
          <el-table-column
            prop="class1Name"
            label="一级分类名称"
            width="120">
          </el-table-column>
          <!--<el-table-column
            prop="class2Id"
            label="二级分类Id"
            width="90">
          </el-table-column>-->
          <el-table-column
            prop="class2Name"
            label="二级分类名称"
            width="120">
          </el-table-column>
          <!-- <el-table-column
             prop="class3Id"
             label="三级分类Id"
             width="120">
           </el-table-column>-->
          <el-table-column
            prop="class3Name"
            label="三级分类名称"
            width="120">
          </el-table-column>
          <el-table-column
            prop="averageSaleNumPer30days"
            label="30天平均销量"
            width="120">
          </el-table-column>
          <el-table-column
            prop="warehouseName"
            label="仓库名称"
            width="120">
          </el-table-column>
          <el-table-column
            prop="amount"
            label="可用库存量"
            width="100">
          </el-table-column>
          <!--<el-table-column-->
          <!--prop="amountFormat"-->
          <!--label="可用库存量(规格)"-->
          <!--width="150">-->
          <!--</el-table-column>-->
          <el-table-column
            prop="physicalUnit"
            label="物理单位"
            width="80">
          </el-table-column>
          <el-table-column
            prop="valuationUnit"
            label="计价单位"
            width="80">
          </el-table-column>
          <el-table-column
            prop="enterWarehouseDate"
            label="最后入库时间"
            width="110">
          </el-table-column>
          <el-table-column
            prop="goodsdt"
            label="第一次上线时间"
            width="120">
          </el-table-column>
          <el-table-column
            prop="maxpmsdate"
            label="最后预采时间"
            width="120">
          </el-table-column>
          <el-table-column
            prop="lastOrderTime"
            label="最后下单时间"
            width="120">
          </el-table-column>
          <el-table-column
            prop="delayDays"
            label="滞销天数"
            width="120">
          </el-table-column>

          <el-table-column
            prop="stockLabel"
            label="库存标签"
            width="120">
          </el-table-column>

          <el-table-column
            prop="inventoryAgeDays"
            label="在库天数"
            width="120">
          </el-table-column>

          <el-table-column
            prop="inventoryAgeDaysStatusLabel"
            label="库龄状态"
            width="120">
          </el-table-column>

          <el-table-column
            prop="statusNewProductLabel"
            label="是否新品"
            width="120">
          </el-table-column>
        </el-table>
        <!--</div>-->
      </el-scrollbar>
      <!-- 分页 -->
      <div class="paginate_div">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryData.pageNum"
          :page-sizes="[20, 50, 100, 200, 300, 400]"
          :page-size="queryData.numPerPage"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount">
        </el-pagination>
      </div>
    </main>


    <!-- 传递页面标识参数给子页面 暂不显示-->
    <!--  <PcpOperateLogShow :operateMenuId=10></PcpOperateLogShow>-->

  </div>
</template>

<script>
  // import the component
  import Treeselect from '@riophae/vue-treeselect'
  // import the styles
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  //引入子页面  日志暂不显示
  /*import PcpOperateLogShow from './PcpOperateLogShow.vue';*/

  export default {
    name: 'JssjShow',
    props: [],
    data() {
      return {
        queryData: {
          cityId: [0],
          stockFlag: 0,            // 查询维度
          pageNum: 1,                  // 当前查询页数
          numPerPage: 20                // 每页展示数
        },
        cityId: [],
        citys: [{id: 0, label: "全部城市", children: []}],            // 所有城市
        weidus: [
          {id: 0, label: "全部库存"},
          {id: 1, label: "无库存"},
          {id: 2, label: "有库存"},
        ],
        stockOptions: [
          {id: 1, label: "高库存"},
          {id: 2, label: "正常"},
          {id: 3, label: "低库存"},
        ],
        ageOptions: [
          {id: 1, label: "高库龄"},
          {id: 0, label: "正常"}
        ],
        class1Id: [],
        class2Id: [],
        class3Id: [],
        allPmsClass: [],      // 所有的采销分类
        pms_c1s: [],          // 所有的一级采销分类
        pms_c2s: [],          // 所有的二级采销分类
        pms_c3s: [],          // 所有的三级采销分类
        computePeriodBegin: '',  //开始天数
        computePeriodEnd: '',     //结束天数
        stockStatus: '',       //库存状态
        ageStatus: '',         //库龄状态
        loadDataing: false,     // table加载数据
        tableData: [],
        totalCount: 0
      }
    },
    created() {
      // 查询所有的城市
      this.getAllCity()
      // 查询所有的采销分类
      this.getPmsClass()
      // 查询列表数据
      this.searchList()
    },
    computed: {
      title: function () {
        var customDate = this.getCustomDate()
        var tempTitle = "滞销商品"
        var start = "(" + customDate.dateEnd.year + "年" + customDate.dateEnd.month + "月" + customDate.dateEnd.date + "日" + "未售卖的商品)列表"
        return tempTitle + start;
      }
    },
    watch: {
      "class1Id": function (val) {
        this.class2Id = []
        this.pms_c2s = []
        var cl1_ids = []
        if (val && val.length == 1 && val[0] == 0) {
          // 查询所有的一级分类
          var cl1s = this.getClassByParentIds(0, 1)
          for (var i = 0; i < cl1s.length; i++) {
            cl1_ids.push(cl1s[i].id)
          }
        } else {
          cl1_ids = val
        }
        // 组装二级分类
        this.pms_c2s = this.setCategory(cl1_ids, 2)
      },
      "class2Id": function (val) {
        this.class3Id = []
        this.pms_c3s = []
        // 组装三级分类
        this.pms_c3s = this.setCategory(val, 3)
      },
    },
    mounted() {
    },
    methods: {
      // 查询日期
      getCustomDate: function () {
        let currentDate = new Date()
        let dateEnd = {}
        currentDate.setDate(currentDate.getDate() - 1)
        dateEnd = this.formatDate(currentDate)
        return {
          dateEnd
        }
      },

      //校验输入整数
      checkNoBegin: function (value) {
        let reg = /^[1-9]\d*$/;
        if (value) {
          if (value < 0 || value > 90 || new RegExp(reg).test(value) == false) {
            this.queryData.computePeriodBegin = '';
            this.$message({
              type: 'error',
              showClose: true,
              message: '输入值必须是1到90之间的正整数！'
            });
          }
        }
      },
      checkNoEnd: function (value) {
        let reg = /^[1-9]\d*$/;
        if (value) {
          if (value < 0 || value > 90 || new RegExp(reg).test(value) == false) {
            this.queryData.computePeriodEnd = '';
            this.$message({
              type: 'error',
              showClose: true,
              message: '输入值必须是1到90之间的正整数！'
            });
          }
        }
      },

      formatDate(time) {
        let year = time.getFullYear()
        let month = (time.getMonth() + 1) < 10 ? ('0' + (time.getMonth() + 1)) : (time.getMonth() + 1)
        let date = time.getDate() < 10 ? ('0' + time.getDate()) : time.getDate()

        return {
          year,
          month,
          date
        }
      },
      // 查询所有的城市
      getAllCity: function () {
        var vm = this
        this.$http.post("/common/searchPermissionCitys", {}).then(res => {
          var allCity = res.data.data
          var children = []
          for (var i = 0; i < allCity.length; i++) {
            children.push({id: allCity[i].cityId, label: allCity[i].cityName})
          }
          vm.citys[0].children = children
        });
      },
      //请求采销分类数据
      getPmsClass() {
        this.$http.post("/common/searchAllClass", {}).then(res => {
          this.allPmsClass = res.data.data;
          this.pms_c1s = this.setCategory([0], 1);
        });
      },
      setCategory(parent_ids, level) {
        var vm = this
        // 根据Ids,查询分类
        var classArr = this.getClassByIds(parent_ids)
        // 根据parentIds，查询分类
        for (var i = 0; i < classArr.length; i++) {
          classArr[i].children = this.getClassByParentIds(classArr[i].id, level)
        }
        return classArr
      },
      // 根据id,查询分类
      getClassByIds: function (ids) {
        var idsArr = []
        if (ids && ids.length == 1 && ids[0] == 0) {
          idsArr.push({id: 0, label: '全部一级分类'})
        } else {
          for (var i = 0; i < ids.length; i++) {
            for (var j = 0; j < this.allPmsClass.length; j++) {
              if (this.allPmsClass[j].id == ids[i] && this.allPmsClass[j].name.indexOf("美家优享") == -1) {
                idsArr.push({id: this.allPmsClass[j].id, label: this.allPmsClass[j].name})
                break
              }
            }
          }
        }
        return idsArr;
      },
      // 根据parentIds和level，查询分类
      getClassByParentIds: function (parentId, level) {
        var classArr = []
        this.allPmsClass.map((item, index) => {
          if (item.level == level && parentId == item.parentId && item.name.indexOf("美家优享") == -1) {
            classArr.push({id: item.id, label: item.name})
          }
        })
        return classArr
      },
      // 分页查询
      searchList: function () {
        this.queryData.pageNum = 1
        this.searchListData()
      },
      // 改变每页显示
      handleSizeChange(val) {
        this.queryData.pageNum = 1
        this.queryData.numPerPage = val
        this.searchListData()
      },
      // 查询后台数据
      searchListData: function () {
        this.loadDataing = true
        this.queryData.class1Id = this.class1Id
        this.queryData.class2Id = this.class2Id
        this.queryData.class3Id = this.class3Id
        this.$http.post("/zombie/pageListZombie", this.queryData).then(res => {
          this.tableData = res.data.data.items
          this.totalCount = res.data.data.totalCount
          this.loadDataing = false
          this.$message({
            type: 'success',
            message: '加载成功!'
          });
        }).catch(err => {
          this.tableData = []
          this.loadDataing = false
        });
      },
      // 改变查询页数
      handleCurrentChange(val) {
        this.queryData.pageNum = val
        this.searchListData()
      },
      // 导出数据
      exportData: function () {
        var vm = this;
        this.$confirm('您确定要导出吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            var param = {
                cityId:vm.queryData.cityId,
                stockFlag  :vm.queryData.stockFlag,
                stockStatus:vm.queryData.stockStatus,
                ageStatus  :vm.queryData.ageStatus,
                class1Id   :vm.class1Id,
                class2Id   :vm.class2Id,
                class3Id   :vm.class3Id,
                computePeriodBegin:vm.queryData.computePeriodBegin,
                computePeriodEnd:vm.queryData.computePeriodEnd,
                pageNum:vm.queryData.pageNum
            };
          vm.$http.download("/zombie/exportZombieStatistic", param,  vm.title+".csv").then(function () {
              vm.$notify.success({
                  title: '消息',
                  message: '导出成功！'
              });
          })
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
    },
    components: {
      Treeselect
      //,PcpOperateLogShow
    }
  }
</script>

<style scoped>
  #JssjShow {
    padding-bottom: 59px;
  }

  #JssjShow .top-address {
    padding: 10px 20px;
    font-size: 14px;
  }

  .split {
    padding: 10px 0px;
    border-top: 1px solid #ecebeb;
  }

  .title {
    float: left;
    margin: 20px 0px 10px 0px;
    color: #7593e8;
    border-left: 3px solid #93aede;
    padding-left: 10px;
    font-size: 14px;
  }

  main {
    display: inline-block;
    width: 100%;
    box-sizing: border-box;
  }

  main .search-label_2 {
    font-weight: bold;
    font-size: 13px;
    color: #ada7a7;
  }

  main .desc-label {
    line-height: 35px;
    font-weight: bold;
    font-size: 13px;
    color: #ada7a7;
    text-align: center;
  }

  .vue-treeselect__list-item {
    padding: 15px 0px;
  }

  .paginate_div {
    position: fixed;
    bottom: 0px; padding: 20px 0px; background: #fff;
    z-index: 999; border-top: 1px solid #eee;
    width: 100%;
  }
</style>
