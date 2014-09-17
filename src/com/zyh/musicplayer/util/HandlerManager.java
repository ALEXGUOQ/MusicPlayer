package com.zyh.musicplayer.util;

import android.os.Handler;

public class HandlerManager {

	private static ThreadLocal<Handler> threadLocal = new ThreadLocal<Handler>();

	public static Handler getHandler() {
		return threadLocal.get();
	}

	public static void putHandler(Handler handler) {
		threadLocal.set(handler);
	}
}
