<template>
	<div v-if="isAuth(['ROOT', 'DRIVER:SELECT'])">
		<el-form :inline="true" :model="searchForm" :rules="dataRule" ref="searchForm">
			<el-form-item prop="name"><el-input v-model="searchForm.query.name" placeholder="姓名" size="medium" class="input" clearable="clearable" /></el-form-item>
			<el-form-item>
				<el-select v-model="searchForm.query.status" class="input" placeholder="状态" size="medium" clearable="clearable">
					<el-option label="状态" value="0" />
					<el-option label="有效" value="1" />
          <el-option label="作废" value="2" />
        </el-select>
			</el-form-item>
			<el-form-item><el-button size="medium" type="primary" @click="loadDataList()">查询</el-button></el-form-item>
			<el-form-item><el-button size="medium" type="primary" @click="showAddAggrement()">上传合同</el-button></el-form-item>
		</el-form>
		<el-table
			:data="dataList"
			border
			v-loading="dataListLoading"
			cell-style="padding: 2px 0"
			style="width: 100%;"
			size="medium"
			@expand-change="expand"
			:row-key="getRowKeys"
			:expand-row-keys="expands"
		>
			<el-table-column prop="name" header-align="center" align="center" min-width="80" label="姓名" />
			<el-table-column prop="phone" header-align="center" align="center" min-width="80" label="电话" />
			<el-table-column prop="idCardNum" header-align="center" align="center" min-width="80" label="身份证号" />
			<el-table-column prop="agreementSn" header-align="center" align="center" min-width="80" label="合同编号" />
			<el-table-column header-align="center" align="center" min-width="130" label="合同状态">
        <template #default="scope" >
          <el-tag v-if="scope.row.status === '0' " type="info">待审核</el-tag>
          <el-tag v-if="scope.row.status === '1' " type="success">已通过</el-tag>
          <el-tag v-if="scope.row.status === '2' " type="danger">已驳回</el-tag>
        </template>
      </el-table-column>

			<el-table-column header-align="center" align="center" width="250" label="操作">
        <template #default="scope">
					<el-button type="text" size="medium" v-if="isAuth(['ROOT', 'USER:UPDATE'])"
                     @click="showApproveModel(scope)">
						查看合同
					</el-button>
					<el-button :disabled="scope.row.status === '2' " type="text" size="medium" v-if="isAuth(['ROOT', 'USER:UPDATE'])"
                     @click="showRepealModel(scope)">
						合同作废
					</el-button>
        </template>
			</el-table-column>
		</el-table>
		<el-pagination
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
			:current-page="searchForm.page"
			:page-sizes="[10, 20, 50]"
			:page-size="searchForm.rows"
			:total="totalCount"
			layout="total, sizes, prev, pager, next, jumper"
		></el-pagination>

    <el-dialog v-model="addAggrement" title="提示信息" width="400px" center>
      <div class="notice">司机合同</div>
      <div>
        姓名：<el-input type="text"  v-model="name"  placeholder="姓名" />
      </div>
      <div>
        合同编号： <el-input :disabled="true" type="text"  v-model="agreementSn"  placeholder="自动生成合同编号，无需填写" />
      </div>
      <!--文件上传-->
      <el-upload
          class="upload-demo"
          action="http://localhost:10010/fccar/driver/manager/aggrementSelf/importExcel"
          :before-remove="beforeRemove"
          :on-success="handleSuccess"
          multiple
          :limit="1"
          :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="addAggrement = false">取消</el-button>
					<el-button type="primary" @click="repealHandle">确定</el-button>
				</span>
      </template>
    </el-dialog>

		<el-dialog v-model="repealModelVisible" title="提示信息" width="400px" center>
			<div class="notice">你确定取消该代驾司机的实名认证？</div>
			<div>
				<el-input type="text"  v-model="cancelAuditRemark"  placeholder="填写审核备注" />
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="repealModelVisible = false">取消</el-button>
					<el-button type="primary" @click="repealHandle">确定</el-button>
				</span>
			</template>
		</el-dialog>
		<update ref="update" @refreshDataList="loadDataList"></update>
	</div>
