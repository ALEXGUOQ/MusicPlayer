package com.zyh.musicplayer.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.zyh.musicplayer.domain.Music;

public class MediaUtils {

	public static int PLAYSTATE;
	public static int PROGRESS;
	public static int CURRENTPOS;

	public static List<Music> getSongList(Context context) {
		List<Music> songList = new ArrayList<Music>();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA }, null, null, null);
		while (cursor.moveToNext()) {
			Music music = new Music();
			music.setTitle(cursor.getString(0));
			music.setDuration(cursor.getString(1));
			music.setArtist(cursor.getString(2));
			music.setId(cursor.getString(3));
			music.setPath(cursor.getString(4));

			music.toString();

			songList.add(music);
		}
		cursor.close();
		return songList;
	}

	/**
	 * 把int格式化时间
	 */
	public static String formatTime(int time) {
		// 245995
		// time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * 将歌词的时间字符串转化成毫秒数
	 */
	static long String2Long(String strTime) {
		String beforeDot = new String("00:00:00");
		String afterDot = new String("0");

		// 将字符串按小数点拆分成整秒部分和小数部分。
		int dotIndex = strTime.indexOf(".");
		if (dotIndex < 0) {
			beforeDot = strTime;
		} else if (dotIndex == 0) {
			afterDot = strTime.substring(1);
		} else {
			beforeDot = strTime.substring(0, dotIndex);// 00:01:23
			afterDot = strTime.substring(dotIndex + 1); // 45
		}
		long intSeconds = 0;
		int counter = 0;
		while (beforeDot.length() > 0) {
			int colonPos = beforeDot.indexOf(":");
			try {
				if (colonPos > 0) {// 找到冒号了。
					intSeconds *= 60;
					intSeconds += Integer.parseInt(beforeDot.substring(0, colonPos));
					beforeDot = beforeDot.substring(colonPos + 1);
				} else if (colonPos < 0) {// 没找到，剩下都当一个数处理了。
					intSeconds *= 60;
					intSeconds += Integer.valueOf(beforeDot);
					beforeDot = "";
				} else {// 第一个就是冒号，不可能！
					return -1;
				}
			} catch (NumberFormatException e) {
				return -1;
			}
			++counter;
			if (counter > 3) {// 不会超过小时，分，秒吧。
				return -1;
			}
		}
		// intSeconds=83
		String totalTime = String.format("%d.%s", intSeconds, afterDot);// totaoTimer = "83.45"
		Double doubleSeconds = Double.parseDouble(totalTime); // 转成小数83.25
		return (long) (doubleSeconds * 1000);// 转成毫秒8345
	}

}
