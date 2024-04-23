import App from './App'


// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
	...App
})

//注册uView组件
import uView from 'uview-ui';
Vue.use(uView);


app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif


//==请求开始============================================================================
Vue.prototype.Consts = {
	QQMAP_KEY: "2WQBZ-RKYCZ-JOAX7-7W5UH-QZGQV-MYB5Y", //腾讯地图的秘钥
	//GPNBZ-PBC6B-HFZUF-NRV6Q-BHZ7V-SGB6K
	//2WQBZ-RKYCZ-JOAX7-7W5UH-QZGQV-MYB5Y
	API:{
		DRIVER_REGISTER:"/driver/app/driver/register",	//司机注册
		APP_LOGIN: "/uaa/app/mp/login"	,		//app微信登录
	},
	WORK_STATUS:{ //司机的工作状态
		STOP_ACCPET_ORDER:"停止接单",
		START_ACCPET_ORDER:"开始接单",
		RECEPTION_CUSTOMER:"接客户",
		ARRIVE_START_PLACE:"到达代驾点",
		START_DRIVING: "开始代驾",
		END_DRIVING: "结束代驾",
	},
	
	//  0等待接单，1已接单，2司机已到达，3开始代驾，4结束代驾，5.司机确认费用，6未付款，
	//  7已付款，8订单已结束，9顾客撤单，10司机撤单，11事故关闭，12其他
	OrderStatus:{ //订单的状态
		ORDER_STATUS_WAIT : 0,
		ORDER_STATUS_ACCEPTED : 1,		//对应工作状态的 ：接客户
		ORDER_STATUS_ARRIVE : 2,		//对应工作状态的：到达代驾点
		ORDER_STATUS_START_DRIVING : 3,	//对应工作状态的：开始代驾
		ORDER_STATUS_COMPLETE_DRIVED : 4,	//结束代驾-应该跳转 确认费用页
		ORDER_STATUS_ENSURE : 5,	//司机已经确认费用 应该跳转 发送账单页
		ORDER_STATUS_NOT_PAY : 6,	//账单已经发送 跳转订单详情页
		ORDER_STATUS_PAYED : 7,
		ORDER_STATUS_FINISH_ORDER : 8,
		ORDER_STATUS_CUSTOMER_CANCEL_ORDER : 9,
		ORDER_STATUS_DRIVER_CANCEL_ORDER : 10,
		ORDER_STATUS_ACCIDENT_CLOSE : 11,
		ORDER_STATUS_OTHER : 12,
	}
	

}


Vue.prototype.msgTempIds = ['RDavpQwtw2CJcBGZikSu89m4GrFN6gqsiEiEicIuupA']



let baseUrl = "http://127.0.0.1:10010/fccar"

function sendRequest(url,method,sendData,callBack){
	
	//uni.showLoading({ title: "请求中..." })

	
	uni.request({
		"url": baseUrl+url,
		"method": method,
		"header": {
			satoken: uni.getStorageSync("satoken")
		},
		"data": sendData,
		success: function(resp) {
			if (resp.statusCode == 401) {
				uni.redirectTo({
					url: "/pages/login/login.vue"
				})
			} else if (resp.statusCode == 200 && resp.data.code == 200) {
				
				let data = resp.data
				if (data.hasOwnProperty("token")) {
					let token = data.token
					uni.setStorageSync("token", token)
				}
				
				if(callBack){
					callBack(resp)
				}

			} else {
				console.log("请求结果异常：",resp.data.message);
				uni.showToast({
					icon: "none",
					title: "请求失败["+resp.data.message+"]"
				})
				if(callBack){
					callBack(resp)
				}
			}
		},
		complete() {
			setTimeout(function(){
				uni.hideLoading()
			},1000)
		},
		fail: function(error) {
			uni.showToast({
				icon: "none",
				title: "请求错误["+error.errMsg+"]"
			})
		}
	})
}


Vue.prototype.post = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"post",sendData,callBack);
}
Vue.prototype.get = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"get",sendData,callBack);
}
Vue.prototype.del = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"del",sendData,callBack);
}
Vue.prototype.put = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"put",sendData,callBack);
}

Vue.prototype.request= function(url, method, data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,method,sendData,callBack);
}

//==请求结束============================================================================


