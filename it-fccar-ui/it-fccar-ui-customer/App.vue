<script>
export default {
    onLaunch: function() {
		
		//订阅消息
		wx.requestSubscribeMessage({
		  tmplIds: ['V_KaT0Y_CzAf264nC_hqkztS5RlVhr3EyKAaQ9l5lY4'],
		  success (res) {
			  console.log("订阅消息，",res);
		  }
		})
		
		//开启位置更新监听
		wx.startLocationUpdate({
			type:"wgs84",
			success(res){
				console.log("开始位置实时监听...");
			},fail(res) {
				uni.showToast({
					title:"定位失败",icon:"error"
				})
			}
		})
		
		wx.onLocationChange((res)=>{
			//获取经纬度
			const {latitude,longitude} = res;
			
			//把坐标位置传给locationChange 函数
			const location = {latitude:latitude,longitude:longitude};
			uni.$emit("locationChange",location)
		});
    },
    onShow: function() {
    },
    onHide: function() {
        console.log('App Hide');
    }
};
</script>

<style lang="scss">
@import "uview-ui/index.scss";
</style>
