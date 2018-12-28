<template>
  <div>
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item><i class="el-icon-caret-right"></i> 商品指挥平台</el-breadcrumb-item>
      <el-breadcrumb-item>有库存未上线</el-breadcrumb-item>
      <el-breadcrumb-item>治理方式配置</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="split"></div>
    <div class="ss-content" style="margin-top: -20px;">
      <div class="coll-h3" style="margin-bottom: 20px">治理方式配置</div>
      <div class="coll-content">
        <el-row>
          <el-col :span="7">
            <div><span class="deal_label">治理方式：</span><span class="deal_content">{{governSetting.sendMode==2?'手动治理':'自动治理'}}</span></div>
          </el-col>
          <el-col :span="7">
            <div><span class="deal_label">治理时间间隔：</span><span class="deal_content">{{governSetting.sendPeriod?governSetting.sendPeriod+'天':'无'}}</span></div>
          </el-col>
          <el-col :span="7" style="margin-top: 5px;">
            <div><el-button size="small" type="primary" @click="showGovernModel">设置</el-button></div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="7">
            <div><span class="deal_label">上次治理日期：</span><span class="deal_content">{{governSetting.lastSendTime?formatDate(governSetting.lastSendTime):'无'}}</span></div>
          </el-col>
          <el-col :span="7">
            <div><span class="deal_label">下次治理日期：</span><span class="deal_content">{{governSetting.nextSendTime?formatDate(governSetting.nextSendTime):'无'}}</span></div>
          </el-col>
        </el-row>
      </div>

      <!-- 弹框新增配置 -->
      <div v-if="showGovernSetting">
        <govern-setting :show.sync="showGovernSetting" :govern-model="governSetting" @reflush="getGovernSetting"></govern-setting>
      </div>

      <!-- TAB页配置 -->
      <div style="margin-top:10px;">
        <el-tabs type="card" @tab-click="handleClick">
          <el-tab-pane label="城市采购配置" >
            <div v-if="tabType == 0">
              <CityManagerConfig ref="cityManagers" :govern-setting="governSetting"></CityManagerConfig>
            </div>
          </el-tab-pane>
          <el-tab-pane label="商品分类配置" >
            <div v-if="tabType == 1">
              <GoodsGroupConfig ref="goodsGroups" :govern-setting="governSetting"></GoodsGroupConfig>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

    </div>
  </div>
</template>

<script>
  import moment from 'moment'
  import GovernSetting from "./GovernSetting";
  import GoodsGroupConfig from "./GoodsGroupConfig";
  import CityManagerConfig from "./CityManagerConfig";
  export default {
    name: "SearchSkus",
    components: {GovernSetting,CityManagerConfig,GoodsGroupConfig},
    data() {
      return {
        tabType: 0,
        sendMsgTip: "点击确认后，会发送给对应的城市端负责人，提醒他们治理，并对治理结果进行监控。",
        // 治理配置窗口
        showGovernSetting: false,
        // 治理配置对象
        governSetting: {}
      }
    },
    watch: {

    },
    mounted() {
      this.initContent();
    },
    methods: {
        handleClick(tab, e) {//点击选项卡
            this.tabType = tab.index;
        },
      initContent(){
        this.getGovernSetting();
      },
      // 查询治理设置
      getGovernSetting () {
        this.$http.post("/dealconfig/getGovernSetting", {}).then(res => {
          this.$nextTick(()=>{
            this.governSetting = res.data.data
          })
        }).catch(err => {
          console.log(err);
        })
      },
      // 格式化时间
      formatDate (time) {
        return moment(time).format('YYYY-MM-DD')
      },
      // 治理设置
      showGovernModel () {
        this.showGovernSetting = true
      }
    }
  }
</script>

<style scoped>
  .split {
    padding: 10px 0px;
    border-top: 1px solid #ecebeb;
  }

  .coll-h3 {
    margin: 20px 0px 0 0;
    color: #1F2D3D;
    font-size: 16px;
    font-family: 'PingFangSC-Medium', sans-serif;
    font-weight: bold;
  }

  .ss-content {
    font-size: 12px;
    font-family: PingFangSC-Regular;
  }

  .coll-content {
    margin-top: 20px;
    padding: 20px 20px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }

  .coll-content .deal_label {
    color: #475669;
    font-size: 14px;
  }
  .coll-content .deal_content {
    color: #1F2D3D;
    font-size: 13px;
  }

  .radio-group-wrap .radio-item {
    margin-top: 20px;
  }

  .radio-group-wrap .radio-city {
    margin-bottom: 20px;
  }
</style>