package com.zyh.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zyh.musicplayer.domain.Music;
import com.zyh.musicplayer.fragment.MusicListFragment;
import com.zyh.musicplayer.fragment.MusicListFragment.ItemSelectedListener;
import com.zyh.musicplayer.fragment.MusicPlayFragment;
import com.zyh.musicplayer.service.MediaService;
import com.zyh.musicplayer.util.ConstantValue;
import com.zyh.musicplayer.util.MediaUtils;

public class MainActivity extends FragmentActivity implements ItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private ViewPager viewpager;
	private MyPagerAdapter adpater;
	private List<Fragment> fragments;
	private List<String> titles;
	private Button btn_play, btn_stop, btn_pre, btn_next, btn_setting;

	private void init() {

		btn_play = (Button) findViewById(R.id.btn_play);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_pre = (Button) findViewById(R.id.btn_pre);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		setListener();

		if (MediaUtils.PLAYSTATE == ConstantValue.OPTION_PAUSE) {
			btn_play.setText("暂停");
		}
		initViewPager();
	}

	private void initViewPager() {
		titles = new ArrayList<>();
		titles.add("播放列表");
		titles.add("正在播放");
		getActionBar().setTitle(titles.get(0));

		fragments = new ArrayList<>();
		fragments.add(new MusicListFragment());
		fragments.add(new MusicPlayFragment());

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		adpater = new MyPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adpater);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				getActionBar().setTitle(titles.get(arg0));
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void setListener() {
		btn_play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (MediaUtils.PLAYSTATE) {
					case ConstantValue.OPTION_CONTINUE:
					case ConstantValue.OPTION_PLAY:
						startPlayService(null, ConstantValue.OPTION_PLAY);
						btn_play.setText("播放");
						break;
					case ConstantValue.OPTION_PAUSE:
						if (MediaUtils.CURRENTPOS >= 0 && MediaUtils.CURRENTPOS < MusicListFragment.songList.size()) {
							startPlayService(MusicListFragment.songList.get(MediaUtils.CURRENTPOS), ConstantValue.OPTION_PLAY);
						}
						btn_play.setText("暂停");
						break;
				}
			}
		});
	}

	public void startPlayService(Music music, int option) {
		Intent i = new Intent(getApplicationContext(), MediaService.class);
		if (music != null) i.putExtra("file", music.getPath());
		i.putExtra("option", option);
		startService(i);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelectedListener(int position) {
		System.out.println("ppppp:" + position);
		if (MediaUtils.CURRENTPOS == position) return;

		MediaUtils.CURRENTPOS = position;
		if (MediaUtils.CURRENTPOS >= 0 && MediaUtils.CURRENTPOS < MusicListFragment.songList.size()) {
			startPlayService(MusicListFragment.songList.get(MediaUtils.CURRENTPOS), ConstantValue.OPTION_PLAY);
		}
		btn_play.setText("暂停");
	}

}
