import {
	createApp
} from 'vue'

import App from './App.vue'

//导入Svg图片插件，可以在页面上显示Svg图片
import 'vite-plugin-svg-icons/register';

//导入JQuery库，因为Ajax用起来非常方便，支持同步和异步的Ajax请求
import $ from 'jquery';


const app = createApp(App) //创建VUE对象

//导入路由配置
import router from './router'

app.use(router) //挂载路由插件

//导入Cookie库，可以读写Cookie数据
import VueCookies from 'vue3-cookies'
app.use(VueCookies); //挂载Cookie插件

//导入ElementUI
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import locale from 'element-plus/lib/locale/lang/zh-CN'

//导入ElementUI的消息通知组件，下面封装全局Ajax的时候处理异常的时候需要弹出通知
import { ElMessage } from 'element-plus'

//挂载ElementUl-Plus插件
app.use(ElementPlus, {
	locale
})

//导入echarts库
import * as echarts from 'echarts'

//设置全局变量$echarts
app.config.globalProperties.$echarts = echarts

//配置JS生成PDF的公共函数
import html2Canvas from 'html2canvas'
import JsPDF from 'jspdf'

app.config.globalProperties.getPdf = function() {
	var title = this.htmlTitle //PDF标题
	html2Canvas(document.querySelector('#pdfDom'), {
		allowTaint: true,
		taintTest: false,
		useCORS: true,
		//width:960,
		//height:5072,
		dpi: window.devicePixelRatio * 4, //将分辨率提高到特定的DPI 提高四倍
		scale: 4 //按比例增加分辨率
	}).then(function(canvas) {
		let contentWidth = canvas.width
		let contentHeight = canvas.height
		let pageHeight = contentWidth / 592.28 * 841.89
		let leftHeight = contentHeight
		let position = 0
		let imgWidth = 595.28
		let imgHeight = 592.28 / contentWidth * contentHeight
		let pageData = canvas.toDataURL('image/jpeg', 1.0)
		let PDF = new JsPDF('', 'pt', 'a4')
		if (leftHeight < pageHeight) {
			PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
		} else {
			while (leftHeight > 0) {
				PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
				leftHeight -= pageHeight
				position -= 841.89
				if (leftHeight > 0) {
					PDF.addPage()
				}
			}
		}
		PDF.save(title + '.pdf')
	})
}

//请求工具类
import request from './utils/request'


function sendRequest(url,method,data,fun){
	request({
		url:url,
		method: method,
		data:data
	}).then(res=>{
		if(fun)(fun(res));
	}).catch(error=>{
		console.log(error);
		ElMessage.error({ message: "服务器异常["+error.message+"]", type:"error",duration: 1200});
	})
}


app.config.globalProperties.post = function(url,  data, fun) {

	let sendData = typeof data == "function"?{}:data;

	let callBack = typeof data == "function"?data:fun;

	sendRequest(url,"post",sendData,callBack);
}

app.config.globalProperties.get = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"get",sendData,callBack);
}
app.config.globalProperties.del = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"delete",sendData,callBack);
}
app.config.globalProperties.put = function(url,  data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,"put",sendData,callBack);
}

app.config.globalProperties.request = function(url, method, data, fun) {
	let sendData = typeof data == "function"?{}:data;
	let callBack = typeof data == "function"?data:fun;
	sendRequest(url,method,sendData,callBack);
}

//封装用于判断用户是否具有某些权限的公共函数
app.config.globalProperties.isAuth = function(permission) {
	console.log("权限校验：",permission);
	//判断是否是管理员
	let login = localStorage.getItem("login");
	let objLogin = JSON.parse(login);
	if (objLogin.admin)return true;
	//判断权限
	let permissions = localStorage.getItem("permissions");
	let flag = false
	for (let one of permission) {
		if (permissions.includes(one)) {
			flag = true
			break;
		}
	}
	return flag;
}
app.mount('#app')
