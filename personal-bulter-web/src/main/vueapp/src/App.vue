<template>
  <div>
<!--    <mc-layout></mc-layout>-->
<!--    <vxe-table-->
<!--            border-->
<!--            resizable-->
<!--            :data="tableData"-->
<!--            :edit-config="{trigger: 'dblclick', mode: 'cell'}"-->
<!--            @edit-actived="editActivedEvent"-->
<!--            @edit-closed="editClosedEvent">-->
<!--      <vxe-table-column type="expand" width="60">-->
<!--        <template v-slot:content="{ row, rowIndex }">-->
<!--            <vxe-table border :data="row.child" :edit-config="{trigger: 'dblclick', mode: 'cell'}">-->
<!--              <vxe-table-column field="field" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>-->
<!--              <vxe-table-column field="desc" title="Age" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>-->
<!--            </vxe-table>-->
<!--        </template>-->
<!--      </vxe-table-column>-->
<!--      <vxe-table-column show-overflow>-->
<!--          <template v-slot="{ row }">-->
<!--              <vxe-table border :data="row.child" :edit-config="{trigger: 'dblclick', mode: 'cell'}">-->
<!--                  <vxe-table-column field="field" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>-->
<!--                  <vxe-table-column field="desc" title="Age" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>-->
<!--              </vxe-table>-->
<!--          </template>-->
<!--      </vxe-table-column>-->
<!--      <vxe-table-column field="role" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>-->
<!--      <vxe-table-column field="sex6" title="Sex" :edit-render="{name: '$select', options: sexList, optionProps: {value: 'value2', label: 'label'}}"></vxe-table-column>-->
<!--      <vxe-table-column field="num6" title="Number" :edit-render="{name: '$input', props: {type: 'number'}}"></vxe-table-column>-->
<!--      <vxe-table-column field="date12" title="Date" :edit-render="{name: '$input', props: {type: 'date'}}"></vxe-table-column>-->
<!--      <vxe-table-column field="date13" title="Week" :edit-render="{name: '$input', props: {type: 'week'}}"></vxe-table-column>-->
<!--    </vxe-table>-->


<!--    <el-upload-->
<!--      name="file"-->
<!--      class="upload-demo"-->
<!--      drag-->
<!--      :action="uploadUrl"-->
<!--      :with-credentials="true"-->
<!--      multiple>-->
<!--      <i class="el-icon-upload"></i>-->
<!--      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>-->
<!--      <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
<!--    </el-upload>-->

    <el-form :inline="true">
      <el-form-item label="类名">
        <el-input style="width: 800px" v-model="className" placeholder="类名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchClassMethod">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="classMethodList"  style="width: 100%">
      <el-table-column prop="className" label="类名称"></el-table-column>
      <el-table-column prop="methodName" label="方法名"></el-table-column>
      <el-table-column prop="paramClassNameList" label="入参" ></el-table-column>
      <el-table-column prop="returnClassName" label="返回结果"></el-table-column>
      <el-table-column width="180">
        <template slot-scope="scope">
          <el-button @click="showMethodReturn(scope.row)" type="text" size="mini">方法返回值</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
      title="我是标题"
      :visible.sync="drawer"
      :with-header="false"
      size="50%"
    >
      <div>
<!--        <json-view :data="json" :deep="10"/>-->
        <el-button @click="mockJsonSave" type="primary" size="mini">保存JSON</el-button>
        <div class="editor-container" style="height: 100%">
          <json-editor ref="jsonEditor" v-model="json" />
        </div>
      </div>
    </el-drawer>

  </div>

</template>

<script>
  import McLayout from "@/components/layout/McLayout";
  import JsonEditor from "@/components/JsonEditor";

  import jsonView from 'vue-json-views'

  export default {
    name: 'app',
    components: {McLayout, jsonView, JsonEditor},
    data() {
      return {
        outVisit: false,
        showLayout: false,

        tableData: [],
        sexList: [],

        uploadUrl: "/bulter/file/upload",
        fileList: [],

        className: "",
        classMethodList: [],
        curItem: {},
        drawer: false,
        json: {}
      }
    },
    mounted() {
      this.tableData = [{
        name: "wcl_1",
        role: "王成林",
        sex6: "2",
        num6: 3,
        date12: '2013-01-22',
        date13: '2020-02-21',
        child: [{
          field: "wcl_1_1",
          desc: "我测试_1_1"
        }]
      },
        {
          name: "wcl_2",
          role: "刚睡醒",
          sex6: "1",
          num6: 32,
          date12: '2014-02-22',
          date13: '2019-05-21',
          child: [{
            field: "wcl_2_1",
            desc: "我测试_2_1"
          }]
        }]
    },
    methods: {
      searchClassMethod(){
        this.$http.get("/bulter/showClassMethods/"+ this.className).then(res=> {
          this.classMethodList = res.data
        })
      },
      mockJsonSave(){
        const param = {
          className: this.curItem.className,
          methodName: this.curItem.methodName,
          argClassNameList: this.curItem.paramClassNameList,
          json: this.json
        }
        this.$http.post("/bulter/mocksave", param).then(res=> {
          this.classMethodList = res.data
        })
      },
      showMethodReturn(item){
        this.curItem = item
        this.drawer = true
        let formData = {
          className: item.className,
          methodName: item.methodName,
          argClasses: item.paramClassNameList
        }
        this.json = {}
        this.$http.post("/bulter/mockshow", formData).then(res=> {
          console.log(1111111)
          this.json = res.data.classInstance
        })
      },
      customUpload(fileobj) {
        let _data = new FormData();
        _data.append('file', fileobj.file)
        protoUpload(_data)
      },
      handleChange(file, fileList) {
        this.fileList = fileList.slice(-3);
      },
      editActivedEvent ({ row, column }, event) {
        console.log(`打开 ${column.title} 列编辑`)
      },
      editClosedEvent ({ row, column }, event) {
        console.log(`关闭 ${column.title} 列编辑`)
      }
    }
  }
</script>

<style>
  @import "./assets/common.css";

  .el-drawer__body{
    overflow: auto;
  }
  .editor-container{
    height: 100%;
  }
</style>
