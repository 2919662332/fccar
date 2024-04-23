<template>
    <div class="page" >
        <el-row type="flex" justify="center" align="middle" class="container" v-loading="loading" element-loading-text="请稍等...">
            <el-col :lg="14" :xl="10" class="hidden-md-and-down">
                <el-row class="panel">
                    <el-col :span="14">
                        <div class="left" style="padding-top: 50px">
                          <img src="../assets/login/big-2.jpg" class="big" />
                        </div>
                    </el-col>
                    <el-col :span="10">
                        <div class="right">
                            <div class="title-container">
                                <h2>管理员请登录</h2>
                                <span>FCCAR</span>
                            </div>
                            <div v-if="!qrCodeVisible">
                                <div class="row">
                                    <el-input
                                        v-model="username"
                                        placeholder="用户名"
                                        prefix-icon="el-icon-user"
                                        clearable
                                    ></el-input>
                                </div>
                                <div class="row">
                                    <el-input
                                        type="password"
                                        v-model="password"
                                        placeholder="密码"
                                        prefix-icon="el-icon-lock"
                                        clearable
                                    ></el-input>
                                </div>
                                <div class="row">
                                    <el-button type="primary" class="btn" @click="login">登陆系统</el-button>
                                </div>
                                <div class="row"><a class="link" @click="changeMode">短信登陆</a></div>
                            </div>
                            <div v-if="qrCodeVisible">
                                <div class="qrCode-container">
                                    <img :src="qrCode" height="153" class="qrCode" />
                                    <img src="../assets/login/phone.png" height="148" />
                                </div>
                                <div class="row"><a class="link" @click="changeMode">用户名密码登陆</a></div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import 'element-plus/lib/theme-chalk/display.css';
import { isUsername, isPassword } from '../utils/validate.js';
import router from '../router/index.js';
export default {
    data: function() {
        return {
            username: "admin",
            password: "123456",
            qrCodeVisible: false,
            qrCode: '',
            uuid: null,
            qrCodeTimer: null,
            loginTimer: null,
            loading:false
        };
    },

    methods: {
        login: function() {
            let that = this;
            if (!isUsername(that.username)) {
                that.$message({
                    message: '用户名格式不正确',
                    type: 'error',
                    duration: 1200
                });
            } else if (!isPassword(that.password)) {
                that.$message({
                    message: '密码格式不正确',
                    type: 'error',
                    duration: 1200
                });
            } else {
                let data = { username: that.username, password: that.password };
                //发送登陆请求
                // that.loading = true;

                this.post("/uaa/client/login/admin",data,function(resp) {
                    console.log(resp.data)
                    if(resp.success){
                      localStorage.setItem('satoken',resp.data.satoken);
                      localStorage.setItem('permissions', JSON.stringify(resp.data.dbPermissions));
                      localStorage.setItem('login', JSON.stringify(resp.data.login));
                      router.push({ name: 'Home' });
                    }else{
                      that.$message({ message: '登陆失败', type: 'error', duration: 1200 });
                    }
                });
            }
        },
        changeMode: function() {
            let that = this;
            that.qrCodeVisible = !that.qrCodeVisible;
            //加载二维码图片
            if (that.qrCodeVisible) {
                that.loadQRCode();
                //创建刷新二维码定时器
                that.qrCodeTimer = setInterval(function() {
                    that.loadQRCode();
                }, 5 * 60 * 1000);
                that.loginTimer = setInterval(function() {
                    that.$http('user/wechatLogin', 'POST', { uuid: that.uuid }, true, function(resp) {
                        if (resp.result) {
                            clearInterval(that.qrCodeTimer);
                            clearInterval(that.loginTimer);
                            let permissions = resp.permissions;
                            localStorage.setItem('permissions', permissions);
                            router.push({ name: 'Home' });
                        }
                    });
                }, 5000);
            } else {
                //销毁刷新二维码定时器
                clearInterval(that.qrCodeTimer);
                clearInterval(that.loginTimer);
            }
        },
        //加载二维码图片的封装方法
        loadQRCode: function() {
            this.$http('user/createQrCode', 'GET', null, true, resp => {
                this.qrCode = resp.pic;
                this.uuid = resp.uuid;
            });
        }
    }
};
</script>

<style lang="less" scoped="scoped">
@import url('login.less');
</style>
