package com.zyh.musicplayer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zyh.musicplayer.R;
import com.zyh.musicplayer.util.MediaUtils;

public class MusicPlayFragment extends Fragment {

	private OnSetProgressListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnSetProgressListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_play, container, false);
		init(view);
		return view;
	}

	private SeekBar sb;
	private TextView tv_cur, tv_total, tv_title;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// System.out.println("currentTime" + currentTime);
			float p = (float) (1000.0 * currentTime / (float) totleTime);
			// System.out.println("p:" + p);
			sb.setProgress((int) p);
			currentTime++;
			if (currentTime > totleTime) return;
			setCurrentTime(currentTime);
			handler.sendEmptyMessageDelayed(200, 1000);
		};
	};
	private int currentTime;
	private int totleTime;

	private void init(View view) {
		sb = (SeekBar) view.findViewById(R.id.sb);
		tv_cur = (TextView) view.findViewById(R.id.tv_cur);
		tv_total = (TextView) view.findViewById(R.id.tv_total);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				System.out.println("ppppppp:" + seekBar.getProgress());
				currentTime = (int) ((seekBar.getProgress() / 1000.0) * totleTime);
				listener.onsetProgress(seekBar.getProgress() * totleTime);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}

	/**
	 * 设置当前音乐已经播放的时间
	 * @param time
	 */
	public void setCurrentTime(int time) {
		tv_cur.setText(MediaUtils.formatTime(time));
	}

	/**
	 * 设置当前音乐总时间
	 * @param time
	 */
	public void setTotalTime(int time) {
		System.out.println("tiem:" + time);
		this.totleTime = time / 1000; // 将ms值转成秒数
		currentTime = 0;
		tv_total.setText(MediaUtils.formatTime(totleTime));
		setCurrentTime(currentTime);
		handler.removeMessages(200);
		handler.sendEmptyMessage(200);
	}

	/**
	 * 设置当前音乐名称
	 * @param time
	 */
	public void setName(String name) {
		tv_title.setText(name);
	}

	public interface OnSetProgressListener {
		public void onsetProgress(int progress);
	}
}
