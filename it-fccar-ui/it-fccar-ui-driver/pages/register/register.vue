<template>
	<view class="page">
		<image src="../../static/register/top2.jpg" mode="widthFix" class="top" />
		<view class="location-container">
			<view class="left">
				<image src="../../static/register/location.png" mode="widthFix" class="location"></image>
				<text>代驾服务地点</text>
			</view>
			<view class="right">{{cityName}}</view>
		</view>
		<view class="info-container">
			<view class="title-container">
				<image src="../../static/register/title-bg.png" mode="widthFix" class="title-bg"></image>
				<text class="title">入驻条件</text>
			</view>
			<view class="list">
				<view class="item">
					<text>1.</text>
					三年安全驾驶经验；
				</view>
				<view class="item">
					<text>2.</text>
					年龄23~55周岁；
				</view>
				<view class="item">
					<text>3.</text>
					无违法犯罪记录、无精神病史、无吸毒史，以及平台认为不适合代驾的其他历史证明；
				</view>
				<view class="item">
					<text>4.</text>
					有熟练驾车经验；
				</view>
				<view class="item">
					<text>5.</text>
					身体健康，无肢体残疾和大面积纹身；
				</view>
				<view class="item">
					<text>6.</text>
					需要提供身份证、驾驶证、直系亲属联系方式，并保存前述材料的真实合法性；
				</view>
			</view>
		</view>
		<view class="info-container">
			<view class="title-container">
				<image src="../../static/register/title-bg.png" mode="widthFix" class="title-bg"></image>
				<text class="title">入驻流程</text>
			</view>
			<view class="list">
				<view class="complex-item">
					<view class="left">01</view>
					<view class="right">
						<text class="item-title">在线注册账号</text>
						<text class="item-desc">在小程序完成注册</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">02</view>
					<view class="right">
						<text class="item-title">司机信息填写</text>
						<text class="item-desc">按照流程提交本人身份证、驾驶证</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">03</view>
					<view class="right">
						<text class="item-title">证件资料审核</text>
						<text class="item-desc">对提交证件进行审核</text>
					</view>
				</view>
				<view class="complex-item">
					<view class="left">04</view>
					<view class="right">
						<text class="item-title">签署合同</text>
						<text class="item-desc">收到邮寄的合同后必须本人签署并寄回</text>
					</view>
				</view>
			</view>
		</view>
<!--		<button class="btn" @tap="wxRegister" >司机注册</button>-->
		 <button class="btn" open-type="getPhoneNumber" @getphonenumber="phoneRegister" >司机注册</button>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
var QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
var qqmapsdk;
export default {
	data() {
		return {
			code: null,
			cityName:"未知"
		};
	},
	onLoad() {
		let _this = this;
		_this.initMap();
		_this.getLocation();
	},
	methods: {
		//实例初始化地图SDK
		initMap(){
			let _this = this;
			//实例化API核心类
			qqmapsdk = new QQMapWX({
				key:_this.Consts.QQMAP_KEY
			});
		},
		//获取经纬度
		getLocation(){
			let _this = this;
			uni.getLocation({
				type:'gcj02',
				highAccuracyExpireTime:6000,
				isHighAccuracy:true,
				success:function(res){
					// console.log("当前位置的经度"+res.longitude);
					// console.log("当前位置的维度"+res.latitude);
					//整合地图接口-解析城市
					_this.reverseGeocoder(res.longitude,res.latitude);
				}
			});
		},
		//坐标转城市
		reverseGeocoder(longitude,latitude){
			var _this =this;
			qqmapsdk.reverseGeocoder({
				location:{
					longitude: longitude,
					latitude: latitude
				},
				success: function(res){
					//成功后的回调
					if(res.status == 0){
						// console.log(res);
						_this.cityName = res.result.address_component.city;
					}
				}
			});
		},
		
		phoneRegister(e){
			let _this = this;
			_this.wxRegister();
		},
		//获取微信并注册
		wxRegister(){
		// wxRegister(){
			let _this = this;
				wx.login({//登录
				success (res) {//成功
					if (res.code) {
						//拿到授权码--要用json格式传输后端接收
						let param = {
							wxCode : res.code,
							phoneCode : "0e3Dmv000f7rRR1Qj6300QZOtf4Dmv0F"
						}
						//发起网络请求
						_this.post(_this.Consts.API.DRIVER_REGISTER,param,(res)=>{
							let {message,data,code,success} = res.data;
							if(success){
								uni.showToast({
									title:"注册成功",icon:"success",
								})
								setTimeout(()=>{
									uni.navigateTo({
										url:"/pages/login/login"
									})
								},1000)
							}else{
								uni.showToast({
									title:message,icon:"error"
								})
							}
						})
					} else {
						uni.showToast({
							title:"注册失败",icon:"error"
						})
					}
				}
			})
		}
	},
	
};
</script>

<style lang="less">
@import url('register.less');
</style>
