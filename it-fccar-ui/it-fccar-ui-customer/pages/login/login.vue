<template>
	<view>
		<image src="../../static/login/top.png" mode="widthFix" class="top"></image>
		<image src="../../static/login/logo.jpg" mode="widthFix" class="logo"></image>
		<view class="desc">
			<text class="name">有我在您可放心畅饮</text>
		</view>
		<button class="btn" @tap="subscribeMessage()">乘客登陆</button>
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
		return {
			code: null
		};
	},
	methods: {
		subscribeMessage(){
			let _this = this;
			wx.login({
			  success (res) {
			    if (res.code) {
					var params = {
						wxCode:res.code,
						type:2
					}
			      //向后台传递临时登录凭证code
				  _this.post("/uaa/app/mp/login",params,(res)=>{
					  console.log(res.data.data.satoken)
					  if(res.data.success){
						 uni.setStorageSync("satoken",res.data.data.satoken);
						 uni.setStorageSync("avatar",res.data.data.avatar);
						 uni.setStorageSync("nickName",res.data.data.nickName);
						 uni.setStorageSync("phone",res.data.data.phone);
						 uni.showToast({
						  	title:"登陆成功!",
							icon:"success"
						  })
						  setTimeout(()=>{
							  uni.switchTab({
							  	url:"/pages/workbench/workbench"
							  })
						  },1500)
					  }else {
						  console.log(res.data)
						  uni.showToast({
							title:res.data.message,
							icon:"error"
						  })
						}
				  })
			    }
			  }
			})
		},
		toRegisterPage(){
			uni.navigateTo({
				url:"/pages/register/register"
			})
		}
	},
	onLoad: function() {}
};
</script>

<style lang="less">
@import url('login.less');
</style>
