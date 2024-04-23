<template>
	<view>
		<camera device-position="back" flash="off" class="camera" @error="error" v-if="showCamera"></camera>
		<image mode="widthFix" class="image" :src="photoPath" v-if="showImage"></image>
		<view class="operate-container">
			<button type="primary" size="medium" class="btn" @tap="clickBtn">拍照</button>
			<button type="warn" size="medium" class="btn" @tap="restClickBtn">重拍</button>
			<button type="primary" size="medium" class="btn" @tap="uploadBtn">确定</button>
		</view>
		<view class="notice-container">
			<text class="notice">注意事项</text>
			<text class="desc">
				<block v-if="type!='driverCardBack'">拍照的时候，必须把证件拍摄清晰并完整，否则影响识别结果。持证拍照的时候不许戴帽子、墨镜和面纱等遮挡五官的饰物，只拍摄上半身即可。手持证件，五官与证件必须同时拍摄到照片中。</block>
				<block v-if="type=='driverCardBack'">拍照的时候，必须把驾驶证背面拍摄的清晰完整，否则影响认证结果。</block>
			</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			type: null,
        	photoPath: '',
        	showCamera: true,
        	showImage: false
		};
	},
	onLoad:function(options){
		this.type = options.type;
	},
		
	methods: {
		//重新拍照
		restClickBtn(){
			this.photoPath = "";
			this.showCamera = true;
			this.showImage = false;
		},
		//拍照按钮
		clickBtn() {
			const ctx = uni.createCameraContext();
			ctx.takePhoto({
				quality: 'high',
				success: (res) => {
					this.photoPath = res.tempImagePath
					this.showCamera = false;
					this.showImage = true;
				}
			});
		},
		//确认上传
		uploadBtn(){
			//获取到所有页面
			let pages = getCurrentPages();
			//上一个页面
			let prevPage = pages[pages.length - 2];
			//调用上一页的 uploadPhoto 方法
			prevPage.$vm.uploadPhoto(this.type,this.photoPath);
			//退回上一个页面
			uni.navigateBack({ delta:1 })
		},
		error(e) {
		}
	}
};
</script>

<style lang="less">
@import url('identity_camera.less');
</style>
