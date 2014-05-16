package com.ym.chatclient.app;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jivesoftware.smack.XMPPConnection;

import com.ym.chatclient.util.CustomConst;
import com.ym.chatclient.xmpp.XmppConnectionManager;
import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class ChatApplication extends Application {

	private static ChatApplication instance = null;
	/**
	 * @Fields xmppConnection : 连接管理
	 */
	public static XMPPConnection xmppConnection ;
	/** 
	* @Fields friendsNames : 用户名管理
	*/  
	public static Map<String,String> friendsNames = new HashMap<String,String>();
	/**
     * 所有会话，以对话用户JID命名
     */
    public static Map<String,Object> mJIDChats = new TreeMap<String,Object>();

	public ChatApplication() {
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance = this;
		XmppConnectionManager.getInstance().new InitXmppConnectionTask(handler)
				.execute();

	}

	/** 
	* @return  
	* @retur  ChatApplication 
	* @Description: 全局实例
	*/
	public static ChatApplication getInstance() {
		return instance;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == CustomConst.XMPP_HANDLER_ERROR) {
				if (msg.arg1 == CustomConst.XMPP_ERROR_CONNETERROR) {
					Toast.makeText(getInstance(), "网络存在异常", Toast.LENGTH_SHORT);
				} else {
					Toast.makeText(getInstance(), "账号/密码错误", Toast.LENGTH_SHORT);
				}
			}

		};

	};
}
