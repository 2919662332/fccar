<template>
	<div v-if="isAuth(['ROOT', 'DRIVER:SELECT'])">
		<el-form :inline="true" :model="searchForm" :rules="dataRule" ref="searchForm">
			<el-form-item prop="name"><el-input v-model="searchForm.query.name" placeholder="姓名" size="medium" class="input" clearable="clearable" /></el-form-item>
			<el-form-item>
				<el-select v-model="searchForm.query.gender" class="input" placeholder="性别" size="medium" clearable="clearable">
					<el-option label="男" value="男" />
					<el-option label="女" value="女" />
				</el-select>
			</el-form-item>
			<el-form-item prop="phone"><el-input v-model="searchForm.query.phone" placeholder="电话" size="medium" class="input" clearable="clearable" /></el-form-item>
			<el-form-item prop="idNumber"><el-input v-model="searchForm.query.idNumber" placeholder="身份证" size="medium" clearable="clearable" /></el-form-item>
			<el-form-item>
				<el-select v-model="searchForm.query.realAuthStatus" class="input" placeholder="实名认证" size="medium" clearable="clearable">
					<el-option label="待审核" value="0" />
					<el-option label="认证成功" value="1" />
          <el-option label="认证失败" value="2" />
        </el-select>
			</el-form-item>
			<el-form-item><el-button size="medium" type="primary" @click="loadDataList()">查询</el-button></el-form-item>
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
			<el-table-column prop="content" header-align="center" align="center" type="expand">
				<template #default="scope" >
					<div class="content-container">
						<div class="left">
							<table class="content-table">
								<tbody>
									<tr>
										<th>开车驾龄</th>
										<th>生日</th>
										<th>邮箱</th>
										<th>收信地址</th>
										<th>身份证住址</th>
									</tr>
									<tr>
										<td>{{ scope.row.driverYear }}年</td>
										<td>{{ scope.row.birthday }}</td>
										<td>{{ scope.row.email }}</td>
										<td>{{ scope.row.mailAddress }}</td>
										<td>{{ scope.row.idcardAddress }}</td>
									</tr>
								</tbody>
							</table>
							<ul class="credentials-list">
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.idcardFront" :preview-src-list="scope.row.idcardFrontList" fit="cover" />
									<p>【 身份证正面 】</p>
								</li>
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.idcardBack" :preview-src-list="scope.row.idcardBackList" fit="cover" />
									<p>【 身份证背面 】</p>
								</li>
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.idcardHolding" :preview-src-list="scope.row.idcardHoldingList" fit="cover" />
									<p>【 手持身份证 】</p>
								</li>
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.drcardFront" :preview-src-list="scope.row.drcardFrontList" fit="cover" />
									<p>【 驾驶证正面 】</p>
								</li>
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.drcardBack" :preview-src-list="scope.row.drcardBackList" fit="cover" />
									<p>【 驾驶证背面 】</p>
								</li>
								<li class="item">
									<el-image style="width: 350px; height: 220px" :src="scope.row.drcardHolding" :preview-src-list="scope.row.drcardHoldingList" fit="cover" />
									<p>【 手持驾驶证 】</p>
								</li>
							</ul>
						</div>
						<div class="right" v-if="scope.row.realAuthStatus == '已认证'">
							<div class="summary-panel">
								<h3 class="title">【 代驾数据摘要 】</h3>
								<ul class="monthly-list">
									<li class="item">
										<p class="section">月订单金额</p>
										<p class="value">
											<span>￥</span>
											{{ scope.row.total }}
										</p>
									</li>
									<li class="item">
										<p class="section">月订单分账</p>
										<p class="value">
											<span>￥</span>
											{{ scope.row.income }}
										</p>
									</li>
									<li class="item">
										<p class="section">代扣个税</p>
										<p class="value">
											<span>￥</span>
											{{ scope.row.tax }}
										</p>
									</li>
								</ul>
								<ul class="statistics-list">
									<li class="item">
										<p class="section">总代驾单数</p>
										<p class="value">{{ scope.row.count }}</p>
									</li>
									<li class="item">
										<p class="section">好评总数</p>
										<p class="value">{{ scope.row.ct1 }}</p>
									</li>
									<li class="item">
										<p class="section">差评总数</p>
										<p class="value">{{ scope.row.ct2 }}</p>
									</li>
									<li class="item">
										<p class="section">差评总数</p>
										<p class="value">{{ scope.row.ct3 }}</p>
									</li>
								</ul>
							</div>
							<div id="chart"></div>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="name" header-align="center" align="center" min-width="80" label="姓名" />
			<el-table-column prop="gender" header-align="center" align="center" min-width="60" label="性别" :formatter="genderFormatter" />
			<el-table-column prop="phone" header-align="center" align="center" min-width="110" label="电话" />
			<el-table-column prop="idNumber" header-align="center" align="center" min-width="180" label="身份证" />
			<el-table-column prop="contactName" header-align="center" align="center" min-width="110" label="紧急联络人" />
			<el-table-column prop="contactPhone" header-align="center" align="center" min-width="110" label="联络人电话" />
			<el-table-column prop="realAuthStatus" header-align="center" align="center" min-width="130" label="实名认证">
        <template #default="scope" >
          <el-tag v-if="scope.row.realAuthStatus === 0" type="info">待审核</el-tag>
          <el-tag v-if="scope.row.realAuthStatus === 1" type="success">已认证</el-tag>
          <el-tag v-if="scope.row.realAuthStatus === 2" type="danger">已驳回</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="realAuthStatus" header-align="center" align="center" min-width="100" label="司机状态" >
        <template #default="scope" >
          <el-tag v-if="scope.row.realAuthStatus === 1" type="success">启用</el-tag>
          <el-tag v-if="scope.row.realAuthStatus === 0 || scope.row.realAuthStatus === 2" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
			<el-table-column header-align="center" align="center" width="250" label="操作">
				<template #default="scope">
					<el-button type="text" size="medium" v-if="isAuth(['ROOT', 'USER:UPDATE'])"
                     @click="showApproveModel(scope.row.id)" :disabled="scope.row.realAuthStatus !== 0">
						审批认证
					</el-button>
					<el-button type="text" size="medium" v-if="isAuth(['ROOT', 'USER:UPDATE'])"
                     @click="showRepealModel(scope.row.id)" :disabled="scope.row.realAuthStatus !== 1">
						撤销认证
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

		<el-dialog v-model="approveModelVisible" title="提示信息" width="400px" center>
			<div class="notice">你确定已经核准该代驾司机的实名认证？</div>
			<div>
				<el-input type="text" v-model="auditRemark" placeholder="填写审核备注" />
			</div>
			<template #footer>
				<span class="dialog-footer">
          <el-button type="primary" @click="approveHandle(true)">同意</el-button>
					<el-button type="danger" @click="approveHandle(false)">拒绝</el-button>
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
			cancelAuditRemark:"",
			auditRemark:"",
			searchForm: {
        query:{
          name: null,
          gender: null,
          phone: null,
          idNumber: null,
          realAuthStatus: null,
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
				year: null,
				birthday: null,
				email: null,
				mailAddress: null,
				idcardAddress: null,
				idcardFront: null,
				idcardFrontList: [],
				idcardBack: null,
				idcardBackList: [],
				idcardHolding: null,
				idcardHoldingList: [],
				drcardFront: null,
				drcardFrontList: [],
				drcardBack: null,
				drcardBackList: [],
				drcardHolding: null,
				drcardHoldingList: [],
				total: null,
				income: null,
				tax: null,
				count: null,
				ct1: null,
				ct2: null,
				ct3: null
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
    /**取消实名审批**/
    repealHandle(){
      let _this = this;
      let param = {
        id: _this.driverMaterialId, //审核ID
        remark: _this.cancelAuditRemark , //审核备注
        approve: true  //是否统一
      }
      //实名审核
      _this.post("/driver/client/realauth/cancel",param,(res)=>{
        let {success, message} = res;
        console.log(res);
        if(success){
          _this.repealModelVisible = false;
          _this.$message({ message: '取消成功', type: 'success', duration: 1200 });
          _this.loadDataList();
        }else{
          this.$message({ message: message, type: 'error', duration: 1200 });
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
    showApproveModel(driverMaterialId){
      this.driverMaterialId = driverMaterialId;
      this.approveModelVisible = true;
    },
    /**撤销认证的弹窗**/
    showRepealModel(driverMaterialId){
      this.driverMaterialId = driverMaterialId;
      this.repealModelVisible= true;
    },
    /** 格式化实名状态 **/
    realAuthFormatter(row){
      console.log(row)
      switch (row.realAuthStatus){
        case 0:return "审核中";
        case 1:return "已通过";
        case 2:return "已驳回";
      }
      return "未知";
    },
    /**格式化性别 **/
    genderFormatter(row){
      return row.gender?"男":"女";
    },
    //加载列表数据
		loadDataList(){
      let _this = this;
      for(var prop in _this.searchForm.query) {
        if (_this.searchForm.query[prop] === "") {
          _this.searchForm.query[prop] = null;
        }
      }
      _this.post("/driver/manager/driverAuthMaterial/pagelist",_this.searchForm,(res)=>{
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
@import url('driver.less');
</style>