</template>
<script>
import $ from 'jquery';
import request from '../utils/request'
import Update from './driver-update.vue';
import {ElMessage} from "element-plus";
export default {
	components: {
		Update
	},
	data() {
		return {
      addAggrement:false,
      aggeUrl:'',
      visAble:false,
      selectedFile:[],

      id:'',
			name:"",
      agreementSn:"",

			auditRemark:"",
			searchForm: {
        query:{
          id:null,
          name: null,
          phone: null,
          idCardNum: null,
          status: null,
          aggeUrl:null,
          agreementSn:null
        },
        page: 1,
        rows: 10
			},
			chart: '',
			dataList: [],
			totalCount: 0,
			dataListLoading: false,
			dataRule: {
				name: [{ required: false, pattern: '^[\u4e00-\u9fa5]{1,10}$', message: '姓名格式错误' }],
				phone: [{ required: false, pattern: '^1\\d{10}$', message: '电话格式错误' }],
				idNumber: [{ required: false, pattern: '^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$', message: '身份证格式错误' }]
			},
			expands: [],
			content: {
				name:null,
			},
			approveModelVisible: false,
			repealModelVisible: false,
			driverMaterialId: null,
			getRowKeys(row) {
				return row.id;
			}
		};
	},
	methods: {
    generateRandomSN() {
      // 生成一个 6 位随机数字字符串
      const randomDigits = Math.floor(100000 + Math.random() * 900000).toString();
      // 拼接 "SN" 和随机数字字符串
      const sn = 'SN' + randomDigits;
      return sn;
    },
    handleSuccess(res, file, fileList){
      let _this = this;
      console.log(res.data)
      _this.aggeUrl = res.data
    },
    showAddAggrement(){
      let _this = this;
      _this.addAggrement = true;
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },

    updateDriverStatus(driverMaterialId){
      this.$confirm("确定要修改司机状态？").then(()=>{
        this.sendPost("/driver/driver/changestatus/"+driverMaterialId,(res)=>{
          let {success,message,result} = res;
          if(success){
            this.$message({ message: '修改成功', type: 'success', duration: 1200 });
            this.loadDataList();
          }else{
            this.$message({ message: message, type: 'error', duration: 1200 });
          }
        });
      });
    },
    dialogVisibles(){
      this.dialogVisible = true;
    },
    /**取消实名审批**/
    repealHandle(){
      let _this = this;
      let agreementSn =  _this.generateRandomSN();
      let param = {
        name: _this.name, //姓名
        agreementSn: agreementSn , //合同编号
        status:'0',
        aggeUrl: _this.aggeUrl,
      }
      //实名审核
      _this.post("/driver/manager/aggrementSelf/addAggrement",param,(res)=>{
        let {success, message} = res;
        console.log(res);
        if(success){
          _this.$message({ message: '取消成功', type: 'success', duration: 1200 });
          _this.loadDataList();
          _this.visAble = false;
          _this.id = res.data.id;
          _this.searchForm.queryid = res.data.id;
        }else{
          this.$message({ message: message, type: 'error', duration: 1200 });
          _this.visAble = false;
          _this.loadDataList();
        }
      });
    },
    /** 实名审批 **/
    approveHandle(approve){
      let _this = this;
      let param = {
        id: _this.driverMaterialId, //审核ID
        remark: _this.auditRemark , //审核备注
        approve: approve  //是否同意
      }
      //实名审核
      _this.post("/driver/client/realauth/audit",param,(res)=>{
        let {success, message} = res;
        console.log(res);
        if(success){
          _this.approveModelVisible = false;
          _this.$message({ message: '审核成功', type: 'success', duration: 1200 });
          _this.loadDataList();
        }else{
          this.$message({ message: message, type: 'error', duration: 1200 });
        }
      });

    },
    /** 实名审核弹窗 **/
    showApproveModel(scope){
      console.log(scope)
      window.open(
          "https://view.officeapps.live.com/op/view.aspx?src="+ scope.row.aggeUrl,
          "_blank"
      );
    },
    /**作废合同**/
    showRepealModel(scope){
      let _this = this;
      const params = {
        id: scope.row.id,
      };
      _this.post("/driver/manager/aggrementSelf/updateAggrement",params,(res)=>{
        _this.searchForm.query.status = res.data.status
      })
      _this.loadDataList()
    },
    /** 格式化实名状态 **/
    realAuthFormatter(row){
      console.log(row)
      switch (row.status){
        case 0:return "审核中";
        case 1:return "已通过";
        case 2:return "已驳回";
      }
      return "未知";
    },
    //加载列表数据
		loadDataList(){
      let _this = this;
      for(var prop in _this.searchForm.query) {
        if (_this.searchForm.query[prop] === "") {
          _this.searchForm.query[prop] = null;
        }
      }
      _this.post("/driver/manager/aggrementSelf/pagelist",_this.searchForm,(res)=>{
        let {success,data} = res;
        if(success){
          _this.totalCount = data.total;
          _this.dataList = data.rows;
        }
      });
    }
	},
	created: function() {
    this.loadDataList();
	}
};
</script>
<style lang="less" scoped="scoped">
@import url('aggrement.less');
</style>
