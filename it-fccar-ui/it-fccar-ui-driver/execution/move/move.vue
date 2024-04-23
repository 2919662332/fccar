<template>
	<view>
		<map
			id="map"
			scale="15"
			:longitude="from.longitude"
			:latitude="from.latitude"
			:enable-poi="true"
			class="map"
			:style="mapStyle"
			:polyline="polyline"
			:markers="markers"
			@longpress="showHandle"
		>
			<!-- <cover-image class="location" src="../static/workbench/location.png" @tap="returnLocationHandle()" /> -->
			<cover-view class="information-container" v-show="infoStatus">
				<cover-image src="../static/move/close-icon.png" class="close-icon" @tap="hideHandle"></cover-image>
				<cover-view class="info">
					<cover-view class="label">剩余</cover-view>
					<cover-view class="value">{{ distance }}公里</cover-view>
					<cover-view class="label">，预计</cover-view>
					<cover-view class="value">{{ duration }}分钟</cover-view>
				</cover-view>
			</cover-view>
		</map>
	</view>
</template>

<script>
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');
let qqmapsdk;
export default {
	data() {
		return {
			map: null,
			mapStyle: '',
			from:{
				//当前司机坐标。应该实时获取定位.也是路线规划的开始位置
				latitude: 0,//uni.getStorageSync("location").latitude,
				longitude: 0,//uni.getStorageSync("location").longitude,
			},
			to:{//路线规划的结束位子
				latitude:0,
				longitude:0
			},
			//测距：距离和时长
			distance: 0,
			duration: 0,
			//路线测距的路线坐标点数组
			polyline: [],
			//图标
			markers: [],
			timer: null,
			infoStatus: true,
			mode:"bicycling"
		};
	},
	methods: {
		//获取经纬度
		getLocation(){
			let _this = this;
			uni.getLocation({
				type:'gcj02',
				highAccuracyExpireTime:6000,
				isHighAccuracy:true,
				success:function(res){
					_this.from.longitude = res.longitude
					_this.from.latitude = res.latitude
				}
			});
		},
		//初始化样式
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
		createLinePlanTimer(){
			let _this = this;
			if(_this.timer)return;
			_this.timer = setInterval(()=>{
				uni.getLocation({
					type: 'gcj02',
					success: function (res) {
						_this.from.latitude = res.latitude;
						_this.from.longitude = res.longitude;
						_this.linePlan();
					}
				});
				
			},10000)
		},
		deleteLinePlanTimer(){
			let _this = this;
			if(_this.timer == null)return;
			clearInterval(_this.timer);
			_this.timer = null;
		},
		//路线规划
		linePlan(){
			let _this = this;
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
					
					_this.polyline = [{ points: pl, color: '#FF0000DD', width: 3 }];
					
					//绘制开始位置和结束位子的图标
					_this.markers = [
						{//开始位置图标
							id:1,
							latitude:_this.from.latitude,
							longitude:_this.from.longitude,
							title:"开始位置",
							iconPath:"../static/move/driver-icon.png",
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
				  },
				  fail: function (error) {
					uni.showToast({
						title:"路线绘制失败",icon:"error"
					})
				  }
			});
		},
		//加载订单的位子
		loadOrderLocation(){
			let _this = this;
			if(!_this.orderNo)return;
			//加载订单地址
			_this.get("/order/app/order/localInfo/"+_this.orderNo,(res)=>{
				let {success,message,data} = res.data; 
				if(success){
					//根据订单状态，设置终点和驾车方式
					if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED){ 
						//接单成功，去往开始位子  //开始位置即是乘客的位置
						_this.to.latitude = data.startPlaceLatitude;
						_this.to.longitude = data.startPlaceLongitude;
						//自行车
						_this.mode = "bicycling";
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE || data.status == _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING){
						//开始代驾，路线规划的结束位子是订单的结束位子
						_this.to.latitude = data.endPlaceLatiude;
						_this.to.longitude = data.endPlaceLongitude;
						console.log("laa:"+_this.to.latitude,_this.to.longitude)
						//驾车
						_this.mode = "driving";
					}
					//创建定时任务
					_this.linePlan();
					_this.createLinePlanTimer();
				
				}
			});
		},
	},
	onLoad: function(options) {
		let _this = this;
		_this.initStyle();
		_this.initMap();
		_this.orderNo = options.orderNo;
		_this.getLocation()
		//加载订单位置
		_this.createLoadOrderLocationTimer();
			
	},
	onShow: function() {
		let _this = this;
		//实时位置监听获取司机的位置信息
		_this.$on("locationChange",(location)=>{
			_this.from.longitude = location.longitude;
			_this.from.latitude = location.latitude;
		});
		_this.loadOrderLocation();
	},
	onUnload(){
		let _this = _this;
		_this.deleteLinePlanTimer();
	},
	onHide: function() {
		let _this = _this;
		_this.deleteLinePlanTimer();
	}
};
</script>

<style lang="less">
@import url('move.less');
</style>
