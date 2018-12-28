<template>
  <el-dialog :title="title" :visible.sync="dialogShow" :close-on-click-modal="false" @close="closeDialog">
    <el-form :model="form" ref="ruleForm" :rules="rules">
      <el-form-item label="选择商品分类：" :label-width="formLabelWidth" prop="groupId">
        <el-select v-model="form.groupId" filterable placeholder="请选择商品分类" style="max-width: 400px;width: 100%">
          <el-option
            v-for="item in goodsGroupIds"
            :key="item.id"
            :label="item.groupName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择城市：" :label-width="formLabelWidth" prop="cityId">
        <el-select v-model="form.cityId" filterable placeholder="请选择城市" style="max-width: 400px;width: 100%">
          <el-option
            v-for="item in allCitys"
            :key="item.cityId"
            :label="item.cityName"
            :value="item.cityId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择责任人：" :label-width="formLabelWidth" prop="staffId">
        <el-select v-model="form.staffId" filterable remote clearable style="max-width: 400px;width: 100%"
                   placeholder="请输入关键词"
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
      <el-form-item label="选择城市采购：" :label-width="formLabelWidth">
        <el-select value="" filterable remote clearable style="max-width: 400px;width: 100%"
                   placeholder="请输入关键词"
                   :remote-method="remoteMethod"
                   :loading="loadDataing" @change="pushCityUser">
          <el-option
                  v-for="item in staffUsers"
                  :key="item.uid"
                  :label="item.uid+' - '+ item.givenname + '（' + item.deptpath+ '）'"
                  :value="item.uid">
          </el-option>
        </el-select>
      </el-form-item>

      <div align="center" v-if="purchaseStaff && purchaseStaff.length > 0">
        <el-table :data="purchaseStaff" border >
          <el-table-column label="已选采购人员信息" header-align="center">
          <el-table-column
                  prop="givenname"
                  label="姓名">
          </el-table-column>
          <el-table-column
                  prop="deptpath"
                  label="部门">
          </el-table-column>
          <el-table-column
                  prop="address"
                  label="操作">
            <template slot-scope="scope">
              <el-button
                      @click="removeStaff(scope.$index)"
                      type="text"
                      size="small">
                删除
              </el-button>
            </template>
          </el-table-column>
          </el-table-column>
        </el-table>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" :loading="saveButtonLoading" @click="submitForm('ruleForm')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "NewCityUser",
    props: {
      show: {
        type: Boolean,
        required: true
      },
      goodsGroupIds: {
        type: Array,
        required: true
      },
      allCitys: {
        type: Array,
        required: true
      },
      cityUserInfo: {
        type: Object
      }
    },
    data() {
      return {
        editable: false,
        title: "新增城市负责人配置",
        dialogShow: this.show,
        form: {
          goodsGroupId: '',
          cityId: '',
          staffId: '',
          purchaseId: ''
        },
        formLabelWidth: '140px',
        loadDataing: false,
        staffUsers: [],
        purchaseStaff: [], //选中的采购人员集合
          // 保存londing
        saveButtonLoading: false,
        // 校验规则
        rules: {
          groupId: [
            {required: true, message: '请选择商品分类', trigger: 'change'}
          ],
          cityId: [
            {required: true, message: '请选择城市', trigger: 'change'}
          ],
          staffId: [
            {required: true, message: '请输入责任人', trigger: 'change'}
          ]
        },
      }
    },
    computed: {

    },
    mounted () {
      // 修改
      if(this.cityUserInfo && this.cityUserInfo!=null) {
        this.form.cityId = this.cityUserInfo.cityId
        this.form.staffId = this.cityUserInfo.staffId
        this.form.groupId = this.cityUserInfo.groupId
        this.purchaseStaff = this.cityUserInfo.purchaseList
        this.purchaseStaff = []
        if(this.cityUserInfo.purchaseList && this.cityUserInfo.purchaseList.length > 0) {
          for(var item of this.cityUserInfo.purchaseList) {
              this.purchaseStaff.push(item);
          }
        }

        this.staffUsers.push({
            uid: this.cityUserInfo.staffId,
            givenname: this.cityUserInfo.staffName,
            deptpath: this.cityUserInfo.staffDept
        })
      this.title = "修改城市负责人配置"
      this.editable = true
      }
    },
    methods: {
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
      // 添加城市采购人员
      pushCityUser(staffId) {
        for(var item of this.staffUsers){
          // 校验重复
          var checkRepeat = this.checkStaffRepeat(item,this.purchaseStaff);
          if (checkRepeat) {
              this.$notify({
                  title: '人员重复',
                  message: "城市采购["+item.givenname+"]已经添加",
                  type: 'error'
              });
              return;
          }
          if(item.uid == staffId){
              this.purchaseStaff.push(item)
          }
        }
        this.form.purchaseId = ""
        this.staffUsers = null;
      },
      // 校验是否重复添加
      checkStaffRepeat(item,list) {
          if(list == null) {
              return false;
          }

         for(var obj of list) {
             if (obj.uid == item.uid) {
                return true;
             }
         }
         return false;
      },
      // 删除城市采购人员
      removeStaff(index) {
        this.purchaseStaff.splice(index, 1);
      },
      // 校验form表单内容
      submitForm(formName) {
        this.saveButtonLoading = true
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.editable){    //  修改
              this.editConfig()
            }else {
              this.saveConfig()   // 新增
            }
          } else {
            this.saveButtonLoading = false
            return false;
          }
        })
      },
      // 新增
      saveConfig () {
        // 获取已选择采购人员id
       var purchaseIds = [];
       if (this.purchaseStaff != null){
         for(var purchase of this.purchaseStaff) {
             purchaseIds.push(purchase.uid)
         }
       }
        this.form.purchaseId = purchaseIds

        this.$http.post("/dealconfig/newCityUser", this.form).then(res => {
        this.saveButtonLoading = false
        var resData = res.data
        if(resData.ret==1){
          this.$notify({
            title: '成功',
            message: '保存成功',
            type: 'success'
          });
          this.closeDialog()
          this.searchList()
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
      },
      // 修改
      editConfig () {
        var param = this.form
        param.id = this.cityUserInfo.id
          // 获取已选择采购人员id
          var purchaseIds = [];
          for(var purchase of this.purchaseStaff) {
              purchaseIds.push(purchase.uid)
          }
          this.form.purchaseId = purchaseIds

        this.$http.post("/dealconfig/editCityUser", param).then(res => {
          this.saveButtonLoading = false
          var resData = res.data
          if(resData.ret==1){
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success'
            });
            this.closeDialog()
            this.searchList()
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
      },
      // 关闭窗口
      closeDialog() {
        this.dialogShow = false
        this.$emit("update:show", false)
      },
      // 查询列表
      searchList () {
        this.$emit("reflush")
      }
    }
  }
</script>

<style>
  .el-dialog__header {
    border-bottom: 1px solid #ddd;
  }
</style>