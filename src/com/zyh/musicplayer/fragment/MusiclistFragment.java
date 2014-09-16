package com.zyh.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zyh.musicplayer.R;
import com.zyh.musicplayer.domain.Music;
import com.zyh.musicplayer.util.MediaUtils;

public class MusiclistFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmen_music_list, container, false);
		init(view);
		return view;
	}

	private ListView lv_music;
	private MusicListAdapte adapter;

	private void init(View view) {
		songList = new ArrayList<Music>();
		lv_music = (ListView) view.findViewById(R.id.lv_music);
		adapter = new MusicListAdapte();
		lv_music.setAdapter(adapter);

		new initData().execute();// 异步更新列表

	}

	private List<Music> songList;

	class initData extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			songList = MediaUtils.getSongList(getActivity());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	class MusicListAdapte extends BaseAdapter {
		public int getCount() {
			return songList.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) convertView = View.inflate(getActivity(), R.layout.listview_item, null);
			ImageView musicplaystate = (ImageView) convertView.findViewById(R.id.musicplaystate);
			TextView musiclistPos = (TextView) convertView.findViewById(R.id.musiclistPos);
			TextView musicTime = (TextView) convertView.findViewById(R.id.musicTime);
			TextView musicName = (TextView) convertView.findViewById(R.id.musicName);
			TextView musicAritst = (TextView) convertView.findViewById(R.id.musicAritst);
			Music music = songList.get(position);
			musiclistPos.setText(position + ".");
			musicTime.setText(MediaUtils.formatTime(Integer.parseInt(music.getDuration())));
			musicAritst.setText(music.getArtist());
			musicName.setText(music.getTitle());

			return convertView;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

	}

}
