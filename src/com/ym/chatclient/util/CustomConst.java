package com.ym.chatclient.util;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class CustomConst {

	public CustomConst() {
	}

	// XMM连接Handler返回类型
	public static final int XMPP_HANDLER_ERROR = 500;
	public static final int XMPP_HANDLER_SUCCESS = 200;
	public static final int XMPP_ERROR_CONNETERROR = 0;
	public static final int XMPP_ERROR_LOGINFAIL = 1;

	// 用户状态
	public static final int USER_STATE_ONLINE = 0;
	public static final int USER_STATE_Q_ME = 1;
	public static final int USER_STATE_BUSY = 2;
	public static final int USER_STATE_AWAY = 3;
	public static final int USER_STATE_SETSELFOFFLINE = 4;
	public static final int USER_STATE_OFFLINE = 5;
}
