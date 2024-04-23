package cn.itsource.utils;

/**
 * 用户状态类，记录用户在平台使用系统中所有的状态。
 */
public class BitStatesUtil {
	
	/**
	 * 手机认证成功
	 */
	public final static long OP_PHONE = 1L << 0;   //1
	/**
	 * 实名认证中
	 */
	public final static long OP_REAL_AUTHENTICATING = 1L << 1;  //2
	/**
	 * 完成实名
	 */
	public final static long OP_REAL_AUTHENTICATIONED = 1L << 2;  //4
	/**
	 * 完成人脸识别
	 */
	public final static long OP_FACE_RECOGNITIONED = 1L << 3;  //8
	/**
	 * 是否初始化支付密码
	 */
	public final static long OP_INIT_PAY_PASSWORD = 1L << 4; //16

	/**
	 * 完成协议签署
	 */
	public final static long OP_DRIVER_AGREEMENT = 1L << 5; //32

	/**
	 * 与运算
	 * @param states 用户当前状态值
	 * @param value  需要判断状态值
	 * @return 是否存在
	 */
	public static boolean hasState(long states,long value){
			return (states & value) == value;
	}

	
	/**
	 * 或运算
	 * @param states 已有状态值
	 * @param value  需要添加状态值
	 * @return 新的状态值
	 */
	public static long addState(long states,long value){
		if(hasState(states, value)){
			return states;
		}
		return (states | value); //或
	}
	
	/**
	 * 异或运算
	 * @param states 已有状态值
	 * @param value  需要删除状态值
	 * @return 新的状态值
	 */
	public static long removeState(long states,long value){
		if(!hasState(states, value)){
			return states;
		}
		return states ^ value; //异或
	}
	
}
