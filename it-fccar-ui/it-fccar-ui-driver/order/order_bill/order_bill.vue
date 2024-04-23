<template>
	<view>
		<view>
			<view class="setion-title">
				<image src="../static/order/money.png" mode="widthFix"></image>
				<text>基础收费</text>
			</view>
			<view class="section-content">
				<view class="item">
					<view class="left">
						<text class="item-title">里程费（{{ orderDetail.realMileage }}公里）</text>
						<text class="item-desc">时段收费（{{ orderDetail.baseMileagePrice }}元{{ orderDetail.baseMileage }}公里，超出每公里{{ orderDetail.exceedMileagePrice }}元）</text>
					</view>
					<view class="right">{{ orderDetail.mileageFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">等待费（{{ orderDetail.waitingMinute }}分钟）</text>
						<text class="item-desc">免费{{ orderDetail.baseWaitingMinute }}分钟，超出部分每分钟{{ orderDetail.exceedWaitingMinutePrice }}元</text>
					</view>
					<view class="right">{{ orderDetail.waitingFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">返程费（{{ orderDetail.returnMileage }}公里）</text>
						<text class="item-desc">总里程超过{{ orderDetail.baseReturnMileage }}公里，每公里{{orderDetail.exceedBaseReturnMileagePrice }}元</text>
					</view>
					<view class="right">{{ orderDetail.returnFee }}</view>
				</view>
			</view>
		</view>
		<view>
			<view class="setion-title">
				<image src="../static/order/money.png" mode="widthFix"></image>
				<text>额外收费</text>
			</view>
			<view class="section-content">
				<view class="item">
					<view class="left">
						<text class="item-title">停车费</text>
						<text class="item-desc">如果代驾司机预付停车费，该费用将计入订单费用</text>
					</view>
					<view class="right">{{ orderDetail.parkingFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">路桥费</text>
						<text class="item-desc">如果代驾司机预付停车费，该费用将计入订单费用</text>
					</view>
					<view class="right">{{ orderDetail.tollFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">其他费用</text>
						<text class="item-desc">代驾过程中产生的其他费用</text>
					</view>
					<view class="right">{{orderDetail.otherFee }}</view>
				</view>
			</view>
		</view>
		<view>
			<view class="setion-title">
				<image src="../static/order/money.png" mode="widthFix"></image>
				<text>奖励费</text>
			</view>
			<view class="section-content">
				<view class="item">
					<view class="left">
						<text class="item-title">客户好处费</text>
						<text class="item-desc">代驾客户赠与的红包奖励</text>
					</view>
					<view class="right">{{ orderDetail.favourFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">系统奖励费</text>
						<text class="item-desc">飞驰专车系统激励代驾司机的奖励</text>
					</view>
					<view class="right">{{ orderDetail.incentiveFee }}</view>
				</view>
			</view>
		</view>
		<view>
			<view class="setion-title">
				<image src="../static/order/money.png" mode="widthFix"></image>
				<text>总金额</text>
			</view>
			<view class="section-content">
				<view class="content-container">
					<view class="left">
						<view class="content big">
							【汇总合计】
							<text class="red">¥ {{ orderDetail.total }} 元</text>
						</view>
						<view class="content big">
							【平台抽成】
							<text class="red">¥ {{ orderDetail.platformIncome }} 元 | 比例：{{ orderDetail.platformRate }}</text>
						</view>
						<view class="content big">
							【司机收入】
							<text class="red">¥ {{ orderDetail.driverIncome }} 元</text>
						</view>
					</view>
					<image :src="img" mode="widthFix" class="img"></image>
				</view>
			</view>
			<button class="btn" @tap="sendOrderBill">发送账单</button>
		</view>
		<u-top-tips ref="uTips"></u-top-tips>
	</view>
</template>

<script>
export default {
	data() {
		return {
			orderDetail:{},
			orderNo: null,
			
		};
	},
	methods: {
		sendOrderBill(){
			let _this = this;
			_this.post("/order/app/order/sendPayOrder/"+_this.orderNo,(res)=>{
				let {success,message,data} = res.data; 
				if(success){
					uni.setStorageSync("workStatus","开始接单")
					uni.showToast({
						title:"账单已经发送",
					})
					
					setTimeout(()=>{
						uni.navigateTo({
							url:"../waiting_payment/waiting_payment?orderNo="+_this.orderNo
						})
					},2000)
				}else{
					
				}
			});
		},
		loadOrderBill(){
			let _this = this;
			_this.get("/order/app/order/orderbill/"+_this.orderNo,(res)=>{
				let {success,message,data} = res.data;
				if(success && data){
					_this.orderDetail = data;
				}
			});
		}
	},
	onLoad: function(options) {
		let _this = this;
		_this.orderNo = options.orderNo;
		this.loadOrderBill();
		
	},
	onShow: function() {
		
	},
	onHide: function() {
		
	}
};
</script>

<style lang="less">
@import url('order_bill.less');
</style>
