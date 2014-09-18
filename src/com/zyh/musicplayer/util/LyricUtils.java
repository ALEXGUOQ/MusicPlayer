package com.zyh.musicplayer.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.zyh.musicplayer.domain.LyricLine;

public class LyricUtils {
	private static final String TAG = "LRCUtils";

	private static List<LyricLine> lrclist;
	private static boolean IsLyricExist = false;
	private int lastLine = 0;

	public void RefreshLRC(int current) {
		if (IsLyricExist) {
			for (int i = 0; i < lrclist.size(); i++) {
				if (current < lrclist.get(i).getTimePoint()) if (i == 0 || current >= lrclist.get(i - 1).getTimePoint()) {
					Log.d(TAG, "string = " + lrclist.get(i - 1).getLrcString());
					// mediaPlay.setLRCText(lrclist.get(i - 1).getLrcString(), lastLine != (i - 1));
					lastLine = i - 1;
					// playlrcText.setText(lrclist.get(i-1).getLrcString());
				}

			}
		}
	}

	/**
	 * 从文件中读取LRC歌词
	 * @param f
	 * @return 歌词list
	 */
	public static List<LyricLine> readLRC(File f) {
		try {
			if (f == null || !f.exists()) {
				System.out.println("not exit the lrc file");
				IsLyricExist = false;
				lrclist = null;
				// strLRC = main.getResources().getString(R.string.lrcservice_no_lyric_found);
			} else {
				lrclist = new ArrayList<LyricLine>();
				IsLyricExist = true;
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				BufferedReader br = new BufferedReader(new InputStreamReader(is, getCharset(f)));
				String strTemp = "";
				while ((strTemp = br.readLine()) != null) {
					System.out.println("分析行......");
					// Log.d(TAG,"strTemp = "+strTemp);
					strTemp = AnalyzeLRC(strTemp);
				}
				br.close();
				is.close();
				Collections.sort(lrclist);// 歌词按时间排序

				for (int i = 0; i < lrclist.size(); i++) {
					LyricLine one = lrclist.get(i);
					if (i + 1 < lrclist.size()) {
						LyricLine two = lrclist.get(i + 1);
						one.setSleepTime(two.getTimePoint() - one.getTimePoint());
					}
					System.out.println("time = " + lrclist.get(i).getTimePoint() + "   string = " + lrclist.get(i).getLrcString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lrclist;
	}

	private static String AnalyzeLRC(String LRCText) {
		try {
			int pos1 = LRCText.indexOf("[");
			int pos2 = LRCText.indexOf("]");

			System.out.println("22p1:" + pos1 + ",p2:" + pos2);
			if (pos1 >= 0 && pos2 != -1) {
				Long time[] = new Long[getPossiblyTagCount(LRCText)];
				time[0] = timeToLong(LRCText.substring(pos1 + 1, pos2));
				if (time[0] == -1) return ""; // LRCText
				String strLineRemaining = LRCText;
				int i = 1;
				System.out.println("33p1:" + pos1 + ",p2:" + pos2);
				while (pos1 >= 0 && pos2 != -1) {
					System.out.println("p1:" + pos1 + ",p2:" + pos2);
					System.out.println("一个时间");
					strLineRemaining = strLineRemaining.substring(pos2 + 1);
					pos1 = strLineRemaining.indexOf("[");
					pos2 = strLineRemaining.indexOf("]");
					if (pos2 != -1) {
						time[i] = timeToLong(strLineRemaining.substring(pos1 + 1, pos2));
						if (time[i] == -1) return ""; // LRCText
						i++;
					}
				}

				LyricLine tl = null;
				for (int j = 0; j < time.length; j++) {
					if (time[j] != null) {
						tl = new LyricLine();
						// Log.d(TAG,"time["+j+"] = "+time[j].intValue()+"    strLineRemaining = "+strLineRemaining);
						tl.setTimePoint(time[j].intValue());
						tl.setLrcString(strLineRemaining);
						lrclist.add(tl);// 添加封装好的一句歌词
						System.out.println("添加一句");
						// map.put(time[j], strLineRemaining);
						// lstTimeStamp.add(time[j]);
					}
				}
				return strLineRemaining;
			} else return "";
		} catch (Exception e) {
			return "";
		}
	}

	private static int getPossiblyTagCount(String Line) {
		String strCount1[] = Line.split("\\[");
		String strCount2[] = Line.split("\\]");
		if (strCount1.length == 0 && strCount2.length == 0) return 1;
		else if (strCount1.length > strCount2.length) return strCount1.length;
		else return strCount2.length;
	}

	public static long timeToLong(String Time) {
		try {
			String[] s1 = Time.split(":");
			int min = Integer.parseInt(s1[0]);
			String[] s2 = s1[1].split("\\.");
			int sec = Integer.parseInt(s2[0]);
			int mill = 0;
			if (s2.length > 1) mill = Integer.parseInt(s2[1]);
			return min * 60 * 1000 + sec * 1000 + mill * 10;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 判断文件编码
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0) break;
					if (0x80 <= read && read <= 0xBF) break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) continue;
						else break;
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else break;
						} else break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

}
