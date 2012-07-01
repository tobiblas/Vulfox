package com.vulfox.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Logger {

	public static boolean doLog = true;
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMdd_HHmmss:SS");

	public static void log(String message) {
		if (doLog) {
			Log.d("Logger", message);
		}
	}

	public static void logWithTimeStamp(String message) {
		if (doLog) {
			Log.d("Logger", sdf.format(new Date()) + " " + message + " Thread: " + Thread.currentThread().getName());
		}
	}
}
