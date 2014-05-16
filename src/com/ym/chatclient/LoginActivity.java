package com.ym.chatclient;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smackx.packet.VCard;


import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.iangclifton.android.floatlabel.FloatLabel;
import com.ym.chatclient.app.AppManager;
import com.ym.chatclient.ui.ProgressWheel;
import com.ym.chatclient.util.CustomConst;
import com.ym.chatclient.xmpp.XmppConnectionManager;
import com.ym.chatclient.xmpp.XmppFriendManager;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
public class LoginActivity extends Activity {

	public String TAG = "System.out" ;
	private FloatLabel login_usrname, login_passwd;
	private BootstrapButton login_butn;
	private BootstrapCircleThumbnail login_userimage;
	private ProgressWheel progress;
	private RelativeLayout login_image_relative,login_new_user;
	private LinearLayout login_linear;
	private Shader shader;
	private Animation alphaAnimation, aAnimation;
	private Animator mCurrentAnimator;
	private boolean flag = false ;  // 登录成功否
	private XmppConnectionManager connectionManager = null ;
	private static final int INIT_ANIMATION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//getActionBar().setTitle("登入") ;
		super.setContentView(R.layout.login_lay);
		AppManager.getAppManager().addActivity(this) ;
		
		
		
		ShapeDrawable bg = new ShapeDrawable(new RectShape());
		int[] pixels = new int[] { 0x800080, 0x800080, 0x800080,
				0x800080, 0x800080,  0x0000FF,  0x0000FF,  0x0000FF }; 
		/*0xFF8E8E8E, 0xFF8E8E8E, 0xFF8E8E8E,
		0xFF8E8E8E, 0xFF8E8E8E, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF */
		Bitmap bm = Bitmap.createBitmap(pixels, 8, 1, Bitmap.Config.ARGB_8888);
		shader = new BitmapShader(bm, Shader.TileMode.REPEAT,
				Shader.TileMode.REPEAT);
		connectionManager = XmppConnectionManager.getInstance() ;
		
