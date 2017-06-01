package com.cnbit.nimmasarkara.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DC on 4/22/2016.
 */
public class NetworkUtils {
  public static boolean haveNetworkConnection(Context context) {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
      if (ni.getTypeName().equalsIgnoreCase("WIFI")) if (ni.isConnected()) haveConnectedWifi = true;
      if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
        if (ni.isConnected()) haveConnectedMobile = true;
      }
    }
    return haveConnectedWifi || haveConnectedMobile;
  }

  public static int getType(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (networkInfo != null) {
      // returns ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE etc.
      return networkInfo.getType();
    } else {
      return -1;
    }
  }

  public static String getTypeName(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (networkInfo != null) {
      return networkInfo.getTypeName();
    } else {
      return null;
    }
  }
}
