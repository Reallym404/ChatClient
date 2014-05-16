package com.ym.chatclient;

import java.util.List;

import com.ym.chatclient.entity.UserInfo;
import com.ym.chatclient.xmpp.XmppFriendManager;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	public String TAG = "System.out" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		List<UserInfo> s = null ;
		XmppFriendManager manager = XmppFriendManager.getInstance() ;
		Log.d(TAG, "--------MainActivity------------") ;
	//	s = manager.getFriends() ;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
