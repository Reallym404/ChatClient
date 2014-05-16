package com.ym.chatclient.xmpp.listener;

import java.util.Collection;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import com.ym.chatclient.app.ChatApplication;
import com.ym.chatclient.util.CustomConst;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class MRosterListener implements RosterListener {

	public MRosterListener() {
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	@Override
	public void presenceChanged(Presence presence) {
		// TODO Auto-generated method stub
		String uid = presence.getFrom().split("/")[0];
		int type = 0;
		if (presence.getType().equals(Presence.Type.unavailable)) {
			// 删除与该人的当前会话，防止下次再通讯时导致会话不一致
			ChatApplication.mJIDChats.remove(uid);
			type = CustomConst.USER_STATE_OFFLINE;
		} else if (presence.getType().equals(Presence.Type.available)) {
			Presence.Mode mode = presence.getMode();
			if (mode.equals(Presence.Mode.chat)) {
				type = CustomConst.USER_STATE_Q_ME;
			} else if (mode.equals(Presence.Mode.dnd)) {
				type = CustomConst.USER_STATE_BUSY;
			} else if (mode.equals(Presence.Mode.away)) {
				type = CustomConst.USER_STATE_AWAY;
			} else {
				type = CustomConst.USER_STATE_ONLINE;
			}
		}
	}

}
