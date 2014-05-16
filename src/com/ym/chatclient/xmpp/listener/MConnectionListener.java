package com.ym.chatclient.xmpp.listener;

import org.jivesoftware.smack.ConnectionListener;
import com.ym.chatclient.app.ChatApplication;
import com.ym.chatclient.xmpp.XmppConnectionManager;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
 /**   
 * @author 叶铭   
 * @email yeming_1001@163.com
 * @version V1.0   
 * @Description: 连接监听器
 */
public class MConnectionListener implements ConnectionListener{

	private Context context;
	
	public MConnectionListener(Context context) {
		this.context = context ;
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		Toast.makeText(context, "连接断开。。。", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		// TODO Auto-generated method stub
		Looper.prepare();
		if(e.getMessage().equals("stream:error (conflict)")){
			Toast.makeText(context, "账号在别处登录", Toast.LENGTH_SHORT).show();
			ChatApplication.xmppConnection.disconnect();
			XmppConnectionManager.getInstance().closeConnection();
		//	ChatApplication.getInstance().exit();
			System.exit(0);
		}
		Looper.loop();
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub
		Looper.prepare();
		Toast.makeText(context, "连接断开。。。" + e.getMessage(), Toast.LENGTH_LONG).show();
		Looper.loop();
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		Looper.prepare();
		Toast.makeText(context, "重新连接成功。。。", Toast.LENGTH_LONG).show();
		Looper.loop();
	}

}
