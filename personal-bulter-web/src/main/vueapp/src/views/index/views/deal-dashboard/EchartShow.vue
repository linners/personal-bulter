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
        type: Object
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
          this.reflushChart(value)
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
        return {
          title: {
            text: option.title || "",
            subtext: option.subTitle || ""
          },
          tooltip: {
            trigger: 'axis',
            backgroundColor: '#fff',
            borderWidth: 1,
            borderColor: '#C3CBD6',
            axisPointer: {
              lineStyle: {
                color: '#C3CBD6',
              },
            },
            padding: [5, 10],
            textStyle: {
              color: '#495060',
              fontFamily: 'Microsoft YaHei',
              fontSize: 12
            },
            formatter: (params, ticket, callback) => {
              vm.xIndex = params[0].name
              //x轴名称
              var name = params[0].name
              var showContent = "";
              params.forEach((obj, index, array) => {
                var seriesName = obj.seriesName
                var value = obj.value
                var marker = obj.marker;
                showContent = showContent + '<div style="padding: 5px 10px;color:#495060">' + marker + '<span>' + seriesName + '</span>&nbsp;&nbsp;<span style="float: right">' + value + '</span></div>'
              })
              return showContent;
            }
          },
          legend: {
            type: 'scroll',
            data: option.legend.data || "",
            width: '70%',
            itemWidth: 16,
            itemHeight: 3,
            itemGap: 12,
            icon: 'roundRect',
            textStyle: {
              color: '#464c5b',
            },
            bottom: 0
          },
          grid: {
            left: 20,
            right: 0,
            bottom: 45,
            top: 30,
            containLabel: true
          },
          // toolbox: {
          //   feature: {
          //     magicType: {show: true, type: ['line', 'bar']},
          //     saveAsImage: {
          //       show: this.hasAuth
          //     },
          //   }
          // },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#D7DDE4',
              },
            },
            axisTick: {
              alignWithLabel: true,
              lineStyle: {
                color: '#D7DDE4',
              },
            },
            axisLabel: {
              textStyle: {
                color: '#7F8B9C',
              },
              interval:0,
              rotate: option.xAxis.data.length<15?0:40
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#F5F7F9',
              },
            },
            triggerEvent: true,
            data: option.xAxis.data
          },
          yAxis: option.yAxis,
          series: option.series
        }
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
