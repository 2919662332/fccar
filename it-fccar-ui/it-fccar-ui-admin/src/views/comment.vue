<template>
	<div v-if="isAuth(['ROOT', 'COMMENT:SELECT'])">
		<el-form :inline="true" :model="dataForm" :rules="dataRule" ref="dataForm">
			<el-form-item prop="orderNo">
				<el-input v-model="dataForm.orderNo" placeholder="订单编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item prop="driverId">
				<el-input v-model="dataForm.driverId" placeholder="司机编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item prop="customerId">
				<el-input v-model="dataForm.customerId" placeholder="乘客编号" size="medium" clearable="clearable" />
			</el-form-item>
			<el-form-item>
				<el-select v-model="dataForm.rate" class="input" placeholder="评价" size="medium" clearable="clearable">
					<el-option label="好评" value="好评" />
					<el-option label="中评" value="中评" />
					<el-option label="差评" value="差评" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-select
					v-model="dataForm.status"
					class="input"
					placeholder="状态"
					size="medium"
					clearable="clearable"
				>
					<el-option label="未申诉" value="0" />
					<el-option label="已申诉" value="1" />
					<el-option label="申诉成功" value="2" />
					<el-option label="申诉失败" value="3" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-date-picker
					v-model="date"
					type="daterange"
					range-separator="~"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					size="medium"
				></el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-button size="medium" type="primary" @click="searchHandle()">查询</el-button>
			</el-form-item>
		</el-form>
		<el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            cell-style="padding: 4px 0"
            style="width: 100%;"
            size="medium"
            @expand-change="expand"
            :row-key="getRowKeys"
            :expand-row-keys="expands"
        >
            <el-table-column prop="content" header-align="center" align="center" type="expand">
                <template #default="scope">
                    <div class="order-table">
                        <div class="row">
                            <div class="th">【乘客评价】</div>
                            <div class="td">{{ scope.row.remark }}</div>
                        </div>
                        <div class="row" v-show="scope.row.status != 0">
                            <div class="th">【申诉理由】</div>
                            <div class="td">{{ scope.row.driverReason }}</div>
                        </div>
                        <div class="row" v-show="scope.row.status == 3">
                            <div class="th">【处理意见】</div>
                            <div class="td">{{ scope.row.note }}</div>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column type="index" header-align="center" align="center"  label="序号">
                <template #default="scope">
                    <span>{{ (page - 1) * rows + scope.$index + 1 }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="orderNo" header-align="center" align="center"  width="200" label="订单编号" />
            <el-table-column prop="driverName" header-align="center" align="center"  label="司机" />
            <el-table-column prop="customerName" header-align="center" align="center"  label="乘客" />
            <el-table-column prop="rate" header-align="center" align="center" min-width="40" label="评分">
                <template #default="scope">
                    <span v-show="scope.row.rate > 3" class="comment-rate-green">好评</span>
                    <span v-show="scope.row.rate == 3" class="comment-rate-orange">中评</span>
                    <span v-show="scope.row.rate < 3" class="comment-rate-red">差评</span>
                </template>
            </el-table-column>
            <el-table-column prop="statusName" header-align="center" align="center" min-width="80" label="状态" />
            <el-table-column prop="createTime" header-align="center" align="center" min-width="80" label="日期" />
            <el-table-column prop="auditLoginUsername" header-align="center" align="center" min-width="120" label="申诉受理人" />
            <el-table-column header-align="center" align="center" width="240" label="操作">
                <template #default="scope">
                    <el-button
                        type="text"
                        size="small"
                        v-if="isAuth(['ROOT', 'COMMENT:APPROVAL'])"
                        @click="showAcceptModel(scope.row.id)"
                        :disabled="!(scope.row.status ==1 && scope.row.userId == null)">
                        受理
                    </el-button>

                </template>
            </el-table-column>
        </el-table>
		<el-pagination
			@size-change="sizeChangeHandle"
			@current-change="currentChangeHandle"
			:current-page="page"
			:page-sizes="[10, 20, 50]"
			:page-size="rows"
			:total="totalCount"
			layout="total, sizes, prev, pager, next, jumper"
		></el-pagination>

		<el-dialog v-model="handleVisible" title="提示信息" width="350px" center>
			<div class="notice">你确定对代驾司机的申述进行审核？</div>
			<el-input
				v-model="note"
				:rows="3"
				type="textarea"
				placeholder="输入审核备注"/>

			<template #footer>
				<span class="dialog-footer">
					<el-button type="success" size="medium" @click="handleAppeal(true)">申述通过</el-button>
					<el-button type="danger" size="medium" @click="handleAppeal(false)">申述驳回</el-button>
					<el-button size="medium" @click="handleVisible = false">取消</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script>
import $ from 'jquery';
export default {
	components: {},
	data() {
		return {
			dataForm: {
				orderNo: null,
				driverId: null,
				customerId: null,
				rate: null,
				status: null,
			},
			date: [],
			page: 1,
			rows: 10,
			dataList: [],
			totalCount: 0,

			dataListLoading: false,
			dataRule: {
				orderNo: [{ required: false, message: '订单编号格式错误' }],
				driverId: [{ required: false, message: '司机编号格式错误' }],
				customerId: [{ required: false, message: '客户编号格式错误' }]
			},
			expands: [],
			content: {},
			handleVisible: false,
			commentId: null,
			note: null,
			getRowKeys(row) {
				return row.orderId;
			}
		};
	},
	methods: {
		handleAppeal(approve){
			let _this = this;
			let param = {
				commentId:_this.commentId,
				approve:approve,
				auditDesc:_this.note
			}
			_this.sendPost("/order/orderComment/audit",param,(res)=>{
				let {success,code,message,result} = res;
				if(success){
					this.$message({ message: '审批成功', type: 'success', duration: 1200 });
					_this.loadTableData();
					this.handleVisible = false;
				}
			})

		},
		showAcceptModel(commentId){
			this.handleVisible = true;
			this.commentId = commentId;
		},
		searchHandle(){
			let _this = this;
			_this.loadTableData();
		},
		loadTableData(){
			let _this = this;
			let param = {
				rows:_this.rows,
				page:_this.page,
				query:_this.dataForm,
				date:_this.date
			}
			_this.sendPost("/order/orderComment/pagelist",param,(res)=>{
						console.log(res);
						let {success,code,message,result} = res;

						if(success){
							_this.dataList = result.rows;
							_this.totalCount = result.total;
						}
			})

		}
	},
	created: function() {
		let _this = this;
		_this.loadTableData();
	}
};
</script>
<style lang="less" scoped="scoped">
@import url('comment.less');
</style>
