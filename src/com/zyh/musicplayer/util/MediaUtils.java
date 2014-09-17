package com.zyh.musicplayer.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.zyh.musicplayer.domain.Music;

public class MediaUtils {

	public static int PLAYSTATE;
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

	public static String formatTime(int time) {
		int min = time / (1000 * 60);
		String sec = time % (1000 * 60) + "";
		if (sec.length() < 2) {
			sec += "000";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}
}
