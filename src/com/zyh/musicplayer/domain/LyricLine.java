package com.zyh.musicplayer.domain;

//歌词信息 
public class LyricLine implements Comparable<LyricLine> {

	private String lrcString;// 歌词内容
	private int sleepTime;// 歌词停止时间
	private int timePoint;// 开始时间

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
