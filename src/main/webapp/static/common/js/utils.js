/**
 * 深拷贝
 * @param obj
 * @returns {any}
 */
function deepClone(obj) {
	return JSON.parse(JSON.stringify(obj))
}

/**
 * 关闭窗体
 * @param {Object} name 窗体名称
 */
function winClose(name) {
	plus.webview.close(name);
}

/**
 * 下划线转换驼峰
 * @param {Object} str 目标字符串
 */
function toHump(str) {
	return str.replace(/\_(\w)/g, function(all, letter) {
		return letter.toUpperCase();
	});
}
/**
 * 驼峰转换下划线
 * @param {Object} str 目标字符串
 */
function toLine(str) {
	return str.replace(/([A-Z])/g, "_$1").toLowerCase();
}

/**
 * 当前时间
 */
function getCurrentDate(len = 19) {
	const date = new Date();
	const year = date.getFullYear();
	let month = date.getMonth() + 1;
	let day = date.getDate();
	let hours = date.getHours();
	let minute = date.getMinutes();
	let second = date.getSeconds();

	month = month < 10 ? '0' + month : month;
	day = day < 10 ? '0' + day : day;
	hours = hours < 10 ? '0' + hours : hours;
	minute = minute < 10 ? '0' + minute : minute;
	second = second < 10 ? '0' + second : second;
	const dataStr = `${year}-${month}-${day} ${hours}:${minute}:${second}`;
	return dataStr.substr(0, len);
};

//保留两位小数(不四舍五入)，向下取偶数
function fomatFloat(num) {
	num = num.toFixed(3);
	num = num.substr(0, num.length - 1);
	var lastStr = num.charAt(num.length - 1);
	if (lastStr % 2 == 0) {
		return num;
	} else {
		return (num - 0.01).toFixed(2);
	}
}

/**
 * 保留小数不四舍五入
 * @param {Object} num	数字
 * @param {Object} decimal	保留位数
 */
function toFixed(num, decimal) {
	num = num.toString();
	let index = num.indexOf('.');
	if (index !== -1) {
		num = num.substring(0, decimal + index + 1)
	} else {
		num = num.substring(0)
	}
	return Number(parseFloat(num).toFixed(decimal));
}

// wgs转gcj
function transGcjLngLat(lng, lat) {
	let point = coordtransform.wgs84togcj02(lng, lat);
	let lngLat = {
		lng: point[0],
		lat: point[1]
	}
	return lngLat;
}

// gcj转wgs
function transWgsLngLat(lng, lat) {
	let point = coordtransform.gcj02towgs84(lng, lat);
	let lngLat = {
		lng: point[0],
		lat: point[1]
	}
	return lngLat;
}

//坐标距离计算
function getDistance(lat1, lng1, lat2, lng2) {
	var radLat1 = lat1 * Math.PI / 180.0;
	var radLat2 = lat2 * Math.PI / 180.0;
	var a = radLat1 - radLat2;
	var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(
		b / 2), 2)));
	s = s * 6378.137;
	s = (Math.round(s * 10000) / 10000) * 1000;
	return s;
}

/**
 * 两点坐标角度
 * @param {Object} currentLatLng 坐标-1
 * @param {Object} targetLatLng	坐标-2
 */
function getAngleCalc(currentLatLng, targetLatLng) {
	let currentLng = currentLatLng.lng;
	let currentLat = currentLatLng.lat;
	let targetLng = targetLatLng.lng;
	let targetLat = targetLatLng.lat;
	let a = (90 - targetLat) * Math.PI / 180;
	let b = (90 - currentLat) * Math.PI / 180;
	let AOC_BOC = (targetLng - currentLng) * Math.PI / 180;
	let cosc = Math.cos(a) * Math.cos(b) + Math.sin(a) * Math.sin(b) * Math.cos(AOC_BOC);
	let sinc = Math.sqrt(1 - cosc * cosc);
	let sinA = Math.sin(a) * Math.sin(AOC_BOC) / sinc;
	let A = Math.asin(sinA) * 180 / Math.PI;
	let angle = 0;
	if (targetLng > currentLng && targetLat > currentLat) angle = A;
	else if (targetLng > currentLng && targetLat < currentLat) angle = 180 - A;
	else if (targetLng < currentLng && targetLat < currentLat) angle = 180 - A;
	else if (targetLng < currentLng && targetLat > currentLat) angle = 360 + A;
	else if (targetLng > currentLng && targetLat == currentLat) angle = 90;
	else if (targetLng < currentLng && targetLat == currentLat) angle = 270;
	else if (targetLng == currentLng && targetLat > currentLat) angle = 0;
	else if (targetLng == currentLng && targetLat < currentLat) angle = 180;
	return angle;
}
