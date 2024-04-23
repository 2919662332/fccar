<template>
	<view>
		<!-- 一.未实名认证，显示实名提示 -->
		<view class="review-container" v-show="!realAuthSuccess">
			<image src="../../static/workbench/review3.png" mode="widthFix"></image>
			<view class="title">您的实名资料还未认证审核通过</view>
			<view class="desc">这期间您将无法接单，请等待资料审核通过</view>
			<view class="toRealAuth">
				<button class="btn"  @click="toRealAuth" >我要认证</button>
				<button class="btn" v-show="summary.bitStatus == 35" :disabled="summary.bitStatus == 35" >合同正在审核</button>
			</view>
		</view>
		<!-- 二.实名成功，显示司机的统计数据 -->
		<view v-show="realAuthSuccess">
			<view class="top-container">
				<view class="notice">今日数据：凌晨00:00 ~ 本日23:59</view>
				<view class="summary-container">
					<view class="item">
						<view class="title">代驾时长</view>
						<view class="value">
							<text class="number">{{ summary.driveDuration }}</text>
							<text class="unit">时</text>
						</view>
					</view>
					<view class="item">
						<view class="title">今日收入</view>
						<view class="value">
							<text class="number">{{ summary.todayIncome }}</text>
							<text class="unit">元</text>
						</view>
					</view>
					<view class="item">
						<view class="title">今日成单</view>
						<view class="value">
							<text class="number">{{ summary.todayTradeOrders }}</text>
							<text class="unit">单</text>
						</view>
					</view>
				</view>
				<view class="service-container">
					<view class="item" :style="service.locationStyle">
						<image :src="service.locationIcon" mode="widthFix" class="icon icon-1"></image>
						{{ service.locationText }}
					</view>
					<view class="item" :style="service.messageStyle">
						<image :src="service.messageIcon" mode="widthFix" class="icon icon-2"></image>
						{{ service.messageText }}
					</view>
					<view class="item" :style="service.listenStyle" @tap="listenHandle()">
						<image :src="service.listenIcon" mode="widthFix" class="icon icon-3"></image>
						{{ service.listenText }}
					</view>
					<view class="item" :style="service.settingStyle" @tap="settingHandle()">
						<image :src="service.settingIcon" mode="widthFix" class="icon icon-4"></image>
						{{ service.settingText }}
					</view>
				</view>
			</view>
			<!-- 三.停止接单 -->
			<view v-show="workStatus == '停止接单'">
				<map id="map" :longitude="location.longitude" :latitude="location.latitude" 
				scale="15" :style="contentStyle" :enable-traffic="true" 
				:show-location="true" :enable-poi="true" style="width: 100%;">
					<cover-image class="location" src="../../static/workbench/location.png" @tap="returnLocationHandle()"></cover-image>
				</map>
			</view>
			<!-- 四.显示开始接单 -->
			<scroll-view scroll-y="true" :style="contentStyle" v-show="workStatus == '开始接单'">
				<view class="new-order">
					<view v-if="newOrder == null">
						<image src="../../static/workbench/no-order.png" mode="widthFix" class="no-order-img"></image>
						<view class="no-order-title">目前暂无订单</view>
					</view>
					
					<!-- 五.拉取到订单，执行抢单 -->
					<view v-if="newOrder != null">
						<view class="line-1">
							<view>
								<view class="sub-time">距离您</view>
								<view>
									<text class="number">{{ newOrder.distance }}</text>
									<text class="unit">公里</text>
								</view>
							</view>
							<view>
								<view class="sub-time">全程</view>
								<view>
									<text class="number">{{ newOrder.mileage }}</text>
									<text class="unit">公里</text>
								</view>
							</view>
							<view>
								<view class="tag">含加价{{ newOrder.favourFee }}元</view>
								<view>
									<text class="number">{{ newOrder.expectsFee }}</text>
									<text class="unit">元</text>
								</view>
							</view>
						</view>
						<view class="line-2">
							<view class="from">
								<text>{{ newOrder.from }}</text>
							</view>
							<view class="dashed-line"></view>
							<view class="to">
								<text>{{ newOrder.to }}</text>
							</view>
						</view>
						<button class="accept-btn" 		@tap="killOrderHandle">立即抢单</button>
						<button class="disabled-btn" 	@tap="ignoreHandle">拒绝接单</button>
					
					</view>
				</view>
				<view class="notice-container">
					<view class="title">【 抢单说明 】</view>
					<view class="desc">
						<text class="num">1.</text>
						当前地区可能存在多个代驾订单，但是司机端只能显示其中一条订单。这是为了保障代驾乘客订单的接单率，避免代驾司机只挑选优质订单。
					</view>
					<view class="desc">
						<text class="num">2.</text>
						某条订单如果10秒钟之内您没有抢单，系统则认定您主动放弃该订单，系统将为你推送新的订单。
					</view>
				</view>
			</scroll-view>
			<!-- 六.抢到订单，显示订单详情 -->
			<scroll-view scroll-y="true" :style="contentStyle" class="execute-order-container"
			 v-show="['接客户', '到达代驾点', '开始代驾'].includes(workStatus)">
				<view class="call" @tap="callCustomerHandle"></view>
				<view class="customer-container">
					<u-avatar :src="executeOrder.customerPhoto" mode="square"></u-avatar>
					<view class="info">
						<view class="customer-name">代驾客户（{{ executeOrder.customerName }}）</view>
						<view class="customer-phone">phone：{{ executeOrder.customerPhone }}</view>
					</view>
				</view>
				<view class="address-container">
					<view class="from">
						<text>{{ executeOrder.startPlace }}</text>
					</view>
					<view class="dashed-line"></view>
					<view class="to">
						<text>{{ executeOrder.endPlace }}</text>
					</view>
				</view>
				<view class="order-container">
					<view>【 订单号码 】 {{ executeOrder.orderNo }}</view>
					<view>【 下单时间 】 {{ executeOrder.createTime }}</view>
					<view>【 客户红包 】 9.9元</view>
					<view>【 代驾车型 】 {{ executeOrder.carType }}</view>
					<view>【 代驾车牌 】 {{ executeOrder.carPlate }}</view>
				</view>
				<view class="operate-container">
					<view class="item">
						<image src="../../static/workbench/alarm-icon2.png" mode="widthFix" class="alarm-icon"></image>
						<text class="alarm-text">立即报警</text>
					</view>
				
					<view class="vline"></view>
					<view class="item" v-show="workStatus == '接客户'" @tap="arriveStartPlaceHandle">
						<image src="../../static/workbench/arrive-start-icon.png" mode="widthFix" class="arrive-start-icon"></image>
						<text class="arrive-start-text">到达代驾点</text>
					</view>
					<view class="item" v-show="workStatus == '到达代驾点'" @tap="startDrivingHandle">
						<image src="../../static/workbench/drive-start-icon.png" mode="widthFix" class="drive-start-icon"></image>
						<text class="drive-start-text">开始代驾</text>
					</view>
					
					<view class="item" v-show="workStatus == '开始代驾'" @tap="endDrivingHandle">
						<image src="../../static/workbench/drive-stop-icon.png" mode="widthFix" class="drive-stop-icon"></image>
						<text class="drive-stop-text">结束代驾</text>
					</view>
					<view class="vline"></view>
					<view class="item" @tap="cancelOrderHandle">
						<image src="../../static/workbench/cancel-icon.png" mode="widthFix" class="cancel-icon"></image>
						<text class="cancel-text">取消订单</text>
					</view>
				</view>
				<view class="other-container">
					<view class="item" @tap="showNavigationHandle">
						<image src="../../static/workbench/other-icon-a.png" mode="widthFix" class="location-icon"></image>
						<text class="location-text">定位导航</text>
					</view>
					<view class="item" @tap="showMoveHandle">
						<image src="../../static/workbench/other-icon-b.png" mode="widthFix" class="display-icon"></image>
						<text class="display-text">司乘同显</text>
					</view>
					<view class="item" @tap="showOrderHandle">
						<image src="../../static/workbench/other-icon-c.png" mode="widthFix" class="order-icon"></image>
						<text class="order-text">订单详情</text>
					</view>
					<view class="item" @tap="callServiceHandle">
						<image src="../../static/workbench/other-icon-d.png" mode="widthFix" class="service-icon"></image>
						<text class="service-text">客服热线</text>
					</view>
				</view>
			</scroll-view>
			
			<!-- 七.开始接单和停止接单 -->
			<view class="bottom-container" v-show="['开始接单', '停止接单'].includes(workStatus)">
				<view class="btn" @tap="gohomeHandle()">
					<image src="../../static/workbench/btn-icon-1.png" mode="widthFix" class="btn-icon-1"></image>
					<text>回家</text>
				</view>
				<view class="btn primary">
					<text v-show="workStatus == '停止接单'" @tap="startWorkHandle()">开始接单</text>
					<text v-show="workStatus == '开始接单'" @tap="stopWorkHandle()">停止接单</text>
				</view>
				<view class="btn" @tap="rechargeHandle()">
					<image src="../../static/workbench/btn-icon-2.png" mode="widthFix" class="btn-icon-2"></image>
					<text>充值</text>
				</view>
			</view>
		</view>
		<u-top-tips ref="uTips"></u-top-tips>
		<u-toast ref="uToast" />
		<u-modal
			v-model="model.cancelConfirm.show"
			:title="model.cancelConfirm.title"
			:content="model.cancelConfirm.content"
			:show-confirm-button="true"
			:show-cancel-button="true"
			@confirm="confirmCancelOrder"
		></u-modal>
		<u-modal
			v-model="model.cancelSuccess.show"
			:title="model.cancelSuccess.title"
			:content="model.cancelSuccess.content"
			:show-confirm-button="true"
			:show-cancel-button="false"
		></u-modal>
	</view>
