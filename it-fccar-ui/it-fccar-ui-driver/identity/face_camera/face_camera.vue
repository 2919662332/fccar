<template>
	<view>
		<view class="face-container">
			
			<camera device-position="front" flash="off" class="camera" @error="error" v-if="showCamera">
				<cover-image src="../static/face_camera/bg.png" class="bg"></cover-image>
			</camera>
			
			<view class="image-container" v-if="showImage">
				<image mode="widthFix" class="photo" :src="photoPath"></image>
				<view class="cover"></view>
			</view>
		</view>
		<view class="desc">
			<block v-if="mode == 'verificate'">
				<image src="../static/face_camera/tips.png" mode="widthFix" class="tips"></image>
				<text>请把面部放在圆圈内</text>
				<text>,拍摄脸部来确认身份</text>
			</block>
			<block v-if="mode == 'create'">
				<image src="../static/face_camera/face.png" mode="widthFix" class="face"></image>
				<text>请把完整面部放在圆圈内</text>
				<text>拍摄脸部来保存身份识别数据</text>
			</block>
		</view>
		<button class="btn" @tap="confirmHandle">{{ mode == 'create' ? '录入面部信息' : '身份核实' }}</button>
	</view>
</template>

<script>
let dayjs = require('dayjs');
export default {
	data() {
		return {
			mode: 'verificate',
			photoPath: '',
			showCamera: true,
			showImage: false,
			audio: null,
			driverData:{
				phone:"",
				gender:"",
				name:""
			}

		};
	},
	methods: {
		confirmHandle(){
			//拿到相机数据
			let _this = this;
			let camera = uni.createCameraContext();
			camera.takePhoto({
				quality:"high",
				success:function(res){
					let img = res.tempImagePath;
					_this.photoPath = img;
					_this.showCamera = false;
					_this.showImage = true;
					_this.audio.stop();
					
					uni.getFileSystemManager().readFile({
						filePath:img,
						encoding:"base64",
						success:function(imgResult){
							let imageBase64 = "data:image/jpeg;base64,"+imgResult.data;
							//上传人脸数据
							_this.driverData["imageBase64"] = imageBase64;
							_this.post(_this.url.baseUrl+"/common/face/upload",_this.driverData,(res)=>{
								console.log(res);
								if(res.data.success){
									uni.showToast({
										icon:"success",
										title:"人脸数据采集完成",
										success:function(){
											setTimeout(function(){
												uni.navigateTo({
													url:"/pages/login/login"
												})
											},2000)
											
										}
									})
								}else{
									uni.showToast({
										icon: "error",
										title: res.data.message
									})
									_this.showCamera = true;
									_this.showImage = false;
								}
								
							});
						}
					})
				}
				
			})
		},
		error(){
			
		},
	},
	onLoad: function(options) {
		this.mode = options.mode;
		this.driverData.phone = options.phone;
		this.driverData.gender = options.gender;
		this.driverData.name = options.name;
		console.log(this.driverData);
		//播放音乐
		let audio = uni.createInnerAudioContext();
		this.audio = audio;
		audio.src = "/static/voice/voice_5.mp3";
		audio.play();
	},
	onHide:function(){
		
	}
};
</script>

<style lang="less">
@import url('face_camera.less');
</style>
