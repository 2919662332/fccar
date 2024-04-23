<template>
    <view class="page">
        <view v-if="list.length > 0" v-for="one in list" class="row"
					@tap="choseOneHandle(one.id,one.carPlate,one.carType)" @longpress="removeHandle(one.id)">
            <view>
                <u-icon name="info-circle-fill" color="#2979ff" size="35" class="icon"></u-icon>
                <text class="car-type">{{ one.carType }}</text>
            </view>
            <text class="car-plate">{{ one.carPlate }}</text>
        </view>
        <image v-if="list.length == 0" src="../../static/car_list/none.jpg" mode="widthFix" class="none"></image>
        <button class="btn" @tap="addHandle">添加车辆</button>
        <u-toast ref="uToast" />
    </view>
</template>

<script>
export default {
    data() {
        return {
            list: []
        };
    },
    methods: {
		//选择车辆
		choseOneHandle(carId,carPlate,carType){
			let car = {
				carId : carId,
				carPlate : carPlate,
				carType : carType
			}
			uni.setStorageSync("car",car);
			uni.navigateBack();
		},
		//添加车辆
        addHandle(){
			uni.navigateTo({
				url:"../add_car/add_car"
			})
		},
		loadCarList(){
			let _this = this;
			_this.get("/customer/app/customer/car/list/current",(res)=>{
				let {success,message,data} = res.data;
				if(success){
					_this.list = data;
				}else{
					uni.showToast({icon:'error',title:message });
				}
				
			});
		}
    },
	//每次展示页面就会调用
	onShow() {
		let _this = this;
		_this.loadCarList();
	},
	//每次加载页面就会调用
    onLoad: function() {
		
    },
};
</script>

<style lang="less">
@import url('car_list.less');
</style>
