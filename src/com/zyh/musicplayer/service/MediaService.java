package com.zyh.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.IBinder;

import com.zyh.musicplayer.util.ConstantValue;
import com.zyh.musicplayer.util.MediaUtils;

public class MediaService extends Service implements OnSeekCompleteListener, OnCompletionListener, OnErrorListener {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static MediaPlayer player;

	// private static ProgressTask task;
	private String file;
	private int position;

	@Override
	public void onCreate() {
		super.onCreate();
		if (player == null) {
			player = new MediaPlayer();
			player.setOnSeekCompleteListener(this);
			player.setOnCompletionListener(this);
			player.setOnErrorListener(this);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		int option = intent.getIntExtra("option", -1);
		int progress = intent.getIntExtra("progress", -1);
		if (progress != -1) this.position = progress;

		switch (option) {
			case ConstantValue.OPTION_PLAY:
				if (player.isPlaying()) player.stop();
				file = intent.getStringExtra("file");
				play(file);
				break;
			case ConstantValue.OPTION_PAUSE:
				position = player.getCurrentPosition();
				pause();
				break;

			default:
				break;
		}
		MediaUtils.PLAYSTATE = option;
	}

	private void pause() {
		player.pause();
	}

	private void play(String path) {
		if (player == null) player = new MediaPlayer();

		try {
			player.reset();
			player.setDataSource(path);
			player.prepare();
			player.start();

			// if(task=null){
			// task = new ProgressTask();
			// task.execute();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

}
