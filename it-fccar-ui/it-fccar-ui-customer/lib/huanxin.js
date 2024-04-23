import uniappsdk from "./uniapp-sdk-4.1.3.js";
const WebIM = wx.WebIM = uniappsdk;


WebIM.conn = new WebIM.connection({
	appKey: '1149230529160260#driver',//注意这里的 "K" 需大写
	isMultiLoginSessions: false, //是否可以登录多个，并在所有端上接收消息
	https: false, //是否使用 HTTPS
	url: 'wss://im-api-wechat.easemob.com/websocket', // socket server (3.0 SDK)
	apiUrl: 'https://a1.easemob.com',    // rest server
	heartBeatWait: 30000, //心跳间隔
	autoReconnectNumMax: 5, //自动重连次数
	useOwnUploadFun: false // 是否使用自己的上传方式（如将图片文件等上传到自己的服务器，构建消息时只传 URL）
});

export {WebIM};