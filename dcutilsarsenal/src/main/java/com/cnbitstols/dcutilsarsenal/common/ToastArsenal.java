package com.cnbitstols.dcutilsarsenal.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DC on 4/22/2016.
 * Class ToastArsenal - This is a helper class contains static methods which
 * are used to show messages to the user based on some events
 */
public final class ToastArsenal {
  private static final String NO_INTERNET = "no internet";

  public static void showToast(Context context, String msg) {
    try {
      Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
  }

  public static void noInternetToast(Context context) {
    showToast(context, NO_INTERNET);
  }

  public static void showNoInternetSnackBar(View view) {
    showSnackBar(view, NO_INTERNET);
  }

  public static void showSnackBar(View view, String text) {
    showSnackBar(view, text, Snackbar.LENGTH_LONG);
  }

  public static void showSnackBar(View view, String text, @NonNull int duration) {
    getSnackBar(view, text, duration).show();
  }

  private static Snackbar getSnackBar(View view, String text, @NonNull int duration) {
    return Snackbar.make(view, text, duration);
  }

  public static Snackbar showSnackBar(View view, String text, @NonNull int duration, String action,
      View.OnClickListener clickListener) {
    return getSnackBar(view, text, duration).setAction(action, clickListener);
  }
}
