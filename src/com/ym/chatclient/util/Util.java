package com.ym.chatclient.util;

import android.util.Log;
 /**   
 * @author 叶铭   
 * @email yeming_1001@163.com
 * @version V1.0   
 */
public class Util {
	
	public static String TAG = "System.out" ;
	
	public Util() {
	}

	/** 
	* @param srcStr
	* @param defaultValue
	* @return  
	* @retur  String 
	* @Description: 用户管理者 默认用户名 
	*/
	public static String NameDefaultValue(String srcStr,String defaultValue){
		Log.d(TAG, "-------------NameDefaultValue----------"+srcStr+"//////---"+defaultValue) ;
		return (srcStr == null || "".equals(srcStr)) ? defaultValue
		: srcStr;	
	}
}
