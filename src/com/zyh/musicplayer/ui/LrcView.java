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
 * ��ʲ��ŵ��Զ���ؼ�
 */
public class LrcView extends TextView {

	private List<LyricLine> lrclist;
	// private String[] lrc = new String[] { "ddddd", "aaaaa", "ddddd", "bbbb", "ccccc", "ddddd", "eeeeeeeeee", "ddddd"
	// };
	private int curLine;// ��ǰ��
	private int lineSpace;// �м��
	private Paint curPaint, otherPaint;// ��������

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();// ˢ�½���ķ���
			curLine++;
		}
	};

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);

		File f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/11.lrc");
		System.out.println(f.getAbsolutePath());
		lrclist = LyricUtils.readLRC(f);
		System.out.println("size:" + lrclist.size());
		// ��ʼ������
		lineSpace = DensityUtil.dip2px(context, 28);
		curLine = 0;

		// ��ǰ�л���
		curPaint = new Paint();
		curPaint.setColor(Color.GREEN);
		curPaint.setTextSize(DensityUtil.dip2px(context, 21));
		curPaint.setTextAlign(Align.CENTER);

		// �����л���
		otherPaint = new Paint();
		otherPaint.setColor(Color.WHITE);
		otherPaint.setTextSize(DensityUtil.dip2px(context, 17));
		otherPaint.setTextAlign(Align.CENTER);
		// curPaint.setTypeface(Typeface.SERIF);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (lrclist != null && lrclist.size() > 0) {
			if (curLine < lrclist.size()) {// ��ֹ����y
				// ������֮ǰ��
				for (int i = curLine - 1; i >= 0; i--) {
					canvas.drawText(lrclist.get(i).getLrcString(), getWidth() / 2, getHeight() / 2 - (curLine - i) * lineSpace, otherPaint);
				}
				// ��ǰ������
				canvas.drawText(lrclist.get(curLine).getLrcString(), getWidth() / 2, getHeight() / 2, curPaint);
				// ������֮���
				for (int i = curLine + 1; i < lrclist.size(); i++) {
					canvas.drawText(lrclist.get(i).getLrcString(), getWidth() / 2, getHeight() / 2 + (i - curLine) * lineSpace, otherPaint);
				}
				handler.sendEmptyMessageDelayed(200, lrclist.get(curLine).getSleepTime());
			}
		} else {
			canvas.drawText("δ�ҵ����", getWidth() / 2, getHeight() / 2, curPaint);
		}
		super.onDraw(canvas);
	}

}
