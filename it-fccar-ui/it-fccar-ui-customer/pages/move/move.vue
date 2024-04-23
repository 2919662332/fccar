<template>
	<view>
		<map
			id="map"
			scale="13"
			:longitude="from.longitude"
			:latitude="from.latitude"
			:enable-poi="true"
			class="map"
			:style="mapStyle"
			:polyline="polyline"
			:markers="markers"
			@longpress="showHandle"
		>
		<image class="location" src="../static/workbench/location.png" @tap="returnLocationHandle()" />
		</map>
		<view class="panel" v-show="infoStatus">
			<view class="info">
				<view class="label">剩余</view>
				<view class="value">{{ distance }}公里</view>
				<view class="label">，预计</view>
				<view class="value">{{ duration }}分钟</view>
				<view class="more" @tap="toOrderDetail">订单详情</view>
			</view>
			<view class="opt">
				<button class="cancel-btn" :style="cancelStyle" @tap="cancelHandle">取消订单</button>
				<button class="confirm-btn" v-if="status == 2||status == 3" @tap="driverArriviedHandle">司机到达</button>
			</view>
		</view>
	</view>
</template>

<script>
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
	
import GtPush from '../../lib/gtpush-min.js'

let qqmapsdk;
export default {
	data() {
		return {
			orderNo: null,
			status: null,
			mode: null,
			cancelStyle: '',
			map: null,
			mapStyle: '',
			from:{
				latitude:0,
				longitude:0,
			},
			to:{
				latitude:0,
				longitude:0,
			},
			distance: 0,
			duration: 0,
			polyline: [],
			markers: [],
			//拉取司机坐标的定时器
			driverLocationTimer: null,
			infoStatus: true,
			pullBillTimer:null
		};
	},
	methods: {
		toOrderDetail(){
			let _this = this;
			uni.navigateTo({
				url:"../order/order?orderNo="+_this.orderNo
			})
		},
		//乘客取消订单
		cancelHandle(){
			let _this = this;
			_this.post("/order/app/order/cancel/customer/"+_this.orderNo,(res)=>{
				let {success, message} = res.data;
				if(success){
					uni.showToast({
						title:"订单已取消",icon:"success"
					})
					setTimeout(()=>{
						uni.navigateBack();
					},2000)
				}else{
					uni.showToast({
						title:"取消失败:"+message,icon:"error"
					})
				}
			});
		},
		initStyle(){
			let _this = this;
			//设置地图控件的高度适配屏幕高度
			let windowHeight = uni.getSystemInfoSync().windowHeight;
			_this.windowHeight = windowHeight;
			_this.mapStyle = `height:${_this.windowHeight}px;`;
		},
		//初始化地图
		initMap(){
			let _this = this;
			_this.map = uni.createMapContext("map");
			//初始化腾讯地图
			qqmapsdk = new QQMapWX({
				key: _this.Consts.QQMAP_KEY
			});
		},	
		//路线规划
		linePlan(){
			let _this = this;
			if(_this.to.latitude == 0 || _this.to.longitude == 0
			 || _this.from.latitude == 0 || _this.from.longitude == 0
			)return;
			qqmapsdk.direction({
				//模式：驾车
				  mode: _this.mode,
				  //路线规划开始位置
				  from: {
					latitude: _this.from.latitude,
					longitude: _this.from.longitude
				  },
				  //路线规划结束位子
				  to: {
					  latitude: _this.to.latitude,
					  longitude: _this.to.longitude
				  }, 
				  success: function (res) {
					var ret = res;
					
					//方案整体距离（米）（米转换为千米）
					let distance = ret.result.routes[0].distance / 1000.0;
					//方案估算时间（分钟）
					let duration = ret.result.routes[0].duration;
					_this.distance = distance ;
					_this.duration = duration;
					
					//拿到路线规划结果中的路线坐标 ， pl是用来装经纬度数组的集合
					var coors = ret.result.routes[0].polyline, pl = [];
					//坐标解压（返回的点串坐标，通过前向差分进行压缩）
					var kr = 1000000;
					for (var i = 2; i < coors.length; i++) {
					  coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
					}
					//将解压后的坐标放入点串数组pl中
					for (var i = 0; i < coors.length; i += 2) {
					  pl.push({ latitude: coors[i], longitude: coors[i + 1] })
					}
					
					//设置polyline属性，将路线显示出来,将解压坐标第一个数据作为起点
					_this.from.latitude = pl[0].latitude;
					_this.from.longitude = pl[0].longitude;
					
					_this.polyline = [{ points: pl, arrowLine:true,color: '#FF0000DD', width: 4 }];
					
					//绘制开始位置和结束位子的图标
					_this.markers = [
						{//开始位置图标
							id:1,
							latitude:_this.from.latitude,
							longitude:_this.from.longitude,
							title:"开始位置",
							iconPath:"/static/move/driver-icon.png",
							height:40
							
							//iconPath:"https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/start.png"
						},
						{//结束位置图标
							id:2,
							latitude:_this.to.latitude,
							longitude:_this.to.longitude,
							title:"结束位置",
							iconPath:"https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/end.png"
						}
					]
					
					uni.hideLoading();
				  },
				  fail: function (error) {
					uni.showToast({
						title:"路线绘制失败",icon:"error"
					})
				  }
			});
		},
		//加载订单的位子 ：结束位置
		loadOrderLocation(){
			let _this = this;
			if(!_this.orderNo)return;
			//加载订单地址
			_this.get("/order/app/order/location/"+_this.orderNo,(res)=>{
				let {success,message,data} = res.data; 
				if(success){
					//根据订单状态，设置终点和驾车方式
					//订单状态为：接单成功(去接乘客)，路线规划的结束位置就是 订单的起点
					if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED){ 
						//接单成功，去往开始位子
						_this.to.latitude = data.startPlaceLatitude;
						_this.to.longitude = data.startPlaceLongitude;
						//自行车
						_this.mode = "bicycling";
						//定时拉取司机的坐标
						_this.pullDriverLocation();
						_this.createPullDriverLocationTimer();
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE
					 || data.status ==_this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING){
						//已经到达，或者 开始代驾，路线规划的结束位子是订单的结束位子
						_this.to.latitude = data.endPlaceLatiude;
						_this.to.longitude = data.endPlaceLongitude;
						//驾车
						_this.mode = "driving";
						//定时拉取司机的坐标
						_this.pullDriverLocation();
						_this.createPullDriverLocationTimer();
					}
				}
			});
		},
		pullDriverLocation(){
			let _this = this;
			_this.get("/order/app/order/pull/driver/location/"+_this.orderNo,(res)=>{
				let {success , data} = res.data;
				if(success && data){
					_this.from.latitude = data.latitude;
					_this.from.longitude = data.longitude;
					//划线
					_this.linePlan();
				}
			});
		},
		//定时拉取司机坐标：获取开始位置
		createPullDriverLocationTimer(){
			let _this = this;
			if(_this.driverLocationTimer)return;
			_this.driverLocationTimer = setInterval(()=>{
				_this.pullDriverLocation();
			},10000)
		},
		//关闭定时任务
		stopPullDriverLocationTimer(){
			let _this = this;
			if(_this.driverLocationTimer){
				clearInterval(_this.driverLocationTimer);
				_this.driverLocationTimer = null;
			};
		}
	},
	onLoad: function(options) {
		let _this = this;
		uni.showLoading({
			title:"路线加载中..."
		})
		_this.orderNo = options.orderNo;
		_this.initStyle();
		_this.initMap();
	},
	onShow: function() {
		let _this = this;
		//拉取订单位置
		_this.loadOrderLocation();
	},
	onHide: function() {
		let _this = this;
		_this.stopPullDriverLocationTimer();
	},
	onUnload() {
		let _this = this;
		_this.stopPullDriverLocationTimer();
	}
};
</script>

<style lang="less">
@import url('move.less');
</style>
