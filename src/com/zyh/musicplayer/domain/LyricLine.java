package com.zyh.musicplayer.domain;

//�����Ϣ 
public class LyricLine implements Comparable<LyricLine> {

	private String lrcString;// �������
	private int sleepTime;// ���ֹͣʱ��
	private int timePoint;// ��ʼʱ��

	public LyricLine() {
		lrcString = null;
		sleepTime = 0;
		timePoint = 0;
	}

	public void setLrcString(String lrc) {
		lrcString = lrc;
	}

	public void setSleepTime(int time) {
		sleepTime = time;
	}

	public void setTimePoint(int tPoint) {
		timePoint = tPoint;
	}

	public String getLrcString() {
		return lrcString;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public int getTimePoint() {
		return timePoint;
	}

	@Override
	public int compareTo(LyricLine another) {
		return this.timePoint - another.timePoint;
	}
}
