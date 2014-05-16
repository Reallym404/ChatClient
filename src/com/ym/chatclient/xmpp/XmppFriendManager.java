package com.ym.chatclient.xmpp;

import java.util.ArrayList;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Roster;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import com.ym.chatclient.entity.UserInfo;
import com.ym.chatclient.util.Util;
import com.ym.chatclient.xmpp.listener.MRosterListener;

import android.util.Log;

import com.ym.chatclient.app.ChatApplication;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class XmppFriendManager {

	public String TAG = "System.out";
	XMPPConnection connection = ChatApplication.xmppConnection;
	Roster roster;
	VCard vCard;
	static XmppFriendManager xmppFriendManager;
	/** 
	* @Fields groupData :存放父列表数据 
	*/  
	//List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<String> groupData = new ArrayList<String>();
	/** 
	* @Fields childData :放子列表列表数据
	*/  
	//List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	//List<List<UserInfo>> childData = new ArrayList<List<UserInfo>>();
	Map<String, List<UserInfo>> childData = new HashMap<String, List<UserInfo>>();
	
	private XmppFriendManager() {
		xmppFriendManager = this;
	}

	/**
	 * @return
	 * @retur XmppFriendManager
	 * @Description: 实例
	 */
	public static XmppFriendManager getInstance() {
		if (xmppFriendManager == null) {
			xmppFriendManager = new XmppFriendManager();
		}
		return xmppFriendManager;
	}

	/**
	 * @param user
	 *            用户uid
	 * @return
	 * @retur VCard
	 * @Description: 获取用户个人信息类
	 */
	public VCard getVCard(String user) {
		vCard = new VCard();
		ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
				new org.jivesoftware.smackx.provider.VCardProvider());
		try {
			vCard.load(connection, user);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return vCard;
	}

	/**
	 * @param roster
	 * @return
	 * @retur List<RosterGroup>
	 * @Description: 获取群组
	 */
	public List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groups = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroups = roster.getGroups();
		Iterator<RosterGroup> ite = rosterGroups.iterator();
		while (ite.hasNext()) {
			groups.add(ite.next());
		}
		return groups;
	}

	/**
	 * @param roster
	 * @param groupName
	 * @return
	 * @retur List<RosterEntry>
	 * @Description: 某个群组的用户
	 */
	public List<RosterEntry> getEntiesByGroup(Roster roster, String groupName) {
		List<RosterEntry> enties = new ArrayList<RosterEntry>();
		RosterGroup group = roster.getGroup(groupName);
		Collection<RosterEntry> ent = group.getEntries();
		Iterator<RosterEntry> ite = ent.iterator();
		while (ite.hasNext()) {
			enties.add(ite.next());
		}
		return enties;
	}

	/**
	 * @return
	 * @retur List<UserInfo>
	 * @Description: 获取所有好友列表
	 */
	public List<UserInfo> getFriends() {
		List<UserInfo> friends = new ArrayList<UserInfo>();
		//List<UserInfo> friends
		if (connection != null) {
		//	Log.d(TAG, "--------------getFriends---------------");
			friends = getFriendsInfo(friends);
		}
		return friends;
	}
	
	/** 
	* @param friends
	* @return  
	* @retur  List<UserInfo> 
	* @Description: 所有好友信息 
	*/
	public List<UserInfo> getFriendsInfo(List<UserInfo> friends) {
		roster = connection.getRoster();
		roster.addRosterListener(new MRosterListener());
		List<RosterGroup> groups = getGroups(roster);
		for (RosterGroup rosterGroup : groups) {
			List<RosterEntry> entries = getEntiesByGroup(roster,
					rosterGroup.getName());
			Log.d(TAG, "==========group==========="+rosterGroup.getName()) ;
			groupData.add(rosterGroup.getName());
			for (RosterEntry rosterEntry : entries) {
				String avatar = null;
				ChatApplication.friendsNames.put(rosterEntry.getUser(), Util
						.NameDefaultValue(getVCard(rosterEntry.getUser())
								.getNickName(), rosterEntry.getName()));
				UserInfo userInfo = new UserInfo(rosterEntry.getUser(),
						Util.NameDefaultValue(getVCard(rosterEntry.getUser())
								.getNickName(), rosterEntry.getName()),
						rosterEntry.getName(), "", 1, 1, roster.getPresence(
								rosterEntry.getUser()).isAvailable() ? 0 : 1,
						"", "", Util.NameDefaultValue(
								rosterEntry.getStatus() == null ? null
										: rosterEntry.getStatus().toString(),
								"YY,尼玛!"));
				Log.d(TAG, "==========1===========" + userInfo.toString());
				friends.add(userInfo);
			}
		}
		return friends ;
	}
	
	/** 
	* @return  
	* @retur  List<UserInfo> 
	* @Description: 设置对应组的好友 
	*/
	public boolean getFriendsOfGroup() {
		//List<UserInfo> friends = new ArrayList<UserInfo>();
	//	Map<String, List<UserInfo>> childData1 = new HashMap<String, List<UserInfo>>();
		boolean flag = false ;
		if (connection != null) {
		//	Log.d(TAG, "--------------getFriends---------------");
			getFriendsInfoOfGroup();
			if(!childData.isEmpty() && !groupData.isEmpty()){
				setChildData(childData) ;
				setGroupData(groupData) ;
				flag = true ;
			}
		}
		return flag ;
	}

	/**
	 * @param friends
	 * @return
	 * @retur List<UserInfo>
	 * @Description: 用户信息
	 */
	public void getFriendsInfoOfGroup() {
		roster = connection.getRoster();
		roster.addRosterListener(new MRosterListener());
		List<RosterGroup> groups = getGroups(roster);
		List<UserInfo> friends = null ;
		for (RosterGroup rosterGroup : groups) {
			List<RosterEntry> entries = getEntiesByGroup(roster,
					rosterGroup.getName());
		//	Map<String, String> curGroupMap = new HashMap<String, String>();
			Log.d(TAG, "==========group==========="+rosterGroup.getName()) ;
			groupData.add(rosterGroup.getName());
			friends = new ArrayList<UserInfo>();
			//curGroupMap.put("groupName", rosterGroup.getName()) ;
		//	List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (RosterEntry rosterEntry : entries) {
				// Log.d(TAG, "==========1==========="+rosterEntry.getUser()) ;
			//	Map<String, String> curChildMap = new HashMap<String, String>();
			//	children.add(curChildMap);
				String avatar = null;
				// Log.d(TAG,
				// "==========2==========="+getVCard(rosterEntry.getUser()).getNickName()+"......"+rosterEntry.getName())
				// ;
				
				ChatApplication.friendsNames.put(rosterEntry.getUser(), Util
						.NameDefaultValue(getVCard(rosterEntry.getUser())
								.getNickName(), rosterEntry.getName()));
				UserInfo userInfo = new UserInfo(rosterEntry.getUser(),
						Util.NameDefaultValue(getVCard(rosterEntry.getUser())
								.getNickName(), rosterEntry.getName()),
						rosterEntry.getName(), "", 1, 1, roster.getPresence(
								rosterEntry.getUser()).isAvailable() ? 0 : 1,
						"", "", Util.NameDefaultValue(
								rosterEntry.getStatus() == null ? null
										: rosterEntry.getStatus().toString(),
								"YY,尼玛!"));
				Log.d(TAG, "==========1===========" + userInfo.toString());
				friends.add(userInfo);
			}
		//	childData.add(friends) ;
			childData.put(rosterGroup.getName(), friends) ;
			Log.d(TAG, "==========11==========="+friends.size());
		}
		return ;
	}

	public List<String> getGroupData() {
		return groupData;
	}

	public void setGroupData(List<String> groupData) {
		this.groupData = groupData;
	}

	public Map<String, List<UserInfo>> getChildData() {
		return childData;
	}

	public void setChildData(Map<String, List<UserInfo>> childData) {
		this.childData = childData;
	}

	
	
	

}
