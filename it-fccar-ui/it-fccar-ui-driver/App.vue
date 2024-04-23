<script>
	


export default {
	data(){
		return {

		};
	},
	onLaunch: function() {
		let _this = this;
		
		//初始化工作状态
		_this.initWorkStatus();
		//保持屏幕亮着11
		wx.setKeepScreenOn({ keepScreenOn:true }) 
		//实时监听时机位置
		uni.startLocationUpdate({
			success(res) {
				
			},
			fail(error) {
				
			}
		});
		
		//监听时机坐标改变
		uni.onLocationChange((res)=>{
			
			//缓存司机定位，用作接单
			_this.cacheLoacationForOrder(res);
			//缓存司机定位，用作司乘同显
			_this.cacheLoacationForMove(res);
			
			//把坐标实时传递到其他页面
			const location = {latitude:res.latitude,longitude:res.longitude};
			uni.$emit("locationChange",location)
			uni.setStorageSync("location",location);
		})
		
		
	},
	methods:{
		//接乘客，以及开始代驾，缓存位置用作司乘同显
		cacheLoacationForMove(res){
			//缓存定位，参数对象
			let _this = this;
			let orderNo = uni.getStorageSync("orderNo");
			
			let param = {
				latitude:res.latitude,
				longitude:res.longitude,
				orderNo:orderNo,
				status:0,	//状态区分是：接客户，还是开始代驾，这2中情况下都要缓存
			}
			
			if(uni.getStorageSync("workStatus") == '接客户'){
				//去接乘客，
				param.status = _this.Consts.OrderStatus.ORDER_STATUS_ARRIVE
			}else if(uni.getStorageSync("workStatus") == '开始代驾'){
				param.status = _this.Consts.OrderStatus.ORDER_STATUS_START_DRIVING
			}else{
				return;
			}
			_this.post("/driver/app/driver/cache/location",param,(resp)=>{
				
			})
		},
		//开始接单：缓存位置用作司机匹配订单
		cacheLoacationForOrder(res){
			let _this = this;
			if(uni.getStorageSync("workStatus") != '开始接单'){
				return;
			}
			//只在10s内才缓存，为了减少缓存频率
			// if(new Date().getSeconds() > 10 ){return;}
			
			//缓存定位，参数对象
			let param = {
				latitude:res.latitude,
				longitude:res.longitude,
			}
			_this.post("/driver/app/driver/geo",param,(res)=>{})
		},
		//初始化司机的工作状态：默认是停止接单
		initWorkStatus(){
			let _this = this;
			let workStatus = uni.getStorageSync("workStatus");
			if(!workStatus || workStatus == ''){
				console.log("司机状态设置为 : 停止接单");
				uni.setStorageSync("workStatus","停止接单")
			}
		}
	},
	onShow: function() {
	},
	onHide: function() {
	}
};
</script>

<style lang="scss">
@import 'uview-ui/index.scss';
</style>