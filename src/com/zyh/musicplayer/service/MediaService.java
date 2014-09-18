package com.zyh.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.IBinder;

import com.zyh.musicplayer.util.ConstantValue;
import com.zyh.musicplayer.util.HandlerManager;
import com.zyh.musicplayer.util.MediaUtils;

public class MediaService extends Service implements OnSeekCompleteListener, OnCompletionListener, OnErrorListener {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static MediaPlayer player;

	// private static ProgressTask task;
	private String file;

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
		int option = intent.getIntExtra("option", ConstantValue.OPTION_STOP);
		file = intent.getStringExtra("file");
		System.out.println("option:" + option);
		switch (option) {
			case ConstantValue.OPTION_NEXT:
			case ConstantValue.OPTION_PRE:
			case ConstantValue.OPTION_PLAY:
				play(file);
				MediaUtils.PLAYSTATE = ConstantValue.OPTION_PLAY;
				break;
			case ConstantValue.OPTION_PAUSE:
				MediaUtils.PROGRESS = player.getCurrentPosition();// 保存进度
				System.out.println("保存进度：" + MediaUtils.PROGRESS);
				pause();
				MediaUtils.PLAYSTATE = option;
				break;
			case ConstantValue.OPTION_RESUME:
				System.out.println("设置进度：" + MediaUtils.PROGRESS);
				resume(file);
				MediaUtils.PLAYSTATE = ConstantValue.OPTION_PLAY;
				break;
			case ConstantValue.OPTION_STOP:
				stop();
				MediaUtils.PLAYSTATE = option;
				break;
			default:
				break;
		}

	}

	private void resume(String path) {
		if (MediaUtils.PROGRESS > 0 && MediaUtils.PROGRESS < player.getDuration()) player.seekTo(MediaUtils.PROGRESS);// 设置进度
		if (player != null) player.start();
	}

	private void stop() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		MediaUtils.PROGRESS = 0;// 重置当前播放进度
	}

	private void pause() {
		if (player != null && player.isPlaying()) player.pause();
	}

	private void play(String path) {
		if (player == null) player = new MediaPlayer();// 防止为空
		if (player.isPlaying()) player.stop();// 如果正在播放，停止当前
		// 播放一首新音乐
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
		stop();
		MediaUtils.PLAYSTATE = ConstantValue.OPTION_STOP;
		HandlerManager.getHandler().sendEmptyMessage(ConstantValue.END);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			player.release();
			player = null;
		}
		super.onDestroy();
	}
}
