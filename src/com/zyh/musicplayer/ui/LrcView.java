package com.zyh.musicplayer.ui;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zyh.musicplayer.domain.LyricLine;
import com.zyh.musicplayer.util.DensityUtil;
import com.zyh.musicplayer.util.LyricUtils;

/**
 * 歌词播放的自定义控件
 */
public class LrcView extends TextView {

	private List<LyricLine> lrclist;
	// private String[] lrc = new String[] { "ddddd", "aaaaa", "ddddd", "bbbb", "ccccc", "ddddd", "eeeeeeeeee", "ddddd"
	// };
	private int curLine;// 当前行
	private int lineSpace;// 行间距
	private Paint curPaint, otherPaint;// 两个画笔

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();// 刷新界面的方法
			curLine++;
		}
	};

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);

		File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/11.lrc");
		System.out.println(f.getAbsolutePath());
		lrclist = LyricUtils.readLRC(f);
		System.out.println("size:" + lrclist.size());
		// 初始化参数
		lineSpace = DensityUtil.dip2px(context, 28);
		curLine = 0;

		// 当前行画笔
		curPaint = new Paint();
		curPaint.setColor(Color.GREEN);
		curPaint.setTextSize(DensityUtil.dip2px(context, 21));
		curPaint.setTextAlign(Align.CENTER);

		// 其他行画笔
		otherPaint = new Paint();
		otherPaint.setColor(Color.WHITE);
		otherPaint.setTextSize(DensityUtil.dip2px(context, 17));
		otherPaint.setTextAlign(Align.CENTER);
		// curPaint.setTypeface(Typeface.SERIF);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (lrclist != null && lrclist.size() > 0) {
			if (curLine < lrclist.size()) {// 防止行溢y
				// 播放行之前的
				for (int i = curLine - 1; i >= 0; i--) {
					canvas.drawText(lrclist.get(i).getLrcString(), getWidth() / 2, getHeight() / 2 - (curLine - i) * lineSpace, otherPaint);
				}
				// 当前播放行
				canvas.drawText(lrclist.get(curLine).getLrcString(), getWidth() / 2, getHeight() / 2, curPaint);
				// 播放行之后的
				for (int i = curLine + 1; i < lrclist.size(); i++) {
					canvas.drawText(lrclist.get(i).getLrcString(), getWidth() / 2, getHeight() / 2 + (i - curLine) * lineSpace, otherPaint);
				}
				handler.sendEmptyMessageDelayed(200, lrclist.get(curLine).getSleepTime());
			}
		} else {
			canvas.drawText("未找到歌词", getWidth() / 2, getHeight() / 2, curPaint);
		}
		super.onDraw(canvas);
	}

}
