<template>
  <div id="LowerVolumeShow">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>商品监控</el-breadcrumb-item>
      <el-breadcrumb-item>低动销商品信息查询</el-breadcrumb-item>
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
          <!-- 采销分类筛选 -->
          <div style="margin-top: 10px;">
            <!--<span class="search-label_2" style="margin-top: 10px;">选择采销分类：</span>-->
          </div>
          <el-row :gutter="20">
            <el-col :span="11">
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
            <el-col :span="10">
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
          </el-row>

          <!-- 有无库存，销量筛选 -->
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="7">
              <!--<span class="search-label_2">有无库存：</span>-->
              <el-select v-model="queryData.stockFlag" placeholder="请选择" size="medium" filterable style="width: 100%;">
                <el-option
                  v-for="item in stockFlagOptions"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
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
            <el-col :span="1" style=" margin-top: 10px;margin-left: -17px;">
              <el-tooltip placement="top">
                <div slot="content" >
                  低动销商品是指全国主城（不包括卫星城）、自营<br/>
                  （非POP、非合伙人、非大客户、非分销）商品中、<br/>
                  查询前一天24时处于上线状态且至少过去7天/30天<br/>
                  （取决于定义）有销量但销量不高于某个值（该值可以设<br/>
                  定，可以根据商品性质的不同来设定不同的值）的商品。
                </div>
                <i class="el-icon-question"></i>
              </el-tooltip>
            </el-col>
          </el-row>

          <!-- 库存，库龄 筛选 -->
          <el-row :gutter="20" style="margin-top: 10px;">
            <el-col :span="7" style="display: flex;flex-direction: row">
              <span class="search-label_2">销量筛选：</span>
              <el-select style="flex: 1;" v-model="queryData.saleType" placeholder="请选择" size="medium" filterable>
                <el-option
                  v-for="item in saleOptions"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <el-input v-model="queryData.percent" maxlength="4" size="medium" @change="checkNumber('percent',queryData.percent)">
                <template slot="prepend">销量低于：</template>
                <template slot="append">%</template>
              </el-input>
            </el-col>
            <el-col :span="7">
              <el-input v-model="queryData.saleAmount" maxlength="4" size="medium" @change="checkNumber('saleAmount',queryData.saleAmount)">
                <template slot="prepend">不高于:</template>
                <template slot="append">件</template>
              </el-input>
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
            库存周转天数 = 仓库库存数量（件）/ 过去30天的日平均销量（件）；<br/>
            二级分类金额周转天数 = 属于该商品的二级分类下的所有商品的库存金额（元）/ 属于该商品的二级分类下所有商品的过去7天的平均销售金额（元）<br/>
            库存金额 = 仓库库存数量（件）* 实时成本价（元/件）<br/>
            是否新品：若该商品在该仓库内第一次入库时间早于查询日期减去新品配置时间，则该商品属于新品，反之，若晚于或等于，则该商品不是新品。<br/>
          </div>
          <i class="el-icon-question" style="color: #0C1012"></i>
        </el-tooltip>
        </span>
      </div>
      <div style="float: right;margin-top: 15px;">
        <el-button size="mini" @click="exportData">导出列表</el-button>
      </div>

      <!-- 列表展示 -->
      <div style="padding-top: 20px;padding-bottom: 40px;">
        <el-table
          v-loading="loadDataing" element-loading-text="拼命加载中"
          :data="tableData"
          stripe
          border
          :header-cell-style="{'background-color': '#e7edf3',textAlign:'center'}"
          style="width: 100%;overflow-x: hidden">
          <el-table-column label="商品基础信息" header-align="center">
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

            <el-table-column
              prop="class1Name"
              label="一级分类名称"
              width="120">
            </el-table-column>

            <el-table-column
              prop="class2Name"
              label="二级分类名称"
              width="120">
            </el-table-column>

            <el-table-column
              prop="class3Name"
              label="三级分类名称"
              width="120">
            </el-table-column>

            <el-table-column
              prop="cityName"
              label="城市名称"
              width="120">
            </el-table-column>

            <el-table-column
              prop="warehouseName"
              label="仓库名称"
              width="120">
            </el-table-column>

            <el-table-column
              prop="skuFormat"
              label="规格"
              width="120">
            </el-table-column>

            <el-table-column
              prop="skuValuationUnitName"
              label="计价单位"
              width="120">
            </el-table-column>

            <el-table-column
              prop="supplierName"
              label="供应商名称"
              width="120">
            </el-table-column>
          </el-table-column>

          <!-- 动销信息 -->
          <el-table-column label="动销信息" header-align="center">
            <el-table-column
              prop="averageSaleNumPer7days"
              label="周销量"
              width="120">
            </el-table-column>

            <el-table-column
              prop="averageSaleNumPer30days"
              label="30天月销量"
              width="120">
            </el-table-column>

            <el-table-column
              prop="ssuIds"
              label="ssu集合"
              width="120">
            </el-table-column>
          </el-table-column>

          <!-- 库存信息 -->
          <el-table-column label="库存信息" header-align="center">
            <el-table-column
              prop="inventoryNumber"
              label="仓库库存量(件)"
              width="160">
            </el-table-column>

            <el-table-column
              prop="rotationDays"
              label="库存周转天数"
              width="160">
            </el-table-column>

            <el-table-column
              prop="rotationDaysCat2"
              label="二级分类金额周转天数"
              width="160">
            </el-table-column>

            <el-table-column
              prop="inventoryMoney"
              label="库存金额"
              width="160">
            </el-table-column>

            <el-table-column
              prop="stockLabel"
              label="库存标签"
              width="160">
            </el-table-column>

            <el-table-column
              prop="statusNewProductLabel"
               label="是否新品"
              width="160">
            </el-table-column>
          </el-table-column>

          <!-- 库龄信息 -->
          <el-table-column label="库龄信息" header-align="center" label-class-name="t1_cls">
            <el-table-column
              prop="inventoryAgeDays"
              label="在库天数"
              width="110">
            </el-table-column>

            <el-table-column
              prop="inventoryAgeDaysStatusLabel"
              label="库龄状态"
              width="110">
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>
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
  </div>