		initView() ;
		initAnimation() ;
		initEditText() ;
		initProgress() ;
		initLoginBtn() ;
		handler.sendEmptyMessageDelayed(INIT_ANIMATION, 200) ;
	}
	
	public void initView(){
		login_image_relative = (RelativeLayout) this
				.findViewById(R.id.login_image_relative);
		login_linear = (LinearLayout) this.findViewById(R.id.login_linear);
		login_new_user = (RelativeLayout) this.findViewById(R.id.login_new_user) ;
	//	TextView title_text = (TextView) this.findViewById(R.id.title_text) ;
	//	title_text.setText("登入") ;
		
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case INIT_ANIMATION:
				startInitAnimator();
				break;
			case CustomConst.XMPP_HANDLER_SUCCESS:
				Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show() ;
				startActivity(FriendExpandableActivity.class) ;
				finish() ;
				break ;
			case CustomConst.XMPP_HANDLER_ERROR:
				goBack() ;
				if (msg.arg1 == CustomConst.XMPP_ERROR_LOGINFAIL) {
					Toast.makeText(LoginActivity.this, "账号或者密码错误",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, "网络存在异常,请检查",
							Toast.LENGTH_SHORT).show();
					handler.postDelayed(new LoginRunnable(), 60000);
				}
				break ;
			default:
				break;
			}

		}
	};
	
	/** 
	*   
	* @retur  void 
	* @Description: 初始化头像动画
	*/
	private void initAnimation() {
		// TODO Auto-generated method stub
		alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
		alphaAnimation.setDuration(400);
	}
	
	/** 
	*   
	* @retur  void 
	* @Description: 开始动画 
	*/
	private void startInitAnimator() {
		// TODO Auto-generated method stub
		AnimatorSet set = new AnimatorSet();
		login_linear.setVisibility(View.VISIBLE);
		set.play(ObjectAnimator.ofFloat(login_linear, View.Y, 700, 400));
		set.setDuration(1000);
		set.setInterpolator(new DecelerateInterpolator());
		set.start();
	}

	public void initProgress() {
		progress = (ProgressWheel) login_image_relative
				.findViewById(R.id.login_progressBar);
		login_userimage = (BootstrapCircleThumbnail) login_image_relative.findViewById(R.id.login_userimage) ;
	}

	/** 
	*   
	* @retur  void 
	* @Description: 登入初始化 
	*/
	public void initLoginBtn() {
		login_butn = (BootstrapButton) this.findViewById(R.id.login_butn);
		login_butn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String userN_str = login_usrname.getEditText().getText().toString().trim() ;
				String userP_str = login_passwd.getEditText().getText().toString().trim() ;
				if(userN_str.equals("") || userP_str.equals("")){
					Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show() ;
					return ;
				}else{
					startLoginAnimation();  //  登录动画
					handler.postDelayed(new LoginRunnable(), 1000) ;
				}
				
			}

		});
	}

	/** 
	*   
	* @retur  void 
	* @Description: 编辑框
	*/
	public void initEditText() {
		login_usrname = (FloatLabel) this.findViewById(R.id.login_usrname);
		login_passwd = (FloatLabel) this.findViewById(R.id.login_passwd);
		// 输入框注册监听事件
		login_usrname.getEditText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String user_str = login_usrname.getEditText().getText().toString() ;
				if(!"".equals(user_str) && user_str.equals("test01")){
					login_userimage.setImage(R.drawable.head_image) ;
					login_userimage.setAnimation(alphaAnimation) ;
				}else{
					login_userimage.setImage(R.drawable.defalut_head_image) ;
				}
			}
		});
	}

	@SuppressLint("NewApi")
	public void startLoginAnimation() {
		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(login_image_relative, View.Y, 0, 450))
				.with(ObjectAnimator.ofFloat(login_linear, View.ALPHA, 1.0f,
						0.0f))
				.with(ObjectAnimator
						.ofFloat(login_butn, View.ALPHA, 1.0f, 0.0f));

		set.setDuration(1000);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				mCurrentAnimator = null;
				progress.setRimShader(shader);
				progress.spin();
				progress.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				mCurrentAnimator = null;
			}
		}) ;
		set.start() ;
		mCurrentAnimator = set;
	}
	
	protected void goBack() {
		// TODO Auto-generated method stub
	//	Toast.makeText(getActivity(), "密码不正确！登入失败", 0).show();
		progress.setVisibility(View.GONE);
		progress.stopSpinning();
		startBackAnimation();

	}
	
	private void startBackAnimation() {
		// TODO Auto-generated method stub

		AnimatorSet set = new AnimatorSet();
		set.play(ObjectAnimator.ofFloat(login_image_relative, View.Y, 450, 0))
				.with(ObjectAnimator.ofFloat(login_linear, View.ALPHA, 0.0f, 1.0f))
				.with(ObjectAnimator.ofFloat(login_butn, View.ALPHA, 0.0f, 1.0f));

		set.setDuration(1000);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
	}
	
	/** 
	* @author 叶铭
	* @Description: 登录线程
	*/
	class LoginRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String userN = login_usrname.getEditText().getText().toString().trim() ;
			String userP = login_passwd.getEditText().getText().toString().trim() ;
			Log.d(TAG, "-------------text---------"+userN+"=-----'"+userP) ;
			//flag = XmppConnectionManager.getInstance().mLogin(userN, userP, getApplicationContext(), handler) ;
			flag = connectionManager.mLogin(userN, userP, getApplicationContext(), handler) ;
			if(flag){
				//XmppConnectionManager.getInstance().setPresence(CustomConst.USER_STATE_BUSY) ;
				connectionManager.setPresence(CustomConst.USER_STATE_ONLINE) ;
				Log.d(TAG, "-------------user---------"+XmppConnectionManager.hostUid) ;
				/*VCard card = XmppFriendManager.getInstance().getVCard(XmppConnectionManager.hostUid) ;
				Log.d(TAG, "---------card-----------"+card.getNickName()) ;
				Log.d(TAG, "---------card-----------"+card.getField("sex")) ;
				Log.d(TAG, "---------card-----------"+card.getProperty("DESC")) ;*/
			//	handler.sendEmptyMessage(CustomConst.XMPP_HANDLER_SUCCESS);
				Message msg1 = new Message();
				msg1.what = CustomConst.XMPP_HANDLER_SUCCESS;
				handler.sendMessageDelayed(msg1, 2000) ;
			}else{
				Message msg = new Message();
				msg.what = CustomConst.XMPP_HANDLER_ERROR;
				msg.arg1 = CustomConst.XMPP_ERROR_LOGINFAIL;
				//handler.sendMessage(msg);
				handler.sendMessageDelayed(msg, 2000) ;
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/** 
	* @param clzss  
	* @retur  void 
	* @Description: 启动一个Activity
	*/
	public void startActivity(Class clzss){
		Intent intent = new Intent(this,clzss);
		finish();
		startActivity(intent);
	}

}
