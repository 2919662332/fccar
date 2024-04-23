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

Vue.prototype.getui = {
	"appid":"PpBUrcNGiL9fnExoJeCsQ5",
}

Vue.prototype.msgTempIds = ['RDavpQwtw2CJcBGZikSu89m4GrFN6gqsiEiEicIuupA']
// #endif


Vue.prototype.Consts = {
	QQMAP_KEY: "2WQBZ-RKYCZ-JOAX7-7W5UH-QZGQV-MYB5Y", //腾讯地图的秘钥
	API:{
		CUSTOMER_REGISTER:"/customer/app/customer/register",	//乘客注册
		APP_LOGIN: "/uaa/app/mp/login"	,		//app微信登录
	},
	GtPush:{
		appid: "Qb5XleyFh67nRidkiR9xz",
		msg_type_pay:0
	},
	OrderStatus:{
		ORDER_STATUS_WAIT : 0,
		ORDER_STATUS_ACCEPTED : 1,
		ORDER_STATUS_ARRIVE : 2,
		ORDER_STATUS_START_DRIVING : 3,
		ORDER_STATUS_COMPLETE_DRIVED : 4,
		ORDER_STATUS_ENSURE : 5,
		ORDER_STATUS_NOT_PAY : 6,
		ORDER_STATUS_PAYED : 7,
		ORDER_STATUS_FINISH_ORDER : 8,
		ORDER_STATUS_CUSTOMER_CANCEL_ORDER : 9,
		ORDER_STATUS_DRIVER_CANCEL_ORDER : 10,
		ORDER_STATUS_ACCIDENT_CLOSE : 11,
		ORDER_STATUS_OTHER : 12,
	}
	

}


let baseUrl = "http://127.0.0.1:10010/fccar"

function sendRequest(url,method,sendData,callBack){
	
	//uni.showLoading({ title: "请求中..." })
	
	uni.request({
		"url": baseUrl+url,
		"method": method,
		"timeout:":10000,
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
				uni.showToast({
					icon: "none",
					title: resp.data.message
				})
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


Vue.prototype.refreshMessage = function(that) {
    uni.request({
        "url": that.url.refreshMessage,
        "method": "POST",
        "header": {
            token: uni.getStorageSync("token")
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

Vue.prototype.uploadCos = function(url, path, module, fun) {
    uni.uploadFile({
        url: url,
        filePath: path,
        name: "file",
        header: {
            token: uni.getStorageSync("token")
        },
        formData: {
            "module": module
        },
        success: function(resp) {
            let data = JSON.parse(resp.data)
            if (resp.statusCode == 401) {
                uni.redirectTo({
                    url: "/pages/login/login.vue"
                })
            } else if (resp.statusCode == 200 && data.code == 200) {
                fun(resp)
            } else {
                uni.showToast({
                    icon: "none",
                    title: data.error
                })
            }
        }
    })
}

Vue.prototype.upload = function(url, path, data, fun) {
    uni.uploadFile({
        url: url,
        filePath: path,
        name: "file",
        header: {
            token: uni.getStorageSync("token")
        },
        formData: data,
        success: function(resp) {
            let data = JSON.parse(resp.data)
            if (resp.statusCode == 401) {
                uni.redirectTo({
                    url: "/pages/login/login.vue"
                })
            } else if (resp.statusCode == 200 && data.code == 200) {
                fun(resp)
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
        return true
    }
    return false
}

Vue.prototype.checkBlank = function(data, name) {
    if (data == null || data == "") {
        this.$refs.uToast.show({
            title: name + "不能为空",
            type: 'error'
        })
        return true
    }
    return false
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


Vue.prototype.checkValidCarType = function(data, name) {
    if (data == null || data == "") {
        this.$refs.uToast.show({
            title: name + "不能为空",
            type: 'error'
        })
        return false
    } else if (!/^[\u4e00-\u9fa5A-Za-z0-9\-\_\s]{2,20}$/.test(data)) {
        this.$refs.uToast.show({
            title: name + "不正确",
            type: 'error'
        })
        return false
    }
    return true
}

Vue.prototype.checkValidCarPlate = function(data, name) {
    if (data == null || data == "") {
        this.$refs.uToast.show({
            title: name + "不能为空",
            type: 'error'
        })
        return false
    } else if (!
        /^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$/
        .test(data)) {
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