</template>

<script>
// import the component
import Treeselect from '@riophae/vue-treeselect'
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: 'LowerVolumeShow',
	props: [],
	data() {
		return {
      queryData: {
        cityId: [0],
        saleType:1,            // 销量筛选
        saleAmount : 10,       //默认销售数量
        stockFlag : 0,         //默认全部库存
        percent : 20,          //销量百分比
        pageNum: 1,            // 当前查询页数
        numPerPage: 20           // 每页展示数
      },
      cityId: [],
			citys: [{id: 0, label: "全部城市", children: []}],            // 所有城市
      saleOptions: [
        {id: 1, label: "7天销量"},
        {id: 2, label: "15天销量"},
        {id: 3, label: "30天销量"},
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
      stockFlagOptions:[
      {id: 0, label: "全部库存"},
      {id: 1, label: "无库存"},
      {id: 2, label: "有库存"},
    ],
      class1Id: [],
      class2Id: [],
      class3Id: [],
      allPmsClass: [],      // 所有的采销分类
      pms_c1s: [],          // 所有的一级采销分类
      pms_c2s: [],          // 所有的二级采销分类
      pms_c3s: [],          // 所有的三级采销分类
      saleAmount:'',        //销售数量
      stockStatus:'',       //库存状态
      ageStatus:'',         //库龄状态
      loadDataing: false,     // table加载数据
      tableData: [],
      totalCount: 0
		}
	},
	created(){
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
      var tempTitle = ""
      var end =customDate.dateEnd.year + "年" + customDate.dateEnd.month + "月" + customDate.dateEnd.date + "日"
      var start =customDate.dateStart.year + "年" + customDate.dateStart.month + "月" + customDate.dateStart.date + "日"

      //获取销量
      var saleAmountLabel;
      if (this.queryData.saleAmount > 0) {
          saleAmountLabel = "期间内销量不高于" + this.queryData.saleAmount + "的商品";
      } else {
          saleAmountLabel = "";
      }

      if(this.queryData.saleType==1){
        tempTitle =  "低动销商品（" + start + " - " + end +saleAmountLabel+ "）列表, 共" + this.totalCount + "个商品"
      }else if(this.queryData.saleType==2){
        tempTitle = "低动销商品（" + start + " - " + end + saleAmountLabel+"）列表, 共" + this.totalCount + "个商品"
      }
      return tempTitle
    }
  },
  watch: {
    "class1Id": function (val) {
      this.class2Id = []
      this.pms_c2s = []
      var cl1_ids = []
      if(val && val.length==1 && val[0]==0){
        // 查询所有的一级分类
        var cl1s = this.getClassByParentIds(0, 1)
        for(var i=0; i<cl1s.length; i++){
          cl1_ids.push(cl1s[i].id)
        }
      }else {
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
	mounted(){},
	methods: {
    //校验输入整数
    checkNumber :function(name,value) {
        //校验整数合法
      let reg = /^[1-9]\d*$/;
      if (value < 0 || new RegExp(reg).test(value) == false) {
          if (name == "percent") {
              this.queryData.percent = '';
          } else if (name == "saleAmount") {
            this.queryData.saleAmount ='';
          }
        this.$message({
          type: 'error',
          showClose: true,
          message: '输入值必须是正整数！'
        });
          return;
      }
      // 校验范围合法
      if (name == "percent") {
          if (value <=0 || value >100) {
            this.queryData.percent = '';
            this.$message({
              type: 'error',
              showClose: true,
              message: '输入值必须是1~100之间正整数！'
            });
          }
      }
    },
    // 查询日期
    getCustomDate: function () {
      let currentDate = new Date()
      let dateEnd = {}
      let dateStart = {}
      switch (this.queryData.saleType) {
        case 1: // 过去7天
          currentDate.setDate(currentDate.getDate() - 1)
          dateEnd = this.formatDate(currentDate)
          currentDate.setDate(currentDate.getDate() - 7 + 1)
          dateStart = this.formatDate(currentDate)
          break
        case 2: // 过去30天
          currentDate.setDate(currentDate.getDate() - 1)
          dateEnd = this.formatDate(currentDate)
          currentDate.setDate(currentDate.getDate() - 30 + 1)
          dateStart = this.formatDate(currentDate)
          break
      }

      return {
        dateStart,
        dateEnd,
      }
    },
    formatDate (time) {
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
        for(var i=0; i<allCity.length; i++){
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
    setCategory(parent_ids, level){
      var vm = this
      // 根据Ids,查询分类
      var classArr = this.getClassByIds(parent_ids)
      // 根据parentIds，查询分类
      for(var i=0; i<classArr.length; i++) {
        classArr[i].children = this.getClassByParentIds(classArr[i].id, level)
      }
      return classArr
    },
    // 根据id,查询分类
    getClassByIds: function (ids) {
      var idsArr = []
      if(ids && ids.length==1 && ids[0]==0){
        idsArr.push({id: 0, label: '全部一级分类'})
      }else {
        for(var i=0; i<ids.length; i++){
          for(var j=0; j<this.allPmsClass.length; j++) {
            if(this.allPmsClass[j].id == ids[i] && this.allPmsClass[j].name.indexOf("美家优享")==-1){
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
      this.allPmsClass.map((item, index)=>{
        if(item.level == level && parentId == item.parentId && item.name.indexOf("美家优享")==-1) {
          classArr.push({id: item.id, label: item.name})
        }
      })
      return classArr
    },
    // 分页查询
    searchList: function () {
      this.queryData.pageNum = 1
      //校验参数
      var msg = this.checkQueryParam();
      if (msg != null) {
        this.$message({
          type: 'error',
          showClose: true,
          message: msg
        });
          return;
      }
      this.searchListData()
    },

    // 查询参数校验 预留
    checkQueryParam() {

       return null;
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
      this.$http.post("/lowerVolume/pageListLowerVolume", this.queryData).then(res => {
        this.tableData = res.data.data.items
        this.totalCount = res.data.data.totalCount
        this.loadDataing = false
        this.$message({
            type: 'success',
            message: '加载成功!'
        });
      }).catch(err => {
          console.log(err);
      })
    },
    // 改变查询页数
    handleCurrentChange(val) {
      this.queryData.pageNum = val
      this.searchListData()
    },
    // 导出数据
    exportData: function () {
      var vm = this;
      //导出校验参数 预留
      var msg = this.checkQueryParam();
      if (msg != null) {
        this.$message({
          type: 'error',
          showClose: true,
          message: msg
        });
        return;
      }
      this.$confirm('您确定要导出吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var param = this.queryData
        param.class1Id = vm.class1Id
        param.class2Id = vm.class2Id
        vm.$http.download("/lowerVolume/exportLowerVolume", param, vm.title+".csv")
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
  }
}
</script>

<style scoped>
  #LowerVolumeShow {
    padding-bottom: 30px;
  }
  #LowerVolumeShow .top-address {
    padding: 10px 20px;
    font-size: 14px;
  }
  .split {
    padding: 10px 0px;
    border-top: 1px solid #ecebeb;
  }
  .title{
    float: left;margin: 20px 0px 10px 0px;color: #7593e8;border-left: 3px solid #93aede;padding-left: 10px;
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
    border: 1px solid #ddd;
    padding: 8px;
    background: #f5f7fa;
    margin-right: -1px;

  }

  main .desc-label {
    line-height: 35px;
    font-weight: bold;
    font-size: 13px;
    color: #ada7a7;
    text-align: center;
  }

  table thead th .t1_cls {
    background: red !important;
  }
  .paginate_div {
    position: fixed;
    bottom: 0px; padding: 20px 0px; background: #fff;
    z-index: 999; border-top: 1px solid #eee;
    width: 100%;
  }
</style>
