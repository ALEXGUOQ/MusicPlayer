package com.zyh.musicplayer.util;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	/**
	 * 判断service是否正在运行
	 */
	public static boolean isRunning(Context context, String serviceName) {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningServices = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(100);
		for (RunningServiceInfo runningService : runningServices) {
			if (serviceName.equals(runningService.service.getClassName())) return true;
		}
		return false;
	}

}
