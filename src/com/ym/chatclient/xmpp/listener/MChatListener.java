package com.ym.chatclient.xmpp.listener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
 /**   
 * @author 叶铭   
 * @email yeming_1001@163.com
 * @version V1.0   
 */
public class MChatListener implements ChatManagerListener{

	public MChatListener() {
	}

	@Override
	public void chatCreated(Chat arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public class MsgProcessListener implements MessageListener{

		@Override
		public void processMessage(Chat arg0, Message arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