</template>

<script>
import {BitStateUtil} from "utils/bitstate.js"
import GtPush from '../../lib/gtpush-min.js'
//录音
let wechatSIPlugin = requirePlugin('WechatSI');
let dayjs = require('dayjs');

//腾讯地图插件
let QQMapWX = require('../../lib/qqmap-wx-jssdk.min.js');

let qqmapsdk;
export default {
	data() {
		return {
			getui:{
				//个推初始化
				appid: 'PpBUrcNGiL9fnExoJeCsQ5',
				cid: '',
				online: false,
				messages: '',
			},
			//是否实名认证成功
			realAuthSuccess: false,
			//司机的今日结算数据
			summary:{
				bitStatus:0,
				//驾龄
				driveDuration: 0,
				//今日收入
				todayIncome: 0,
				//订单数量
				todayTradeOrders: 0,
			},
			//司机当前定位，用户展示地图中心位子
			location:{
				latitude:"",
				longitude:"",
			},
			//地图控件
			map: null,
			//屏幕窗口高度
			windowHeight: 0,
			contentStyle: '',
			//服务图标和文本
			service: {
				locationIcon: '../../static/workbench/service-icon-1.png',
				locationText: '定位正常',
				locationStyle: '',
				messageIcon: '../../static/workbench/service-icon-2.png',
				messageText: '推送正常',
				messageStyle: '',
				listenIcon: '../../static/workbench/service-icon-3.png',
				listenText: '收听订单',
				listenStyle: '',
				settingIcon: '../../static/workbench/service-icon-4.png',
				settingText: '接单设置',
				settingStyle: ''
			},
			//司机的设置
			settings: {
				listenService: true,
				autoAccept: false
			},
			//司机当前的工作状态
			workStatus: uni.getStorageSync('workStatus') || "停止接单",
			//新订单，当前展示
			newOrder: null,
			//订单列表，所有订单
			newOrderList: [],
			//当前执行中的订单
			accpeting:false,
			executeOrder: {
				orderNo: '',
				customerPhoto: '',
				customerName: '',
				customerPhone: '',
				customerId: '',
				
				startPlace: '',
				startLongitude: '',
				startLatitude: '',
				
				endPlace: '',
				endLongitude: '',
				endLatitude: '',
				
				favourFee: '',
				carPlate: '',
				carType: '',
				createTime: ''
			},
			//是否播放语音
			playFlag: false,
			//语音播放器
			voicePlayer: null,
			//显示弹窗
			showModel: false,
			modelTitle: '',
			modelContent: '',
			model: {
				cancelConfirm: {
					show: false,
					title: '提示消息',
					content: '如果不满足订单免费取消规则，只能有偿取消订单，并且可能被禁止接单一段时间，您确认要取消当前订单？'
				},
				cancelSuccess: {
					show: false,
					title: '提示消息',
					content: ''
				}
			},
			//录音相关
			recordNum: 0,
			recordManager: null,
			stopRecord: false,
			//轮询获取订单的定时器
			pullOrderTimer:null,
			UploadLocationTimer:null
		};
	},
		
	methods: {
		//定时上报的定时任务
		uploadLocationTimer(){
			let _this = this;
			_this.UploadLocationTimer = setInterval(()=>{
				uni.getLocation({
					type:'gcj02',
					highAccuracyExpireTime:6000,
					isHighAccuracy:true,
					success:function(res){
						_this.upLocation(res.latitude,res.longitude)
					}
				});
				
			},10000)
		},
		//发送保存位置请求的方法
		upLocation(latitude,longitude){
			let _this = this;
			let params = {
				latitude: latitude,
				longitude:longitude,
				orderNo: _this.executeOrder.orderNo
			}
			_this.post("/bigdata/app/bigdata/uploadLocation",params,(res)=>{})
		},
		// 清楚定时器的方法
		clearuploadLocationTimer(){
			let _this = this;
			clearInterval(_this.UploadLocationTimer);
			_this.UploadLocationTimer = null;
		},
		//结束代驾
		endDrivingHandle(){
		  let _this = this;
		  _this.get("/order/app/order/endDriver/"+_this.executeOrder.orderNo,(res)=>{
		    let {success,message,data} = res.data;
		    if(success){
		      _this.workStatus = "结束代驾";
		      uni.setStorageSync("workStatus","结束代驾");
			  _this.clearuploadLocationTimer()
		      uni.navigateTo({
		        url:"/order/enter_fee/enter_fee?orderNo="+_this.executeOrder.orderNo
		      })
		    }
		  });
		},
		//司乘同显
		showMoveHandle(){
			let _this = this;
			uni.navigateTo({
				url:'/execution/move/move?orderNo='+_this.executeOrder.orderNo
			})
		},
		//到达代驾点
		arriveStartPlaceHandle(){
			let _this = this;
			_this.post("/order/app/order/arriveLocal/"+_this.executeOrder.orderNo,(res)=>{
				let {success,message,data} = res.data;
				if(success){
					_this.workStatus = _this.Consts.WORK_STATUS.ARRIVE_START_PLACE
					uni.setStorageSync("workStatus",_this.workStatus)
				}
			})
		},
		//开始代驾
		startDrivingHandle(){
			let _this = this;
			_this.get("/order/app/order/startDrive/"+_this.executeOrder.orderNo,(res)=>{
				let {success,message,data} = res.data;
				if(success){
					_this.workStatus = _this.Consts.WORK_STATUS.START_DRIVING
					uni.setStorageSync("workStatus",_this.workStatus)
					_this.uploadLocationTimer()
				}
			})
		},
		//发送请求订单的回显数据
		selectOrderStatusData(){
			let _this = this;
			_this.post("/order/app/order/selectOrderStatusData",(res)=>{
				let {success,message,data} = res.data;
				if(success && data){
					//如果订单位已接单状态，设置工作状态为接客户
					if(data.status == null){
						_this.workStatus = _this.Consts.WORK_STATUS.STOP_ACCPET_ORDER
						uni.setStorageSync("workStatus",_this.workStatus)
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ACCEPTED){
						_this.executeOrder = data;
						_this.workStatus = _this.Consts.WORK_STATUS.RECEPTION_CUSTOMER
						uni.setStorageSync("workStatus",_this.workStatus)
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE){
						_this.executeOrder = data;
						_this.workStatus = _this.Consts.WORK_STATUS.ARRIVE_START_PLACE
						uni.setStorageSync("workStatus",_this.workStatus)
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING){
						_this.executeOrder = data;
						_this.workStatus = _this.Consts.WORK_STATUS.START_DRIVING
						_this.uploadLocationTimer()
						uni.setStorageSync("workStatus",_this.workStatus)
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_COMPLETE_DRIVED){
						_this.workStatus = _this.Consts.WORK_STATUS.END_DRIVING
						uni.setStorageSync("workStatus",_this.workStatus)
						//跳转确认费用的页面
						uni.navigateTo({
							url:'/order/enter_fee/enter_fee?orderNo='+_this.executeOrder.orderNo
						})
					}else if(data.status == _this.Consts.OrderStatus.ORDER_STATUS_ENSURE){
						//跳转发送账单页面
						uni.navigateTo({
							url:'/order/order_bill/order_bill'
						})
					}else{
						_this.workStatus = _this.Consts.WORK_STATUS.STOP_ACCPET_ORDER
						uni.setStorageSync("workStatus",_this.workStatus)
					}
				}
			})
		},
		//立即抢单
		killOrderHandle(){
			let _this = this;
			_this.post("/order/app/order/killOrder/"+_this.newOrder.orderNo,(res)=>{
				let {success,message,data} = res.data;
				if(success){
					_this.workStatus = _this.Consts.WORK_STATUS.RECEPTION_CUSTOMER
					uni.setStorageSync("workStatus",_this.workStatus)
					uni.setStorageSync("orderNo",_this.newOrder.orderNo)
					uni.showToast({
						title:"抢单成功！",icon:'success'
					})
					_this.selectOrderStatusData()
				}
			})
		},
		toRealAuth(){
			uni.navigateTo({
				url:"/identity/filling/filling"
			})
		},
		//页面加载执行方法，查询数据
		getRealAuthSuccess(){
			let _this = this;
			_this.get("/driver/app/driver/getRealAuthSuccess",(res)=>{
				if(res.data.success){
					_this.summary.driveDuration = res.data.data.driveDuration
					_this.summary.todayIncome = res.data.data.todayIncome
					_this.summary.todayTradeOrders = res.data.data.todayTradeOrders
					_this.summary.bitStatus = res.data.data.bitStatus
					_this.realAuthSuccess = res.data.data.realAuthSuccess
					uni.setStorageSync("workStatus",_this.Consts.WORK_STATUS.STOP_ACCPET_ORDER)
				}
			})
		},
		//初始化地图组件样式
		initStyle(){
			let _this = this;
			_this.map = uni.createMapContext("map",this);
			//设置地图控件的高度适配屏幕高度
			let windowHeight = uni.getSystemInfoSync().windowHeight;
			_this.windowHeight = windowHeight - 200;
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
					_this.location.latitude = res.latitude;
					_this.location.longitude = res.longitude;
				}
			});
		},
		//点击按钮回到当前位置
		returnLocationHandle(){
			let _this = this;
			_this.map = uni.createMapContext("map",this);
			_this.map.moveToLocation({
				latitude:_this.location.latitude,
				longitude:_this.location.longitude
			})
		},
		
		ignoreHandle(){
			let _this = this;
			//删除数组第一个元素
			_this.newOrderList.shift();
			_this.newOrder = null;
			if(_this.newOrderList == null || _this.newOrderList.length == 0){
				_this.createTimer()
			}else{
				_this.newOrder = _this.newOrderList[0]
			}
		},
		
		//拉去订单的请求
		pullOrder(){
			let _this = this;
			_this.post("/order/app/order/pull",(res)=>{
				let {message,data,success} = res.data
				if(success && data && data.length > 0){
					_this.newOrderList = data;
					_this.newOrder = _this.newOrderList[0]
					//已经拉到订单就关闭定时任务
					_this.closeTimer()
				}
			})
		},
		//停止定时任务
		closeTimer(){
			let _this = this;
			if(_this.pullOrderTimer != null){
				clearInterval(_this.pullOrderTimer)	
			}
		},
		//拉去订单的定时任务
		createTimer(){
			let _this = this;
			_this.pullOrderTimer = setInterval(()=>{
				_this.pullOrder();
			},5000)
		},
		
		//点击开始接单保存司机配置
		startWorkHandle(){
			let _this = this;
			_this.workStatus = _this.Consts.WORK_STATUS.START_ACCPET_ORDER;
			uni.setStorageSync("workStatus",_this.workStatus)
			//发送请求缓存司机的配置数据
			_this.post("/driver/app/driver/saveSetting",(res)=>{
				if(res.data.success){
					_this.voicePlayer.src = '/static/voice/voice_1.mp3'
					_this.voicePlayer.play()
					_this.voicePlayer.onPlay(()=>{
						_this.playFlag = true
					})
					//调用定时任务拉去订单
					_this.createTimer()
				}
			})
		},
		//点击停止接单删除司机的位置数据
		stopWorkHandle(){
			let _this = this;
			_this.workStatus = _this.Consts.WORK_STATUS.STOP_ACCPET_ORDER;
			uni.setStorageSync("workStatus",_this.workStatus)
			//发送请求缓存司机的配置数据
			_this.post("/driver/app/driver/stopAcceptOrder",(res)=>{
				if(res.data.success){
					_this.voicePlayer.src = '/static/voice/voice_2.mp3'
					_this.voicePlayer.play()
					_this.voicePlayer.onPlay(()=>{
					_this.playFlag = true
					})
					_this.closeTimer()
				}
			})
		},
	},
	onLoad: function() {
		//加载进行中的订单
		let _this = this;
		_this.getRealAuthSuccess();
		_this.getLocations()
		_this.initStyle()
		_this.voicePlayer = uni.createInnerAudioContext();
		if(_this.workStatus == _this.Consts.WORK_STATUS.START_ACCPET_ORDER){
			_this.createTimer();
		}
		_this.selectOrderStatusData()
	},
	onShow: function() {
		let _this = this;
	},
	onHide: function() {
	}
};
</script>

<style lang="less">
@import url('workbench.less');
</style>
