<template>
  <div class="chart" v-loading="spinShow" :element-loading-text="loadingInfo">
    <div class="bedroom" ref='area'></div>
    <div class="el-loading-mask" v-show="errorSpin">
      <div class="el-loading-spinner">
        <p class="el-loading-text" style="color: #e68555;">
          {{errorTip}}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
  import DateUtil from '@/utils/dateutil'
  import echarts from 'echarts';
  import themeData from '@/assets/westeros.json'
  import FormatUtil from '@/utils/formatutil'

  export default {
    name: 'EchartShow',
    props: {
      option: {
        type: Object,
        default: {}
      },
      spinShow: { // 图表加载状态显示/隐藏
        type: Boolean,
        default: false
      },
      errorSpin: { // 数据加载失败状态显示/隐藏
        type: Boolean,
        default: false
      },
      loadingInfo: { // 加载提示信息
        type: String,
        default: "拼命加载中"
      },
      errorTip: { // 数据错误提示信息
        type: String,
        default: "未查到相关数据"
      }
    },
    data() {
      return {
        chart: null,
        xIndex: ""
      }
    },
    mounted() {
      const self = this;
      echarts.registerTheme('westeros', themeData);
      this.chart = echarts.init(this.$refs.area, 'westeros');
      this.chart.on('click', function (param) {
        let value = param.name ? param.name.toString() : param.value.toString();
        // 用正则表达式去除‘2018年06月23日’的中文只保留数字
        value = value.replace(/[^0-9]/ig, "");
        self.$emit('change-table-data', value);
      });
      window.addEventListener('resize', this.myChartResize, false);
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.myChartResize, false);
      if (this.chart) {
        this.chart.dispose();
      }
    },
    watch: {
      option: function (value) {
        if(value && value!=null){
          this.reflushChart(value);
        }
      }
    },
    methods: {
      // 窗口改变刷新echarts图
      myChartResize() {
        if (this.chart) {
          this.chart.resize();
        }
      },
      // 刷新图表
      reflushChart(option) {
        this.errorSpin = false
        this.initChart(option)
      },
      // 渲染echart
      initChart(option) {
        const self = this;
        this.chart.setOption(this.getEchartOption(option), true);
      },
      // 用于点击界面非节点的时候出发，需要加click事件，这里业务上先不需要。需要的话请放开。
      mouseDown(event) {
        if (event.zrY > 60 && event.zrY < 325) {
          let value = this.xIndex.toString()
          this.$emit('change-table-data', value)
        }
      },
      // 获得option选项
      getEchartOption(option) {
        var vm = this;
        var labelOption = {
            normal: {
                show: true,
                position: 'top',
            }
        }
        return {
            color: ['#3399cc', '#ff99cc', '#66ffcc', '#e5323e'],
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                  type: 'shadow'
              }
          },
          legend: {
              data: option.legend || ""
          },
          grid: {
              left: 15,
              right: 0,
              bottom: 0,
              top: 40,
              containLabel: true
          },
          calculable: true,
          xAxis: [
              {
                  type: 'category',
                  axisTick: {show: false},
                  data: option.xAxis.data
              }
          ],
          yAxis: [
              {
                  type: 'value'
              }
          ],
          series: option.series
          };
        }
      }
  }
</script>

<style scoped="true">
  .chart {
    border-radius: 4px;
    position: relative;
  }

  .bedroom {
    height: 350px;
    margin: 25px 40px;
  }
</style>
