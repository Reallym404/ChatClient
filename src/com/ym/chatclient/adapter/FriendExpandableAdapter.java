package com.ym.chatclient.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ym.chatclient.FriendExpandableActivity;
import com.ym.chatclient.entity.UserInfo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
 /**   
 * @author 叶铭   
 * @email yeming_1001@163.com
 * @version V1.0   
 */
public class FriendExpandableAdapter extends BaseExpandableListAdapter {

	FriendExpandableActivity expandableActivity ;
	@SuppressWarnings("unused")
	private int mHideGroupPos = -1;
	@SuppressWarnings("unused")
	private List<String> groupData = new ArrayList<String>();
	@SuppressWarnings("unused")
	private List<List<UserInfo>> childData = new ArrayList<List<UserInfo>>();
	
	public FriendExpandableAdapter() {
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;

	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;

	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;

	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;

	}
	
	public void hideGroup(int groupPos) {
		
	}

}
