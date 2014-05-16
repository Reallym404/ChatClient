package com.ym.chatclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.ym.chatclient.app.AppManager;
import com.ym.chatclient.entity.UserInfo;
import com.ym.chatclient.xmpp.XmppFriendManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 * @Description: 好友列表  
 */
public class FriendExpandableActivity extends Activity implements
		OnScrollListener {

	List<String> groupData = new ArrayList<String>();
//	List<List<UserInfo>> childData = new ArrayList<List<UserInfo>>();
	Map<String, List<UserInfo>> childData = new HashMap<String, List<UserInfo>>();
	FriendExpandableAdapter expandableAdapter ;
	ExpandableListView expandableList;
	private int indicatorGroupHeight;
	private int the_group_expand_position = -1;
	private int count_expand = 0;
	private Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
	RelativeLayout friend_group_top ;
	TextView friend_group_top_name ;
	private final int GET_FRIENDS_SUCCESS = 1 ;
	private final int GET_FRIENDS_FAIL = 2 ;
	
	public FriendExpandableActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		super.setContentView(R.layout.friends_lay) ;
		AppManager.getAppManager().addActivity(this) ;
		//handler.sendEmptyMessage(GET_FRIENDS) ;
		TextView title_text = (TextView) findViewById(R.id.title_text) ;
		title_text.setText("好友列表") ;
		handler.postAtTime(new GetFriendsRunnable(), 2000) ;
		expandableAdapter = new FriendExpandableAdapter(FriendExpandableActivity.this) ;
		expandableList = (ExpandableListView) this.findViewById(R.id.friends_list) ;
		View view = new View(this) ;
		expandableList.addHeaderView(view) ;
		expandableList.setAdapter(expandableAdapter) ;
		expandableList.setGroupIndicator(null) ;
		initViewGroup() ;
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				groupData = XmppFriendManager.getInstance().getGroupData() ;
				childData = XmppFriendManager.getInstance().getChildData() ;
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show() ;
				break; 
			default:
				break;
			}
			
		}
	} ;
	
	class GetFriendsRunnable implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			XmppFriendManager friendManager = XmppFriendManager.getInstance() ;
			boolean flag = friendManager.getFriendsOfGroup() ;
			if(flag){
				handler.sendEmptyMessage(GET_FRIENDS_SUCCESS) ;
			}else{
				handler.sendEmptyMessage(GET_FRIENDS_FAIL) ;
			}
		}
	}
	
	/** 
	*   
	* @retur  void 
	* @Description: 初始化group 
	*/
	public void initViewGroup(){
		
		/** 
		* @Description: 监听父节点打开的事件 
		*/
		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				the_group_expand_position = groupPosition;
				ids.put(groupPosition, groupPosition);
				count_expand = ids.size();
			}
		}) ;
		/** 
		* @Description: 监听父节点关闭的事件 
		*/
		expandableList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				ids.remove(groupPosition);
				expandableList.setSelectedGroup(groupPosition);
				count_expand = ids.size();
			}
		}) ;
		
		friend_group_top = (RelativeLayout) findViewById(R.id.friend_group_top) ;
		friend_group_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				friend_group_top.setVisibility(View.GONE) ;
				expandableList.collapseGroup(the_group_expand_position);
				expandableList.setSelectedGroup(the_group_expand_position);
			}
		}) ;
		friend_group_top_name = (TextView) findViewById(R.id.friend_group_top_name) ;
		//设置滚动事件
		expandableList.setOnScrollListener(this);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem==0){
			friend_group_top.setVisibility(View.GONE) ;
		}
		// 控制滑动时TextView的显示与隐藏
				int npos = view.pointToPosition(0, 0);
				if (npos != AdapterView.INVALID_POSITION) {
					long pos = expandableList.getExpandableListPosition(npos);
					int childPos = ExpandableListView.getPackedPositionChild(pos);
					final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
					if (childPos == AdapterView.INVALID_POSITION) {
						View groupView = expandableList.getChildAt(npos
								- expandableList.getFirstVisiblePosition());
						indicatorGroupHeight = groupView.getHeight();
					}
					if (indicatorGroupHeight == 0) {
						return;
					}
					// if (isExpanded) {
					if (count_expand > 0) {
						the_group_expand_position = groupPos;
						friend_group_top_name.setText(groupData.get(the_group_expand_position)
								);
						if (the_group_expand_position != groupPos||!expandableList.isGroupExpanded(groupPos)) {
							friend_group_top.setVisibility(View.GONE);
						} else {
							friend_group_top.setVisibility(View.VISIBLE);
						}
					}
					if (count_expand == 0) {
						friend_group_top.setVisibility(View.GONE);
					}
				}
				if (the_group_expand_position == -1) {
					return;
				}
				/**
				 * calculate point (0,indicatorGroupHeight)
				 */
				int showHeight = getHeight();
				MarginLayoutParams layoutParams = (MarginLayoutParams) friend_group_top
						.getLayoutParams();
				// 得到悬浮的条滑出屏幕多少
				layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
				friend_group_top.setLayoutParams(layoutParams);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
	
	private int getHeight() {
		int showHeight = indicatorGroupHeight;
		int nEndPos = expandableList.pointToPosition(0, indicatorGroupHeight);
		if (nEndPos != AdapterView.INVALID_POSITION) {
			long pos = expandableList.getExpandableListPosition(nEndPos);
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);
			if (groupPos != the_group_expand_position) {
				View viewNext = expandableList.getChildAt(nEndPos
						- expandableList.getFirstVisiblePosition());
				showHeight = viewNext.getTop();
			}
		}
		return showHeight;
	}
	
	/** 
	* @param state
	* @return  
	* @retur  String 
	* @Description:  用户状态
	*/
	public String getState(int state){
		return state == 0 ?"在线" : "隐身" ;
	}
	
	class FriendExpandableAdapter extends BaseExpandableListAdapter{

		FriendExpandableActivity expandableActivity ;
		private int mHideGroupPos = -1;
		
		public FriendExpandableAdapter(FriendExpandableActivity expandable){
			this.expandableActivity = expandable ;
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			//return childData.get(groupPosition).get(childPosition).getName().toString() ;
			return childData.get(groupData.get(groupPosition)).get(childPosition) ;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
			
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.friends_child_item, null);
			}
			TextView friend_child_name = (TextView) view.findViewById(R.id.friend_child_name) ;
			TextView friend_child_state = (TextView) view.findViewById(R.id.friend_child_state) ;
			TextView friend_child_sign = (TextView) view.findViewById(R.id.friend_child_sign) ;
			@SuppressWarnings("unused")
			BootstrapCircleThumbnail friend_child_head_image = (BootstrapCircleThumbnail) view.findViewById(R.id.friend_child_head_image) ;
			
			//friend_child_name.setText(childData.get(groupPosition).get(childPosition).getName().toString()) ;
			friend_child_name.setText(childData.get(groupData.get(groupPosition)).get(childPosition).getName().toString()) ;
			String state = "["+getState(childData.get(groupData.get(groupPosition)).get(childPosition).getState())+"]" ;
			friend_child_state.setText(state) ;
			friend_child_sign.setText(childData.get(groupData.get(groupPosition)).get(childPosition).getSign().toString()) ;
			
			return view ;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childData.get(groupData.get(groupPosition)).size() ;
			
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupData.get(groupPosition) ;
			
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupData.size() ;
			
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition ;
			
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if (view == null) {
				LayoutInflater inflaterGroup = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflaterGroup.inflate(R.layout.friends_group_item, null);
			}
			
			TextView friend_group_name = (TextView) view.findViewById(R.id.friend_group_name) ;
			ImageView friend_group_browser = (ImageView) view.findViewById(R.id.friend_group_browser) ;
			
			friend_group_name.setText(getGroup(groupPosition).toString()) ;
			if(isExpanded){
				friend_group_browser.setBackgroundResource(R.drawable.friend_group_browser2) ;
			}else{
				friend_group_browser.setBackgroundResource(R.drawable.friend_group_browser1) ;
			}
			
			return view;
			
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
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){	
			moveTaskToBack(true);	
			finish() ;
		}
		return super.onKeyDown(keyCode, event);
	}

}
