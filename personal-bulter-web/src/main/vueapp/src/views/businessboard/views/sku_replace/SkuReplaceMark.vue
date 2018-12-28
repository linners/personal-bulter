<template>
  <div class="skureplace">
    <el-form :model="form" ref="ruleForm" :rules="rules" size="mini" label-width="110px">
      <el-form-item label="更改状态：" prop="changeStatus">
        <el-radio-group v-model="form.changeStatus">
          <el-radio label="新品试销"></el-radio>
          <el-radio label="1">新品评估</el-radio>
          <el-radio label="2">短期禁下单</el-radio>
          <el-radio label="3">过季汰换商品</el-radio>
          <el-radio label="4">汰换商品</el-radio>
          <el-radio label="5">删除商品</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div class="form-footer">
      <el-button type="primary" :loading="saveButtonLoading" @click="submitForm('ruleForm')">确 定</el-button>
      <el-button @click="closeDialog">取 消</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'sku-replace-mark',
    props: {
      items: {
        type: Array,
        default() {
          return []
        }
      }
    },
    data() {
      return {
        rules: {
          changeStatus: [
            {required: true, message: '请选择变更后的状态', trigger: 'change'}
          ],
        },
        form: {
          changeStatus: ""
        },
        saveButtonLoading: false
      }
    },
    methods: {
      // 提交表单
      submitForm(formName) {
        let vm = this
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('此操作会将商品进行汰换, 是否继续?', '提示', {
              customClass: 'confirmClass',
              closeOnClickModal: false,
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              // http请求
              var params = []
              for(var item of this.items){
                params.push(item.id)
              }
              this.saveButtonLoading = true
              this.$http.post("/skureplace/changeStatus", params).then(res=>{
                this.saveButtonLoading = false
                this.closeDialog()
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                });
              }).catch(res=>{
                this.saveButtonLoading = false
              })
            }).catch(() => {

            });
            this.saveButtonLoading = false
          } else {
            return false;
          }
        });
      },
      // 取消
      closeDialog() {
        this.$emit("close")
      }
    }
  }
</script>

<style>
  .skureplace .form-footer {
    text-align: right;
    margin-top: 40px;
  }
  .confirmClass {
    vertical-align: top;
    margin-top: 180px;
  }
</style>