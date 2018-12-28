export default {
	/**
	 *  获得当前所属的年
	 */
	getCurYear() {
		var date = new Date()
		return date.getFullYear()
	},
	getCurMonth() {
		var date = new Date()
		return date.getMonth() + 1
	},
	/**
	 * 计算某一天在一年中的总毫秒数（例如：2017-06-01 00:00:00距离2017-01-01 00:00:00之间的毫秒数）
	 */
	getDayTotalTimes(time) {
		return Date.parse(new Date(time.replace(/-/g, "/")))
	},
	/**
	 *  计算当前日期在本年度的周数
	 *  @param {Date} date -日期
	 *  @param {Number} weekStart - 每周开始于周几：周日：0，周一：1，周二：2 ...，默认为周日
	 *  返回周为两位，不够补0
	 */
	getWeekOfYear(date, weekStart) {
		weekStart = (weekStart || 0) - 0;
		if (isNaN(weekStart) || weekStart > 6)
			weekStart = 0;
		var year = date.getFullYear();
		var firstDay = new Date(year, 0, 1);
		var firstWeekDays = 7 - firstDay.getDay() + weekStart;
		var dayOfYear = (((new Date(year, date.getMonth(), date.getDate())) - firstDay) / (24 * 3600 * 1000)) + 1;
		var week = Math.ceil((dayOfYear - firstWeekDays) / 7) + 1;
		if (week.toString().length < 2) {
			week = "0" + week;
		}
		return week;
	},
	/**
	 *  计算当前日期在本月份的周数
	 *  @param {Date} date -日期
	 *  @param {Number} weekStart - 每周开始于周几：周日：0，周一：1，周二：2 ...，默认为周日
	 */
	getWeekOfMonth(date, weekStart) {
		weekStart = (weekStart || 0) - 0;
		if (isNaN(weekStart) || weekStart > 6)
			weekStart = 0;
		var dayOfWeek = date.getDay();
		var day = date.getDate();
		return Math.ceil((day - dayOfWeek - 1) / 7) + ((dayOfWeek >= weekStart) ? 1 : 0);
	},
	/**
	 * 获得一年有多少周
	 * @param {Number} year - 年份，例如：2017
	 */
	getTotalWeeksByYear(year) {
		var date = new Date(year, 0, 1)
		var yt = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? 366 : 365
		return Math.ceil((yt - date.getDay()) / 7)
	},
	/**
	 * 格式化时间，将时间 'Sat Jul 01 2017 08:00:00 GMT+0800 (中国标准时间)' 格式化为：'2017-07-01'
	 * @param {Date} date - 时间
	 */
	formatDate(date) {
		var d = new Date(date)
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		m = m < 10 ? '0' + m : m;
		var d = d.getDate();
		d = d < 10 ? ('0' + d) : d;
		return y + '-' + m + '-' + d;
	},
	/**
	 * 格式化时间，将时间 'Sat Jul 01 2017 08:00:00 GMT+0800 (中国标准时间)' 格式化为：'2017-07-01'
	 * @param {Date} date - 时间
	 */
	formatDateForMonth(date) {
		var d = new Date(date)
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		m = m < 10 ? '0' + m : m;
		var d = d.getDate();
		d = d < 10 ? ('0' + d) : d;
		return m + '.' + d;
	},
	/**
	 *  最近一段时间段
	 *  @param {Number} type - 类型，0：周 1：月
	 *  @param {Number} monthNum - 月数量，仅当type = 1有效
	 *  @return {Array} - [开始时间，结束时间]
	 */
	curRangeDays(type, monthnum) {
		let count = 7
		let monthm = monthnum || 1
		if (type && type == 1) {
			count = monthm * 30
		}
		const end = new Date();
		end.setTime(end.getTime() - 3600 * 1000 * 24)
		const start = new Date();
		start.setTime(start.getTime() - 3600 * 1000 * 24 * count);
		return [start, end];
	},
	/**
	 * 通过周数和星期计算日期
	 * 例如：2017年第23周，第一天对应的日期，
	 * 默认：每周 周日是一周的开始
	 * @param {Number} year - 年，例如：2017
	 * @param {Number} week - 第几周，例如：23周
	 * @param {Number} week - 一周第几天，0：周日 ，1：周一 ... 6: 周六
	 */
	dateFromWeek(year, week, dayIndex) {
		var date1 = new Date(year, 0, 1)
		var dayMS = 24 * 60 * 60 * 1000   // 每天毫秒数
		var firstDay = (7 - date1.getDay()) * dayMS
		var weekMS = (week - 2) * 7 * dayMS
		var result = date1.getTime() + firstDay + weekMS + dayIndex * dayMS
		date1.setTime(result)
		return this.formatDateForMonth(date1)
	},
	/**
	 * 通过当前时间得到当前时间周的起始时间
	 * @param date - 当前时间
	 * @param weekStart - 一周第几天，0：周日 ，1：周一 ... 6: 周六
	 * @returns {Date}
	 */
	getBeginDayOfWeek(date, weekStart) {
		return new Date(date.getFullYear(), date.getMonth(), date.getDate() + weekStart - date.getDay());
	},
	/**
	 * 通过当前时间获取当天0点0分0秒的起始时间
	 * @param date - 当前时间
	 */
	getBeginDayTime(date) {
		return new Date(date.getFullYear(), date.getMonth(), date.getDate());
	},
	/**
	 * 通过当前时间得到yyyyWW样式的周结果
	 * @param date
	 */
	getFormatWeek(date) {
		return date.getFullYear().toString() + this.getWeekOfYear(date, 0).toString();
	},
	/**
	 * 通过当前时间得到当月第一天的时间对象
	 * @param date
	 * @returns {Date}
	 */
	getBeginDayOfMonth(date) {
		return new Date(date.getFullYear(),date.getMonth(),1);
	},
    /**
     * 格式化日期得到yyyyMMdd格式的日期
     * @param date
     * @returns {number}
     */
    formatDateToString(date) {
        let temp_year = date.getFullYear();
        let temp_month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
        let temp_day = (date.getDate()) > 9 ? (date.getDate()) : "0" + (date.getDate());
        return temp_year.toString() + temp_month.toString() + temp_day.toString();
    },
    /**
	 * 获得某个日期前一天的yyyyMMdd格式的日期
     * @param date
     * @returns {number}
     */
	getBeforeDay(date){
		return this.formatDateToString(new Date(date.setDate(date.getDate()-1)));
	},
    /**
     * 获得某个日期7天之前的yyyyMMdd格式的日期
     * @param date
     * @returns {number}
     */
    getBefore7Day(date){
        return this.formatDateToString(new Date(date.setDate(date.getDate()-7)));
	},
	/**
	 * @Description: 将yyyyMMdd转为yyyy年MM月dd日；将yyyyWW转为yyyy年 第WW周；将yyyyMM转为yyy年MM月
	 * @param dateKey：被转的时间 	dateType：被转时间的类型 1日 2周 3月
	 */
	formatDateToChenese(dateKey,dateType){
    	let dateString = null;
        if (dateType == 1) {
            dateString = dateKey.substr(0, 4) + '年' + dateKey.substr(4, 2) + '月' + dateKey.substr(6, 2) + '日';
        } else if (dateType == 2) {
            dateString = dateKey.substr(0, 4) + '年 第' + dateKey.substr(4, 2) + '周';
        } else if (dateType == 3) {
            dateString = dateKey.substr(0, 4) + '年' + dateKey.substr(4, 2) + '月';
        }
        return dateString;
	}
}
