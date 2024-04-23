
export let BitStateUtil = {
	OP_PHONE:1 << 0,
	OP_REAL_AUTHENTICATING : 1 << 1,
	OP_REAL_AUTHENTICATIONED : 1 << 2,

	hasState:function(bitState,state){ 
		return (bitState | state) > 0;
	}
}