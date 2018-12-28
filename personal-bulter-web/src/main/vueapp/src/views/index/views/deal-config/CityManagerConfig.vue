<template>
<div>
    <div class="coll-h4" style="margin-bottom: 20px">城市端负责人名单</div>
    <el-form :inline="true" :model="queryData" class="demo-form-inline">
        <el-form-item>
            <el-select size="small"clearable filterable v-model="queryData.groupId" placeholder="商品分类名称">
                <el-option
                        v-for="item in goodsGroupIds"
                        :key="item.id"
                        :label="item.groupName"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-select size="small"clearable filterable v-model="queryData.cityId" placeholder="城市">
                <el-option
                        v-for="item in allCitys"
                        :key="item.cityId"
                        :label="item.cityName"
                        :value="item.cityId">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <!--<el-input size="small" clearable v-model="queryData.staffMail" placeholder="员工邮箱"></el-input>-->
            <el-select v-model="queryData.staffId" filterable remote clearable style="width: 300px;" size="small"
                       placeholder="请输入责任人姓名、邮箱或工号"
                       :remote-method="remoteMethod"
                       :loading="loadDataing">
                <el-option
                        v-for="item in staffUsers"
                        :key="item.uid"
                        :label="item.uid+' - '+ item.givenname + '（' + item.deptpath+ '）'"
                        :value="item.uid">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search" plain @click="onSubmit">查询</el-button>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="success" icon="el-icon-search" plain @click="newConfig">新增配置</el-button>
        </el-form-item>
        <el-form-item>
            <el-button size="small" type="warning" icon="el-icon-download" plain @click="sendConfigMsg" v-if="governSetting.msgBtnEnable==2">开启治理</el-button>
        </el-form-item>
    </el-form>
    <CityUserConfig ref="skusTable" :table-data="tableData" :loading-show="loadingShow" :error-show="errorShow" @edit-config="editConfig" @reflush="onSubmit"></CityUserConfig>
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

    <!-- 弹框新增配置 -->
    <div v-if="showNewCityUserConfig">
        <new-city-user :all-citys="allCitys" :goods-group-ids="goodsGroupIds" :city-user-info="curCityUserInfo" :show.sync="showNewCityUserConfig" @reflush="onSubmit"></new-city-user>
    </div>

    <!-- 发送消息确认 -->
    <el-dialog title="发送提醒" :visible.sync="sendMsgShow" width="500px">
        <h4 style="text-align: center;font-size: 15px">治理期数：{{governBatch}}期</h4>
        <p style="margin-top: 15px;font-size: 14px;padding: 5px 25px;line-height:25px" v-html="sendMsgTip"></p>
        <span slot="footer" class="dialog-footer" style="text-align: center">
              <el-button size="small" @click="sendMsgShow = false">取 消</el-button>
              <el-button size="small" type="primary" @click="sendDingdingMsg">确 定</el-button>
            </span>
    </el-dialog>
 </div>
</template>

<script>
    import moment from 'moment'
    import CityUserConfig from './CityUserConfig'
    import NewCityUser from './NewCityUser'

    export default {
        name: "CityManagerConfig",
        components: {NewCityUser, CityUserConfig},
        props:{
            governSetting:{
                type:Object
            }
        },
        data() {
            return {
                // 所有的城市
                allCitys: [],
                // 所有的商品分类
                goodsGroupIds: [],
                queryData: {
                    class1Id: '',
                    cityId: '',
                    staffMail: ''
                },
                // 钉钉用户查询
                loadDataing: false,
                staffUsers: [],
                curPage: 1,
                pageSize: 10,
                total: 0,
                tableData: [],
                loadingShow: false,
                errorShow: false,
                // 新建窗口
                showNewCityUserConfig: false,
                // 当前操作的用户信息
                curCityUserInfo: {},

                // 发送消息dialog
                sendMsgShow: false,
                // 治理期数
                governBatch: moment(new Date()).format("YYYYMMDD"),
                sendMsgTip: "点击确认后，会发送给对应的城市端负责人，提醒他们治理，并对治理结果进行监控。"
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
                var _val = tab.index;//
                this.getWaterDetails(_val);
            },

            initContent(){
                this.searchgoodsGroupIds();
                this.searchAllCitys();
                this.queryTableData();
            },
            // 格式化时间
            formatDate (time) {
                return moment(time).format('YYYY-MM-DD')
            },
            // 查询所有的一级采销分类
            searchgoodsGroupIds () {
                this.$http.post("/goodsGroup/listGoodsCategory", {}).then(res => {
                    this.goodsGroupIds = res.data.data
                }).catch(err => {
                    console.log(err);
                })
            },

            // 查询所有的城市
            searchAllCitys () {
                this.$http.post("/common/searchAllCitys", {}).then(res => {
                    this.allCitys = res.data.data
                }).catch(err => {
                    console.log(err);
                })
            },
            // 按照名称和邮箱，模糊查询用户
            remoteMethod(queryString) {
                this.loadDataing = true
                this.$http.post("/common/searchStaffByEmail", {email: queryString}).then(res => {
                    this.staffUsers = res.data.data
                    this.loadDataing = false
                }).catch(err => {
                    console.log(err);
                });
            },
            // // 按员工邮箱，模糊查询
            // searchStaffByEmail () {
            //   this.$http.post("/common/searchStaffByEmail", {email: this.queryData.staffMail}).then(res => {
            //     this.allCitys = res.data.data
            //   }).catch(err => {
            //     console.log(err);
            //   })
            // },
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
                this.$http.post("/dealconfig/queryList", this.queryData).then(res => {
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
            // 新增城市端人员配置
            newConfig () {
                this.curCityUserInfo = null
                this.showNewCityUserConfig = true
            },
            // 修改城市端人员配置
            editConfig (item) {
                this.showNewCityUserConfig = true
                this.curCityUserInfo = item
            },
            // 开启治理，发送消息
            sendConfigMsg () {
                this.$http.post("/dealconfig/checkBindStaff", {}).then(res => {
                    var resData = res.data
                    if(resData.ret==0) {
                        var total = resData.data.total
                        var list = resData.data.list
                        var sendMsgTip = "<div>警告: </div><div>存在 <span style='color: red'>" + total + " </span>个二级分类和城市没有配置相应的负责人,下面是前5个<div>";
                        for(var item of list){
                            sendMsgTip = sendMsgTip + "<p style='color: red;'>"+item+"</p>"
                        }
                        sendMsgTip = sendMsgTip + "<div>点击确认后，会发送给对应的城市端负责人，提醒他们治理，并对治理结果进行监控。</div>"
                        this.sendMsgTip = sendMsgTip;
                    }
                    this.sendMsgShow = true
                }).catch(err => {

                })
            },
            // 发送通知
            sendDingdingMsg () {
                this.sendMsgShow = false
                this.$http.post("/dealconfig/sendDealMsg", this.queryData).then(res => {
                    var resData = res.data
                    if(resData.ret==1){
                        this.$notify({
                            title: '成功',
                            message: '发送成功',
                            type: 'success'
                        });
                    }else {
                        this.$notify({
                            title: '警告',
                            message: resData.error,
                            type: 'warning'
                        });
                    }
                }).catch(err => {

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