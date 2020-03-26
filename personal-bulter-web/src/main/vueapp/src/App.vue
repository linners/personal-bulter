<template>
  <div>
<!--    <mc-layout></mc-layout>-->
    {{tableData}}
    <vxe-table
            border
            resizable
            :data="tableData"
            :edit-config="{trigger: 'dblclick', mode: 'cell'}"
            @edit-actived="editActivedEvent"
            @edit-closed="editClosedEvent">
      <vxe-table-column type="expand" width="60">
        <template v-slot:content="{ row, rowIndex }">
            <vxe-table border :data="row.child" :edit-config="{trigger: 'dblclick', mode: 'cell'}">
              <vxe-table-column field="field" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
              <vxe-table-column field="desc" title="Age" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
            </vxe-table>
        </template>
      </vxe-table-column>
      <vxe-table-column show-overflow>
          <template v-slot="{ row }">
              <vxe-table border :data="row.child" :edit-config="{trigger: 'dblclick', mode: 'cell'}">
                  <vxe-table-column field="field" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
                  <vxe-table-column field="desc" title="Age" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
              </vxe-table>
          </template>
      </vxe-table-column>
      <vxe-table-column field="role" title="Role" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
      <vxe-table-column field="sex6" title="Sex" :edit-render="{name: '$select', options: sexList, optionProps: {value: 'value2', label: 'label'}}"></vxe-table-column>
      <vxe-table-column field="num6" title="Number" :edit-render="{name: '$input', props: {type: 'number'}}"></vxe-table-column>
      <vxe-table-column field="date12" title="Date" :edit-render="{name: '$input', props: {type: 'date'}}"></vxe-table-column>
      <vxe-table-column field="date13" title="Week" :edit-render="{name: '$input', props: {type: 'week'}}"></vxe-table-column>
    </vxe-table>
  </div>
</template>

<script>
  import McLayout from "@/components/layout/McLayout";

  export default {
    name: 'app',
    components: {McLayout},
    data() {
      return {
        outVisit: false,
        showLayout: false,

        tableData: [],
        sexList: []
      }
    },
    created() {
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
      this.findSexList()
    },
    methods: {
      findSexList () {
        return XEAjax.get('/api/conf/sex/list').then(data => {
          this.sexList = data
        })
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
</style>