Vue.prototype.refreshMessage = function(that) {
	uni.request({
		"url": that.url.refreshMessage,
		"method": "POST",
		"header": {
			token: uni.getStorageSync("satoken")
		},
		"data": {
			identity: 'driver'
		},
		success: function(resp) {

			if (resp.statusCode == 401) {
				uni.redirectTo({
					url: "/pages/login/login.vue"
				})
			} else if (resp.statusCode == 200 && resp.data.code == 200) {
				uni.$emit("updateMessageService", true)
				let result = resp.data.result
				let lastRows = result.lastRows
				let unreadRows = result.unreadRows
				if (lastRows > 0) {
					uni.$emit("showMessageTip", lastRows)
				}
			} else {
				//在工作台页面触发更新消息服务状态，显示服务可用或者不可用
				uni.$emit("updateMessageService", false)
			}
		},
		fail: function(error) {
			//在工作台页面触发更新消息服务状态，显示服务可用或者不可用
			uni.$emit("updateMessageService", false)
		}
	})
}

/**
 * @param {Object} url ：后端的地址
 * @param {Object} path ：上传的文件
 * @param {Object} data ：扩展数据
 * @param {Object} fun ：回调
 */
Vue.prototype.upload = function(url, path, data, fun) {
	uni.uploadFile({
		url: baseUrl+url,
		filePath: path,
		name: "file",
		header: {
			satoken: uni.getStorageSync("satoken")
		},
		formData: data,
		success: function(resp) {
	
			let data = JSON.parse(resp.data)
		
			if (resp.statusCode == 401) {
				//没登录
				uni.redirectTo({
					url: "/pages/login/login.vue"
				})
			} else if (resp.statusCode == 200 && data.code == 200) {
				fun(data)
			} else {
				uni.showToast({
					icon: "none",
					title: data.error
				})
			}
		}
	})
}

Vue.prototype.toPage = function(url) {
	uni.navigateTo({
		url: url
	})
}


Vue.prototype.checkNull = function(data, name) {
	if (data == null) {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	}
	return true
}

Vue.prototype.checkBlank = function(data, name) {
	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	}
	return true
}

Vue.prototype.checkValidName = function(data, name) {
	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	} else if (!/^[\u4e00-\u9fa5]{2,15}$/.test(data)) {
		this.$refs.uToast.show({
			title: name + "不正确",
			type: 'error'
		})
		return false
	}
	return true
}
Vue.prototype.checkValidTel = function(data, name) {
	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	} else if (!/^1[0-9]{10}$/.test(data)) {
		this.$refs.uToast.show({
			title: name + "不正确",
			type: 'error'
		})
		return false
	}
	return true
}
Vue.prototype.checkValidEmail = function(data, name) {
	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	} else if (!/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(data)) {
		this.$refs.uToast.show({
			title: name + "不正确",
			type: 'error'
		})
		return false
	}
	return true
}

Vue.prototype.checkValidAddress = function(data, name) {

	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	} else if (!/^[0-9a-zA-Z\u4e00-\u9fa5\-]{6,50}$/.test(data)) {
		this.$refs.uToast.show({
			title: name + "不正确",
			type: 'error'
		})
		return false
	}
	return true
}

Vue.prototype.checkValidFee = function(data, name) {

	if (data == null || data == "") {
		this.$refs.uToast.show({
			title: name + "不能为空",
			type: 'error'
		})
		return false
	} else if (!/^[1-9]\d*\.\d{1,2}$|^0\.\d{1,2}$|^[1-9]\d*$/.test(data)) {
		this.$refs.uToast.show({
			title: name + "不正确",
			type: 'error'
		})
		return false
	}
	return true
}


Vue.prototype.changeNumber = function(value) {
	let newValue = ['', ''];
	let fr = 1000;
	const ad = 1;
	let num = 3;
	const fm = 1;
	while (value / fr >= 1) {
		fr *= 10;
		num += 1;
	}
	if (num <= 4) {
		// 千
		newValue[1] = '千';
		newValue[0] = parseInt(value / 1000) + '';
	} else if (num <= 8) {
		// 万
		const text1 = parseInt(num - 4) / 3 > 1 ? '千万' : '万';
		const fm = '万' === text1 ? 10000 : 10000000;
		newValue[1] = text1;
		newValue[0] = value / fm + '';
	} else if (num <= 16) {
		// 亿
		let text1 = (num - 8) / 3 > 1 ? '千亿' : '亿';
		text1 = (num - 8) / 4 > 1 ? '万亿' : text1;
		text1 = (num - 8) / 7 > 1 ? '千万亿' : text1;
		// tslint:disable-next-line:no-shadowed-variable
		let fm = 1;
		if ('亿' === text1) {
			fm = 100000000;
		} else if ('千亿' === text1) {
			fm = 100000000000;
		} else if ('万亿' === text1) {
			fm = 1000000000000;
		} else if ('千万亿' === text1) {
			fm = 1000000000000000;
		}
		newValue[1] = text1;
		newValue[0] = parseInt(value / fm) + '';
	}
	if (value < 1000) {
		newValue[1] = '';
		newValue[0] = value + '';
	}
	let temp = Math.floor((newValue[0]) * 100) / 100
	return temp + newValue[1];
}
