package com.ym.chatclient.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

/**
 * @author 叶铭
 * @email yeming_1001@163.com
 * @version V1.0
 */
public class FileManager {

	public FileManager() {
	}

	/**
	 * @return
	 * @retur boolean
	 * @Description: 判断SD卡
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	/**
	 * @return
	 * @retur String
	 * @Description: 根目录 路径
	 */
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
																				// /data/data/
		}
	}

	/**
	 * @return
	 * @retur String
	 * @Description: 图片目录
	 */
	public static String getSaveFilePath() {
		if (hasSDCard()) {
			return getRootFilePath() + "jiujiangeyou/images/";
		} else {
			return getRootFilePath() + "jiujiangeyou/images/";
		}
	}

	/**
	 * @param path
	 *            文件路径
	 * @param inputStream
	 * @return
	 * @retur boolean
	 * @Description: 将数据流写入指定的文件
	 */
	@SuppressWarnings("resource")
	public static boolean writeFile(String path, InputStream inputStream) {
		int len = 0;
		BufferedOutputStream bos = null;
		byte[] buff = new byte[2014];
		try {
			bos = new BufferedOutputStream(new FileOutputStream(new File(path)));
			while ((len = inputStream.read(buff)) > 0) {
				bos.write(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
