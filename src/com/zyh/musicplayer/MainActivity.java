package com.zyh.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.zyh.musicplayer.fragment.MusiclistFragment;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private ViewPager viewpager;
	private MyPagerAdapter adpater;
	private List<Fragment> fragments;

	private void init() {

		fragments = new ArrayList<>();
		fragments.add(new MusiclistFragment());

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		adpater = new MyPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adpater);
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

}
