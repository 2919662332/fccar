<template>
	<view>
		<view v-if="inPorcessOrderId != null" class="order-process" @click="toOrder">您有进行中的订单,点击查看</view>
		
		<map id="map" :longitude="from.longitude" :latitude="from.latitude" :style="contentStyle" scale="15" :enable-traffic="false" :show-location="true" :enable-poi="true" class="map">
			<cover-image class="location" src="../../static/workbench/location.png" @tap="toLocationHandle()"></cover-image>
		</map>
		<view class="panel">
			<view class="from" @tap="chooseLocationHandle('from')">
				<text>{{ from.address }}</text>
			</view>
			<view class="dashed-line"></view>
			<view class="to" @tap="chooseLocationHandle('to')">
				<text>{{ to.address }}</text>
			</view>
			<button class="btn" @tap="toCreateOrderHandle">查看路线</button>
		</view>
	</view>
</template>

<script>
	
import GtPush from '../../lib/gtpush-min.js'
	//地图点选插件
const chooseLocation = requirePlugin('chooseLocation');
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
let qqmapsdk ;

export default {
	data() {
		return {
			//开始位置
			from: {
				address: '输入你的开始位置',
				longitude: 0,
				latitude: 0
			},
			//结束位置
			to: {
				address: '输入你的目的地', 
				longitude: 0,
				latitude: 0
			},
			
			//窗口样式
			contentStyle: '',
			windowHeight: 0,
			//地图对象
			map: null,
			//标记:开始位置点选或者结束位置点选
			chooseFromOrToflag: null,
			inPorcessOrderId:null,
			chooseType:"",
		};
	},
	methods: {
		//个推消息推送
		GetuiMessagePush(){
			let _this = this;
			GtPush.setDebugMode(true)
			GtPush.init({
			    appid: 'bSluIsNRkB6oIq0o4iu9g3',
			    onClientId: (res) => {
			      //个推ClientId回调，拿到cid后将其上报到服务端
			      console.info(`onClientId: ${res.cid}`)
				  _this.post("/common/app/common/BindCidAlis/"+res.cid,(res)=>{
					  
				  })
			  },
			  onlineState: (data) => {
			    //个推ClientId在线状态回调
			    console.info(`onlineState: ${data.online}`)
			  },
			
			  onPushMsg: (res) => {
			    //推送消息回调
				let msg = res.message
				let message = JSON.parse(msg)
				if(message.status == 1){
					uni.navigateTo({
						url:"/pages/order/order?orderNo="+ message.content
					})
				}
			  },
			
			  onError: (res) => {
			    //错误回调
			    console.info(`onError`, res.error)
			  }
			})
		},
		//加载回显的订单
		loadInProcessOrder(){
			let _this = this;
			_this.get("/order/app/order/loadCustomerOrder",(res)=>{
				let {data,message,success} = res.data;
				if(success && data){
					if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_WAIT){
						//将订单的from to car 存到缓存
						uni.setStorageSync("location",{
							from:{address:data.startPlace,longitude:data.startPlaceLongitude,latitude:data.startPlaceLatitude},
							to:{address:data.endPlace,longitude:data.endPlaceLongitude,latitude:data.endPlaceLatiude},
						})
						uni.navigateTo({
							url:'/pages/create_order/create_order?showPopup=true&orderNo='+data.orderNo
						})
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED
					 || data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE
					  || data.status == _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING){
						uni.navigateTo({
							url:'/pages/move/move?orderNo='+data.orderNo
						})
						//ORDER_STATUS_ENSURE : 5,	//司机已经确认费用 应该跳转 发送账单页
						//ORDER_STATUS_NOT_PAY : 6,	//账单已经发送 跳转订单详情页
						//TODO,5,6暂时未判断
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_COMPLETE_DRIVED){
						uni.navigateTo({
							url:'/pages/order/order'
						})
					}
				}
			})
		},
		//实例初始化地图SDK
		initMap(){
			let _this = this;
			//实例化API核心类
			qqmapsdk = new QQMapWX({
				key:_this.Consts.QQMAP_KEY
			});
		},
		//初始化地图组件样式
		initStyle(){
			let _this = this;
			_this.map = uni.createMapContext("map",this);
			//设置地图控件的高度适配屏幕高度
			let windowHeight = uni.getSystemInfoSync().windowHeight;
			_this.windowHeight = windowHeight;
			_this.contentStyle = `height:${_this.windowHeight}px;`;
		},
		//获取经纬度传给地图组件
		getLocations(){
			let _this = this;
			uni.getLocation({
				type:'gcj02',
				highAccuracyExpireTime:6000,
				isHighAccuracy:true,
				success:function(res){
					_this.from.latitude = res.latitude;
					_this.from.longitude = res.longitude;
					_this.reverseGeocoders(res.longitude,res.latitude)
				}
			})
		},
		//点击按钮回到当前位置
		toLocationHandle(){
			let _this = this;
			_this.map = uni.createMapContext("map",this);
			_this.map.moveToLocation({
				latitude:_this.from.latitude,
				longitude:_this.from.longitude
			})
		},
		//选择位置
		chooseLocationHandle(type){
			let _this = this;
			_this.chooseType = type;
			wx.navigateTo({
			  url: 'plugin://chooseLocation/index?key=' + _this.Consts.QQMAP_KEY + '&referer=' + 'map'
			});
		},
		//点击查看路线
		toCreateOrderHandle(){
			let _this = this
			if(_this.to.address == '输入你的目的地' || _this.from.latitude == 0){
				uni.showToast({
					title:"请选择地址！",icon:'error'
				})
			}
			//将订单的from to car 存到缓存,用于创建地图页面划线和数据显示
			uni.setStorageSync("location",{
				from:_this.from,
				to:_this.to
			})
			uni.navigateTo({
				url:'../create_order/create_order'
			})
		},
		//坐标转城市
		reverseGeocoders(longitude,latitude){
			var _this =this;
			qqmapsdk.reverseGeocoder({
				location:{
					longitude: longitude,
					latitude: latitude
				},
				success: function(res){
					//成功后的回调
					if(res.status == 0){
						_this.from.address = res.result.address;
					}
				}
			});
		},
	},
	onShow: function() {
		let _this = this;
		const location = chooseLocation.getLocation();
		if(_this.chooseType == 'from'){
			_this.from.address = location.name
			_this.from.longitude = location.longitude
			_this.from.latitude = location.latitude
		}else if(_this.chooseType == 'to'){
			_this.to.address = location.name
			_this.to.longitude = location.longitude
			_this.to.latitude = location.latitude
		}
	},
	onHide: function() {
	},
	onLoad: function() {
		let _this = this;
		_this.initStyle()
		_this.initMap()
		_this.getLocations()
		_this.loadInProcessOrder()
		_this.GetuiMessagePush()
	},
	onUnload: function() {
		
	}
};
</script>

<style lang="less">
@import url('workbench.less');
</style>
