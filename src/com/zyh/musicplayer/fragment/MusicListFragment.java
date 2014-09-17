package com.zyh.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zyh.musicplayer.R;
import com.zyh.musicplayer.domain.Music;
import com.zyh.musicplayer.service.ScanSdFilesReceiver;
import com.zyh.musicplayer.util.ConstantValue;
import com.zyh.musicplayer.util.HandlerManager;
import com.zyh.musicplayer.util.MediaUtils;
import com.zyh.musicplayer.util.ServiceUtils;

public class MusicListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmen_music_list, container, false);
		init(view);
		return view;
	}

	private ListView lv_music;
	private MusicListAdapte adapter;
	private ScanSdFilesReceiver scanReceiver;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

		};
	};

	private ItemSelectedListener itemSelectedListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		itemSelectedListener = (ItemSelectedListener) activity;
		if (ServiceUtils.isRunning(getActivity(), "com.zyh.musicplayer.service.MediaService")) {
			System.out.println("服务正在运行...");
		} else {
			System.out.println("服务挂了...");
			// 初始化当前音乐位置，状态
			MediaUtils.CURRENTPOS = 0;
			MediaUtils.PLAYSTATE = ConstantValue.OPTION_STOP;
		}
		System.out.println("当前歌曲位置：" + MediaUtils.CURRENTPOS);
		System.out.println("当前歌曲状态：" + MediaUtils.PLAYSTATE);
	}

	private void init(View view) {
		handler = HandlerManager.getHandler();
		songList = new ArrayList<Music>();
		lv_music = (ListView) view.findViewById(R.id.lv_music);
		adapter = new MusicListAdapte();
		lv_music.setAdapter(adapter);
		lv_music.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				itemSelectedListener.onItemSelectedListener(position);
				adapter.notifyDataSetChanged();
			}
		});

		new initData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);// 异步更新列表
	}

	/**
	 * 刷新列表
	 */
	public void refreshList() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		filter.addDataScheme("file");
		scanReceiver = new ScanSdFilesReceiver();
		getActivity().registerReceiver(scanReceiver, filter);
		getActivity().sendBroadcast(
				new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
	}

	public static List<Music> songList;

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
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.listview_item, null);
				holder = new ViewHolder();
				holder.musicplaystate = (ImageView) convertView.findViewById(R.id.musicplaystate);
				holder.musiclistPos = (TextView) convertView.findViewById(R.id.musiclistPos);
				holder.musicTime = (TextView) convertView.findViewById(R.id.musicTime);
				holder.musicName = (TextView) convertView.findViewById(R.id.musicName);
				holder.musicAritst = (TextView) convertView.findViewById(R.id.musicAritst);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Music music = songList.get(position);
			holder.musicplaystate.setVisibility(position == MediaUtils.CURRENTPOS ? View.VISIBLE : View.INVISIBLE);
			holder.musiclistPos.setText(position + ".");
			holder.musicTime.setText(MediaUtils.formatTime(Integer.parseInt(music.getDuration())));
			holder.musicAritst.setText(music.getArtist());
			holder.musicName.setText(music.getTitle());
			return convertView;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
	}

	private class ViewHolder {
		ImageView musicplaystate;
		TextView musiclistPos;
		TextView musicTime;
		TextView musicName;
		TextView musicAritst;
	}

	/**
	 * Activity中的回调方法，选择了新的音乐
	 */
	public interface ItemSelectedListener {
		public void onItemSelectedListener(int position);
	}

	public void refreshDate() {
		if (adapter != null) adapter.notifyDataSetChanged();
	}
}
