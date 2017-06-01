package com.cnbit.nimmasarkara.utils;

import android.util.Log;

/**
 * Created by DC on 3/24/2016.
 *
 * Class -  LogUtils - It is a util class for log methods
 */
public class LogUtils {
public static final String TAG = LogUtils.class.getSimpleName();
  public static boolean isLogging = true;
  public static void debugOut(String msg) {
    StackTraceElement element = new Throwable().getStackTrace()[1];
    final String logMsg = element.getMethodName() + " -- "
        + element.getFileName() + " (" + element.getLineNumber()
        + ")+ " + msg;
    if (!isLogging) {
      return;
    }
    Log.d(TAG, logMsg);
  }
}
