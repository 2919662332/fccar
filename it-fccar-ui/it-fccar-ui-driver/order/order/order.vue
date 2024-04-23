<template>
	<view>
		<view class="customer-container">
			<u-avatar :src="orderDetail.customerPhoto" mode="square"></u-avatar>
			<view class="info">
				<view class="customer-name">{{ orderDetail.customerName }}（乘客）</view>
				<view class="customer-tel">Tel：{{ orderDetail.customerPhone }}</view>
			</view>
		</view>
		<view class="address-container">
			<view class="from">
				<text>{{ orderDetail.startPlace }}</text>
			</view>
			<view class="dashed-line"></view>
			<view class="to">
				<text>{{ orderDetail.endPlace }}</text>
			</view>
		</view>
		<view class="order-container">
			<view>【 订单号码 】 {{ orderDetail.orderNo }}</view>
			<view>【 下单时间 】 {{ orderDetail.createTime }}</view>
			<view>【 客户红包 】 {{ orderDetail.favourFee }}元</view>
			<!-- <view>【 系统奖励 】 {{ orderDetail.incentiveFee }}元</view> -->
			<view>【 代驾车型 】 {{ orderDetail.carType }}</view>
			<view>【 代驾车牌 】 {{ orderDetail.carPlate }}</view>
		</view>
		<view>
			<view class="setion-title">
				<image src="../static/order/money.png" mode="widthFix"></image>
				<text>基础收费</text>
			</view>
			<view class="section-content">
				<view class="item">
					<view class="left">
						<text class="item-title">里程距离（{{ orderDetail.realMileage }}公里）</text>
						<text class="item-desc">时段收费（{{ orderDetail.baseMileagePrice }}元{{ orderDetail.baseMileage }}公里，超出每公里{{ orderDetail.exceedMileagePrice }}元）</text>
					</view>
					<view class="right">费用：{{ orderDetail.mileageFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">等待时长（{{ orderDetail.waitingMinute }}分钟）</text>
						<text class="item-desc">免费{{ orderDetail.baseWaitingMinute }}分钟，超出部分每分钟{{ orderDetail.exceedWaitingMinutePrice }}元</text>
					</view>
					<view class="right">费用：{{ orderDetail.waitingFee }}</view>
				</view>
				<view class="item">
					<view class="left">
						<text class="item-title">返程（{{ orderDetail.returnMileage }}公里）</text>
						<text class="item-desc">总里程超过{{ orderDetail.baseReturnMileage }}公里，每公里{{ orderDetail.exceedBaseReturnMileagePrice }}元</text>
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
					<view class="right">{{ orderDetail.otherFee }}</view>
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
		</view>
		

		<view v-if="orderDetail.status >= 7">
			<view class="setion-title">
				<image src="../static/order/rate.png" mode="widthFix"></image>
				<text>客户评价</text>
			</view>
			<view class="section-content">
				<view class="remark-container">
					<view class="remark-rate">
						<view class="photo"><u-avatar :src="orderDetail.customerPhoto" size="60" /></view>
						<view class="rate">
							<u-rate
								:count="rateCount"
								v-model="comment.rate"
								disabled="true"
								active-color="#FFBB2A"
								size="40"
							/>
							<view v-if="comment.rate <= 2 && comment.status == 0" @tap="appeal.showAppeal = true">
								我要申诉
							</view>
							<view v-if="comment.rate <= 2 && comment.status == 1">申诉中</view>
						</view>
					</view>
					<view class="remark">{{ comment.remark }}</view>
				</view>
			</view>
		</view>

		<view class="contact-container">
			<view class="contact">
				<text class="label">客服电话：</text>
				<text class="tel">0411-111111</text>
			</view>
			<view class="contact">
				<text class="label">服务监督电话：</text>
				<text class="tel">0411-111111</text>
			</view>
		</view>
		<u-top-tips ref="uTips"></u-top-tips>
		<u-popup v-model="appeal.showAppeal" mode="center" border-radius="14" width="550rpx" height="580rpx">
			<view class="appeal-title">飞驰专车订单评价申诉</view>
			<u-input
				v-model="appeal.reason"
				type="textarea"
				:border="false"
				:clearable="false"
				placeholder="填写申诉理由"
				:custom-style="appeal.reasonStyle"
				height="230"
			/>
			<u-button type="success" :custom-style="appeal.btnStyle" @click="insertAppeal">确定</u-button>
		</u-popup>
	</view>
</template>

<script>
export default {
	data() {
		return {
			orderDetail:{
				
			},
			orderNo: null,
			img: '',
			rateCount:5,
			comment: {
				rate: 0,
				remark: '[ 若不评价，系统默认为5星好评 ]',
				status: null
			},
			appeal: {
				showAppeal: false,
				reason: null,
				reasonStyle: {
					'background-color': '#f8f8f8',
					padding: '20rpx',
					margin: '0 50rpx'
				},
				btnStyle: {
					margin: '40rpx 50rpx 30rpx 50rpx'
				}
			}
		};
	},
	methods: {
		insertAppeal(){
			let _this = this;
			let param = {
				driverReason:_this.appeal.reason,
				orderNo:_this.orderNo
			}
			_this.post(_this.url.baseUrl+"/order/order/driver/representations",param,(res)=>{
				let {success,message,result} = res.data;
				if(success){
					uni.showToast({
						title:"审核成功"
					})
				}else{
					uni.showToast({
						title:message,
						icon:"error"
					})
				}
				
			});
		},
		loadOrder(){
			let _this = this;

			_this.get(_this.url.baseUrl+"/order/order/driver/orderbill/"+_this.orderNo,(res)=>{
				let {success,message,result} = res.data;
				if(success && result){
					_this.orderDetail = result;
				}
			});
			//加载评论
			_this.get(_this.url.baseUrl+"/order/orderComment/getByOrderNo/"+_this.orderNo,(res)=>{
				let {success,message,result} = res.data;
				if(success && result){
					_this.comment = result;
				}
			});
		}
	},
	onLoad: function(options) {
		this.orderNo = options.orderNo;
		this.loadOrder();
	},
	onShow: function() {},
	onHide: function() {}
};
</script>

<style lang="less">
@import url('order.less');
</style>
