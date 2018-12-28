<template>
  <div>
    <el-form :model="form" ref="groupAddForm" :rules="rules" style="padding-bottom: 20px">
      <!-- 商品名称 -->
      <el-form-item label="商品分类名称：" :label-width="formLabelWidth" prop="groupName">
        <el-input v-model="form.groupName"  style="width: 400px;"
                  placeholder="请输入商品分组名称、二级分类名称" >
        </el-input>
      </el-form-item>

      <!-- 二级分类集合 -->
      <el-form-item label="选择二级分类：" :label-width="formLabelWidth" prop="class2Id">
        <el-select v-model="form.class2Id" placeholder="请选择二级分类" style="max-width: 400px;width: 100%" multiple clearable filterable>
          <el-option
            v-for="item in allClass2s"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div style="text-align: right">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" :loading="saveButtonLoading" @click="submitForm('groupAddForm')">确 定</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NewGoodsGroup",
    props: {
      allClass2s: {
        type: Array,
        required: true
      },
     editable:{
          type:Boolean
     },
     goodsGroupInfo: {
          type: Object
      }
    },
    data() {
      return {
        form: {
          id:'',
          class2Id: [],
          groupName: ''
        },
        formLabelWidth: '140px',
        loadDataing: false,
        // 保存londing
        saveButtonLoading: false,
        // 校验规则
        rules: {
          class2Id: [
            {required: true, message: '请选择二级采销分类', trigger: 'change'}
          ],
          groupName: [
              {required: true, message: '请输入商品分类名称', trigger: 'change'}
          ]
        },
      }
    },
    computed: {

    },
    mounted () {
      console.log(this.form.class2Id)
      // 修改
      if(this.goodsGroupInfo && this.goodsGroupInfo!=null) {
        this.form.id = this.goodsGroupInfo.id
        this.form.groupName = this.goodsGroupInfo.groupName
        this.form.class2Id = this.goodsGroupInfo.class2Id
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
        this.$http.post("/goodsGroup/saveGroup", this.form).then(res => {
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
        this.$http.post("/goodsGroup/updateGroup", this.form).then(res => {
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
        this.$emit("close")
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