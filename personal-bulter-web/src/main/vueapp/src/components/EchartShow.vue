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
  import DateUtil from '../utils/dateutil'
  import echarts from 'echarts';
  import themeData from '../assets/westeros.json'
  import FormatUtil from '../utils/formatutil'

  export default {
		name: 'EchartShow',
		props: {
			hasAuth: {
				type: Boolean,
				default: false
			},
			normType: {
				type: Number,
				default: 0
			},
            normT1Code:{
			    type: String,
				default: ''
			},
			normT2Code:{
			    type: String,
				default: ''
			},
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
				value = value.replace(/[^0-9]/ig,"");
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
				this.reflushChart(value);
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
				this.chart.setOption(self.getEchartOption(option), true);
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
						text: option.title,
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
							var showContentDod = "";
							var unit = ""
                            if (this.normType == 1) {
                                if (this.normT1Code == 'discount_before_order_amount' || this.normT1Code == 'price_per_comprehensive' || this.normT1Code == 'price_per_company') {
                                    unit = '元';
                                }else if (this.normT1Code == 'first_coverage_company' || this.normT1Code == 'secondary_coverage_company') {
									unit = '%';
								}else if (this.normT1Code == 'sale_amount' || this.normT1Code == 'order_company_quantity') {
                                    unit = '个';
								}else if (this.normT1Code == 'sale_weight' || this.normT1Code == 'price_per_weight') {
                                    unit = '斤';
								}
                            } else if (this.normType == 2){
                                if (this.normT2Code == 'nopop_income_total' || this.normT2Code == 'sale_notax_total' || this.normT2Code == 'nopop_profit_total' || this.normT2Code == 'price_per_company' || this.normT2Code == 'price_per_comprehensive') {
                                    unit = '元';
                                }else if (this.normT2Code == 'first_coverage_company' || this.normT2Code == 'secondary_coverage_company' || this.normT2Code == 'nopop_profit_ratio') {
                                    unit = '%';
                                }else if (this.normT2Code == 'sale_amount' || this.normT2Code == 'transport_company_quantity') {
                                    unit = '个';
                                }else if (this.normT2Code == 'sale_weight' || this.normT2Code == 'price_per_weight') {
                                    unit = '斤';
                                }
							}
							let weekRangeDay = ""
							if (name && name.indexOf('周') > 0) {
								let year = name.substr(0, 4);
								let week = name.substring(name.indexOf('年') + 1, name.indexOf('周'));
								let weekBeginDay = DateUtil.dateFromWeek(year, week, 0)
								let weekEndDay = DateUtil.dateFromWeek(year, week, 6)
								weekRangeDay = "(" + weekBeginDay + " - " + weekEndDay + ")"
							}
							params.forEach((obj, index, array) => {
								var seriesName = obj.seriesName
								var value = obj.value
								if (this.normType == 1 || this.normType == 2 || this.normType == 3 || this.normType == 4) {
									value = FormatUtil.formatCurrency(value)
								}
								var marker = obj.marker;
								// 自适应
								if (seriesName.indexOf("环比") >= 0 || seriesName.indexOf("同比") >= 0) {
									showContentDod = showContentDod + '<div style="padding: 5px 10px;color:#495060">' + marker + '<span>' + seriesName + '</span>&nbsp;&nbsp;<span style="float: right">' + value + '&nbsp;' + "%" + '</span></div>'
								} else {
									showContent = showContent + '<div style="padding: 5px 10px;color:#495060">' + marker + '<span>' + seriesName + '</span>&nbsp;&nbsp;<span style="float: right">' + value + '&nbsp;' + unit + '</span></div>'
								}
							})
							return "<p style='padding:4px 10px 6px 10px;font-weight:bold;'>" + name + weekRangeDay + "</p>" + showContent + showContentDod;
						}
					},
					legend: {
						type: 'scroll',
						data: option.legend.data,
						width: '70%',
						itemWidth: 16,
						itemHeight: 3,
						itemGap: 12,
						icon: 'roundRect',
						textStyle: {
							color: '#464c5b',
						},
					},
					grid: {
						left: 0,
						right: '3%',
						bottom: 5,
						top: 60,
						containLabel: true
					},
					toolbox: {
						feature: {
							magicType: {show: true, type: ['line', 'bar']},
							saveAsImage: {
								show: this.hasAuth
							},
							// 添加环比按钮
							myTool1: {
								show: option.toolbox.feature.myTool1.show,
								title: option.toolbox.feature.myTool1.title,
								icon: 'path://M1092.266667 34.133333C828.7744 34.133333 614.4 248.507733 614.4 512 614.4 775.492267 828.7744 989.866667 1092.266667 989.866667 1355.758933 989.866667 1570.133333 775.492267 1570.133333 512 1570.133333 248.507733 1355.758933 34.133333 1092.266667 34.133333M1092.266667 1024C809.949867 1024 580.266667 794.3168 580.266667 512 580.266667 229.6832 809.949867 0 1092.266667 0 1374.583467 0 1604.266667 229.6832 1604.266667 512 1604.266667 794.3168 1374.583467 1024 1092.266667 1024 M512 34.133333C248.507733 34.133333 34.133333 248.507733 34.133333 512 34.133333 775.492267 248.507733 989.866667 512 989.866667 775.492267 989.866667 989.866667 775.492267 989.866667 512 989.866667 248.507733 775.492267 34.133333 512 34.133333M512 1024C229.6832 1024 0 794.3168 0 512 0 229.6832 229.6832 0 512 0 794.3168 0 1024 229.6832 1024 512 1024 794.3168 794.3168 1024 512 1024',
								onclick: function () {
									//触发clickLR事件，告诉父组件环比按钮被点击了
									vm.$emit('click-link-relative');
								}
							}
						}
					},
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
							// interval:0,
							// rotate:40
						},
						splitLine: {
							show: true,
							lineStyle: {
								color: '#F5F7F9',
							},
						},
						triggerEvent: true,
						data: option.xAxis.data
					},
					yAxis: [{
						type: 'value'
					}, {
						show: option.yAxis[1].show,
						type: 'value'
					}],
					series: option.series
				}
			}
		}
	}
</script>

<style scoped="true">
	.chart {
		height: 400px;
		border: 1px solid #d8dfe6;
		border-radius: 4px;
		position: relative;
	}

	.bedroom {
		height: 350px;
		margin: 25px 40px;
	}
</style>
