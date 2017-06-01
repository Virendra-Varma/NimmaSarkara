package com.cnbitstols.dcutilsarsenal.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by DC on 9/22/2016.
 * Dialog methods
 */

public final class DialogUtils {
  public static ProgressDialog getProgressDialog(Context context, String message) {
    ProgressDialog progressDialog = new ProgressDialog(context);
    progressDialog.setIndeterminate(true);
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.setMessage(message);
    return progressDialog;
  }

  public static AlertDialog getAlertDialogPositive(Context context, String title, String message) {
    return getAlertBuilder(context, title, message).setPositiveButton("Ok",
        new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        }).create();
  }

  public static AlertDialog getOkCancelDialog(@NonNull Context context, String title,
      String message, DialogInterface.OnClickListener listener) {
    return getAlertBuilder(context, title, message).setPositiveButton("Ok", listener)
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        })
        .create();
  }

  private static AlertDialog.Builder getAlertBuilder(@NonNull Context context, String title,
      String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(message).setTitle(title);
    return builder;
  }
}
