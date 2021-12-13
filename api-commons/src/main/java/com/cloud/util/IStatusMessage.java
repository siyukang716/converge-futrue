package com.cloud.util;

/**
 * @项目名称： wyait-manage
 * @类名称： IStatusMessage
 * @类描述：响应状态信息
 * @创建时间： 2018年1月4日11:04:17
 * @version:
 */
public interface IStatusMessage {
	
	Integer getCode();

	String getMessage();
	
	public enum SystemStatus implements IStatusMessage{

		SUCCESS(1000,"SUCCESS"), //请求成功
		ERROR(1001,"ERROR"),	   //请求失败
		PARAM_ERROR(1002,"PARAM_ERROR"), //请求参数有误
		SUCCESS_MATCH(1003,"SUCCESS_MATCH"), //表示成功匹配
		NO_LOGIN(1100,"NO_LOGIN"), //未登录
		MANY_LOGINS(1101,"MANY_LOGINS"), //多用户在线（踢出用户）
		LOGIN_EXPIRED(1103,"LOGIN_EXPIRED"), //登录过期
		UPDATE(1102,"UPDATE"), //用户信息或权限已更新（退出重新登录）
		PARAM_NULL(1104,"PARAM_NULL"), //请求参数为空
		IMPORT_PARAM_FAILED(1105,"IMPORT_FAILED"), //导入参数校验失败
		LOCK(1111,"LOCK"); //用户已锁定
		private Integer code;
		private String message;

		private SystemStatus(Integer code,String message){
			this.code = code;
			this.message = message;
		}

		public Integer getCode(){
			return this.code;
		}

		public String getMessage(){
			return this.message;
		}
	}
}