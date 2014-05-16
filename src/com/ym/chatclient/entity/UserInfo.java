package com.ym.chatclient.entity;

import java.io.Serializable;
 /**   
 * @author 叶铭   
 * @email yeming_1001@163.com
 * @version V1.0   
 * @Description: 好友列表框显示的好友信息  
 */
public class UserInfo implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/  
	
	private static final long serialVersionUID = 1L;
	
	/** 
	* @Fields uid :  ID
	*/  
	private String uid;
	/**
	 * 昵称
	 */
	private String nick;
	/** 
	* @Fields name : 用户名 
	*/  
	private String name ;
	/**
	 * 心情
	 */
	private String mood;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 年龄
	 */
	private int age;
	/**
	 * 当前登录状态    0-在线，1-隐身
	 */
	private int state;
	/**
	 * 首字符拼音
	 */
	private String py;
	/**
	 * 头像
	 */
	//private int user_head;
	private String avatar;
	
	/** 
	* @Fields sign :签名
	*/  
	private String sign ;

	public UserInfo() {
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserInfo [uid=" + uid + ", nick=" + nick + ", name=" + name
				+ ", mood=" + mood + ", sex=" + sex + ", age=" + age
				+ ", state=" + state + ", py=" + py + ", avatar=" + avatar
				+ ", sign=" + sign + "]";
	}

	public UserInfo(String uid, String nick, String name, String mood, int sex,
			int age, int state, String py, String avatar, String sign) {
		this.uid = uid;
		this.nick = nick;
		this.name = name;
		this.mood = mood;
		this.sex = sex;
		this.age = age;
		this.state = state;
		this.py = py;
		this.avatar = avatar;
		this.sign = sign;
	}

	
	

	

	
	
	

}
