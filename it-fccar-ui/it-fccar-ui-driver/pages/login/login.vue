<template>
	<view>
		<image src="../../static/login/top3.png" mode="widthFix" class="top"></image>
		<image src="../../static/login/logo.jpg" mode="widthFix" class="logo"></image>
		<view class="desc">
			<text class="name">专业代驾 服务第一</text>
		</view>
		<button class="btn" @tap="wxLogin()">司机登陆</button>
		<view class="register-container">
			没有账号?
			<text class="link" @tap="toRegisterPage()">立即注册</text>
		</view>
		<text class="remark">小程序仅限于飞驰专车代驾使用,登录前请先进行微信注册,注册前请消息阅读注册需求和使用流程</text>
		<u-toast ref="uToast" />
	</view>
</template>

<script>


export default {
	data() {
		return {};
	},
	methods: {
		//司机注册
		toRegisterPage(){
			uni.navigateTo({
				url:"/pages/register/register"
			})
		},
		//司机登录
		wxLogin(){
			let _this = this;
				wx.login({//登录
				success (res) {//成功
					if (res.code) {
						//拿到授权码--要用json格式传输后端接收
						let param = {
							wxCode : res.code,
							type : 1
						}
						//发起网络请求
						_this.post(_this.Consts.API.APP_LOGIN,param,(res)=>{ 
							let {message,data,code,success} = res.data;
							//成功
							if(success){
								uni.setStorageSync("username",data.username);
								uni.setStorageSync("phone",data.phone);
								uni.setStorageSync("avatar",data.avatar);
								uni.setStorageSync("satoken",data.satoken);
								//工作台的跳转
								uni.switchTab({
									url:"/pages/workbench/workbench"
								})
							}else{
								uni.showToast({
									title:message,icon:"error"
								})
							}
						})
					} else {
						uni.showToast({
							title:"登录失败",icon:"error"
						})
					}
				}
			})
		}
	},
	onLoad: function() {
		
	},
}
</script>

<style lang="less">
@import url('login.less');
</style>
