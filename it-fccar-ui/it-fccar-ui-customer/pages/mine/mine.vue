<template>
	<view>
		<view class="summary-container">
			<button plain="true" size="mini" class="headerButton" open-type="chooseAvatar" @chooseavatar="onChooseAvatar" >
				<u-avatar	:src="customer.photo" size="large"></u-avatar>
			</button>
			<view class="summary">
				<input class="name" type="nickname" :value="customer.name"/>
				<view class="level">
					<u-icon name="integral-fill" color="#FFA600" size="35" class="icon"></u-icon>
					<text>{{customer.levelName}}</text>
				</view>
			</view>
		</view>
		<u-cell-group>
			<u-cell-item icon="car-fill" title="我的车辆" @click="myCar"></u-cell-item>
			<u-cell-item icon="trash-fill" :icon-style="icon" title="修改资料" />
			<u-cell-item icon="file-text-fill" title="我的订单"></u-cell-item>
			<u-cell-item icon="info-circle-fill" title="我的罚款" :value="customer.fine"></u-cell-item>
			<u-cell-item icon="coupon-fill" title="代金券" :value="customer.voucher" @click="this.toPage('../voucher_list/voucher_list')"></u-cell-item>
			<u-cell-item icon="server-fill" title="在线客服"></u-cell-item>

			<!-- <u-cell-item icon="trash-fill" :icon-style="icon" title="清理缓存" @click="clearHandle" />
			<u-cell-item icon="file-text-fill" :icon-style="icon" title="用户指南" @click="this.toPage('../../rule/user_guide/user_guide')" /> -->
			<u-cell-item icon="setting-fill" title="设置"></u-cell-item>
		</u-cell-group>
	</view>
</template>

<script>
export default {

	data() {
		return {
			customer:{
				photo: '',
				name: '',
				fine:"0",
				voucher:"0",
				levelName:"青铜"
			}
			
		};
	},
	methods: {
		nameChange(name){
			console.log(name);
		},
		changeHeader(imgPath){
			let _this = this;
			_this.post(_this.url.baseUrl+"/uaa/login/changeAvatarUrl/",{img:imgPath},(res)=>{
				let {success,message,code} = res.data;
				if(success){
					uni.showToast({
						title:"头像已更改",icon:"success"
					})
				}else{
					uni.showToast({
						icon:'error',
						title:message
					})
				}
			});
		},
		onChooseAvatar(e) {
			let _this = this;
			const { avatarUrl } = e.detail
			this.customer.photo = avatarUrl;
			//图片上传到云服务器
			_this.uploadCos(_this.url.baseUrl+"/common/cos/upload",avatarUrl,"customer",(res)=>{
				let {success,message,code,result} = JSON.parse(res.data);
				if(success && result){
					_this.customer.photo = result;
					//头像更改请求，修改lgoin中的头像
					_this.changeHeader(result);
				}else{
					uni.showToast({
						icon:'error',
						title:message
					})
				}
			})
		},
		userNameInput(e){
			console.log(e);
		},
		myCar(){
			uni.navigateTo({
				url:"../car_list/car_list"
			})
		},
		loadCustomerData(){
			let _this = this;
			_this.get(_this.url.baseUrl+"/customer/customer/data4mine/current",(res)=>{
				let {success,message,result} = res.data;
				if(success){
					_this.customer = result;
				}else{
					uni.showToast({icon:'error',title:message });
				}
			});
		},
	},
	onLoad() {
		this.loadCustomerData();
	}
};
</script>

<style lang="less">
@import url('mine.less');
</style>
