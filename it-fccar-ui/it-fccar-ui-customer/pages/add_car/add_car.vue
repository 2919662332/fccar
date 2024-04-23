<template>
    <view class="page">
        <u-cell-group :border="false">
            <u-cell-item icon="info-circle-fill" title="车型信息" :value="carType" @click="carTypeHandle"></u-cell-item>
            <u-cell-item icon="info-circle-fill" title="车牌号码" :value="carPlate" @click="carPlateHandle"></u-cell-item>
        </u-cell-group>
        <button class="btn" @tap="saveHandle">确定保存</button>
        <view class="desc">
            保存即代表您同意飞驰专车代驾的
            <text>《隐私政策》</text>
        </view>
        <u-toast ref="uToast" />
    </view>
</template>

<script>
export default {
    data() {
        return {
            carType: '',
            carPlate: ''
        };
    },
    methods: {
		saveHandle(){
			let _this = this;
			let param = {
				carType: _this.carType,
				carPlate: _this.carPlate
			}
			_this.post("/customer/app/customer/car/add",param,(res)=>{
				let {success,message} = res.data;
				if(success){
					uni.showToast({title:"保存成功" });
					setTimeout(function(){
						uni.navigateBack();
					},1200)
				}else{
					uni.showToast({icon:'error',title:message });
				}
				
			});
		},
		carTypeHandle(){
		  let _this = this;
		  uni.showModal({
		  	title:"填写车形",
			showCancel:true,
			editable:true,
			success(res) {
				console.log(res);
				if(res.confirm){
					_this.carType = res.content;
				}
			}
		  })
		},
		carPlateHandle(){
				  let _this = this;
				  uni.showModal({
					title:"填写车牌号",
					showCancel:true,
					editable:true,
					success(res) {
						console.log(res);
						if(res.confirm){
							_this.carPlate = res.content;
						}
					}
				  })
		}  
    }
};
</script>

<style lang="less">
@import url('add_car.less');
</style>
