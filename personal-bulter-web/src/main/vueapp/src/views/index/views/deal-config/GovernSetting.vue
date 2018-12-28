<template>
  <el-dialog title="治理方式配置" :visible.sync="dialogShow" :close-on-click-modal="false" @close="closeDialog" width="600px">
    <el-form :model="form" ref="ruleForm" :rules="rules">
      <el-form-item label="治理方式：" :label-width="formLabelWidth" prop="sendMode">
        <el-select v-model="form.sendMode" placeholder="请选择治理方式" style="width: 300px;">
          <el-option label="自动" :value="1"></el-option>
          <el-option label="手动" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="治理频次：" :label-width="formLabelWidth" prop="sendPeriod">
        <!--<el-input-number v-model="num1" @change="handleChange" :min="1" :max="10" label="描述文字"></el-input-number>-->
        <el-input-number v-model="form.sendPeriod" style="width: 300px;" :min="1" :max="999" autocomplete="off" :disabled="form.sendMode==2">
        </el-input-number>
        <span style="margin-left: 5px">天</span>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" :loading="saveButtonLoading" @click="submitForm('ruleForm')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "GovernSetting",
    props: {
      show: {
        type: Boolean,
        required: true
      },
      governModel: {
        type: Object
      }
    },
    data() {
      var vm = this
      var vcheckAge = (rule, value, callback) => {
        if(vm.form.sendMode==1){
          if (!value) {
            return callback(new Error('不能为空'));
          }
          var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
          if (!re.test(value)) {
            return callback(new Error('请输入数字值,且大于0'));
          }
          if(parseInt(value)<0){
            return callback(new Error('不能小于0'));
          }
          if(parseInt(value)>999){
            return callback(new Error('不能小于999'));
          }
        }
        callback();
      };
      return {
        form: {
          sendMode: "1",
          sendPeriod: 0
        },
        dialogShow: this.show,
        formLabelWidth: '120px',
        loadDataing: false,
        // 保存londing
        saveButtonLoading: false,

        // 校验规则
        rules: {
          sendMode: [
            {required: true, message: '请选择治理方式', trigger: 'change'}
          ],
          sendPeriod: [
            {validator: vcheckAge, trigger: 'blur'}
          ]
        },
      }
    },
    computed: {

    },
    mounted () {
      this.form.sendMode = this.governModel.sendMode
      this.form.sendPeriod = this.governModel.sendPeriod
    },
    watch: {
      governModel: {
        handler(newVal, oldVal) {
          this.form.sendMode = newVal.sendMode
          this.form.sendPeriod = newVal.sendPeriod
        },
        immediate: true,
        deep: true
      }
    },
    methods: {
      // 校验form表单内容
      submitForm(formName) {
        this.saveButtonLoading = true
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.saveConfig()
          } else {
            this.saveButtonLoading = false
            return false;
          }
        })
      },
      // 保存
      saveConfig () {
        this.$http.post("/dealconfig/saveGovernSetting", this.form).then(res => {
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