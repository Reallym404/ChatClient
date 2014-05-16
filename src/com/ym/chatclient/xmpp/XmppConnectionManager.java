package com.ym.chatclient.xmpp;

import java.io.IOException;
import java.util.Collection;

import org.jivesoftware.smack.AccountManager;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.packet.VCard;
import org.xbill.DNS.Master;

import com.ym.chatclient.app.ChatApplication;
import com.ym.chatclient.util.CustomConst;
import com.ym.chatclient.xmpp.listener.MConnectionListener;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class XmppConnectionManager {

	public String TAG = "System.out" ;
	private static XmppConnectionManager instance;
	private XMPPConnection connection;
	private ConnectionConfiguration config;
	private AccountManager accountManager;
	private ChatManager chatManager; // 回话管理
	private OfflineMessageManager offlineMessageManager; // 离线管理
	private ConnectionListener connectionListener; // 链接监听
	public static String hostUid ;
	private static String IP = "192.168.23.1";
	private static int PORT = 5222;

	public XmppConnectionManager() {
		instance = this;
	}

	/**
	 * @return
	 * @retur OfflineMessageManager
	 * @Description:获取离线信息管理者
	 */
	public OfflineMessageManager getOffMsgManager() {
		return offlineMessageManager;
	}

	/**
	 * @return
	 * @retur ChatManager
	 * @Description: 获取会话管理者
	 */
	public ChatManager getChatManager() {
		return chatManager;
	}

	/**
	 * @return
	 * @retur AccountManager
	 * @Description: 获取账户管理者
	 */
	public AccountManager getAccountManager() {
		return accountManager;
	}

	/**
	 * @return
	 * @retur ConnectionConfiguration
	 * @Description: 获取连接配置
	 */
	public ConnectionConfiguration getConnectionConfig() {
		return config;
	}

	/**
	 * @return
	 * @retur ConnectionListener
	 * @Description: 获取连接状态监听器
	 */
	public ConnectionListener getConnListener() {
		return connectionListener;
	}

	/**
	 * 
	 * @retur void
	 * @Description: 关闭连接
	 */
	public void closeConnection() {
		if (connection != null) {
			if (connection.isConnected()) {
				connection.removeConnectionListener(connectionListener);
				connection.disconnect();
			}
			connection = null;
		}
	}

	/**
	 * @return
	 * @throws XMPPException
	 * @retur XMPPConnection
	 * @Description:获取一个连接
	 */
	public XMPPConnection getConnection() throws XMPPException {
		if (connection == null || !connection.isConnected()) {
			closeConnection();
			connection = new XMPPConnection(config);
			config = new ConnectionConfiguration(IP, PORT);
			config.setSASLAuthenticationEnabled(false);
			config.setReconnectionAllowed(true);
			config.setSendPresence(true);
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
			connection = new XMPPConnection(config);
			connection.connect();
			accountManager = connection.getAccountManager();
			chatManager = connection.getChatManager();
			connectionListener = new MConnectionListener(ChatApplication.getInstance()) ;
			connection.addConnectionListener(connectionListener);
		}
		return connection;
	}

	/**
	 * @return
	 * @retur XmppConnectionManager
	 * @Description: 获取一个单例对象
	 */
	public static synchronized XmppConnectionManager getInstance() {
		if (instance == null) {
			instance = new XmppConnectionManager();
		}
		return instance;
	}

	/**
	 * @param name
	 * @param pass
	 * @param context
	 * @param handler
	 * @return
	 * @retur boolean
	 * @Description: 登录
	 */
	public boolean mLogin(String name, String pass, Context context,
			Handler handler) {
		boolean flag = false;
		if (connection == null || !connection.isConnected()) {
		//	Log.d(TAG, "-------------5---------") ;
			new InitXmppConnectionTask(handler).execute();
		//	Log.d(TAG, "-------------4---------") ;
			return false;
		}
		try {
			connection.login(name, pass);
			if (connection.getUser() == null) {
				return false;
			}
			
			/*Roster roster = connection.getRoster() ;
			Collection<RosterGroup> entriesGroup = roster.getGroups();  
            for(RosterGroup group: entriesGroup){  
                Collection<RosterEntry> entries = group.getEntries();  
                Log.i("---", group.getName());  
                for (RosterEntry entry : entries) {
                	Log.d(TAG, "------------------"+entry.getName()) ;
                }
            }*/
			/*VCard card = XmppFriendManager.getInstance().getVCard(connection.getUser()) ;
			Log.d(TAG, "---------card-----------"+card.getMiddleName()) ;
			Log.d(TAG, "---------card-----------"+card.getFirstName()) ;
			Log.d(TAG, "---------card-----------"+card.getNickName()) ;*/
			
			offlineMessageManager = new OfflineMessageManager(connection);
			hostUid = connection.getUser();
			return true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e.getMessage().equals("not-authorized(401)")) {
				handler.sendEmptyMessage(CustomConst.XMPP_ERROR_LOGINFAIL);
			}
			return false;
		}
		// return flag ;

	}

	/**
	 * @param code
	 * @retur void
	 * @Description: 设置用户发送状态
	 */
	public void setPresence(int code) {
		if (connection == null) {
			return;
		}
		Presence presence = null;
		switch (code) {
		// 设置在线
		case CustomConst.USER_STATE_ONLINE:
			presence = new Presence(Presence.Type.available);
			break;
		// Q我吧
		case CustomConst.USER_STATE_Q_ME:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			break;
		// 忙碌
		case CustomConst.USER_STATE_BUSY:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			break;
		// 离开
		case CustomConst.USER_STATE_AWAY:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			break;
		//
		case CustomConst.USER_STATE_SETSELFOFFLINE:
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(entry.getUser());
				presence.setTo(entry.getUser());
				connection.sendPacket(presence);
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(connection.getUser());
			presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
			break;
		case CustomConst.USER_STATE_OFFLINE:
			presence = new Presence(Presence.Type.unavailable);
			break;
		}
		connection.sendPacket(presence);
	}

	/**
	 * @author 叶铭
	 * @Description: 异步任务获取连接
	 */
	public class InitXmppConnectionTask extends
			AsyncTask<String, Void, Boolean> {

		private Handler handler;

		public InitXmppConnectionTask(Handler handler) {
			Log.d(TAG, "-------------0---------") ;
			this.handler = handler;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				connection = null;
				Log.d(TAG, "-------------1---------") ;
				ChatApplication.xmppConnection = getInstance().getConnection();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG, "-------------2---------") ;
				Message msg = new Message();
				if (e.getMessage().contains("XMPPError connection to")) {
					msg.what = CustomConst.XMPP_HANDLER_ERROR;
					msg.arg1 = CustomConst.XMPP_ERROR_CONNETERROR;
				} else {
					msg.what = CustomConst.XMPP_HANDLER_ERROR;
					msg.arg1 = CustomConst.XMPP_ERROR_CONNETERROR;
				}
				handler.sendMessage(msg);
				return false;
			}
			if (connection != null && accountManager != null
					&& chatManager != null) {
				Log.d(TAG, "-------------3---------") ;
				handler.sendEmptyMessage(CustomConst.XMPP_HANDLER_SUCCESS);
				return true;
			}

			return false;

		}

	}

}
