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
				position = player.getCurrentPosition();// �������
				System.out.println("������ȣ�" + position);
				pause();
				MediaUtils.PLAYSTATE = option;
				break;
			case ConstantValue.OPTION_RESUME:
				player.seekTo(position);// ���ý���
				System.out.println("���ý��ȣ�" + position);
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
		player.start();
	}

	private void stop() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		position = 0;// ���õ�ǰ���Ž���
	}

	private void pause() {
		player.pause();
	}

	private void play(String path) {
		if (player == null) player = new MediaPlayer();// ��ֹΪ��
		if (player.isPlaying()) player.stop();// ������ڲ��ţ�ֹͣ��ǰ
		// ����һ��������
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
