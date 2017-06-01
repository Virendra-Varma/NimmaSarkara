package com.cnbitstols.dcutilsarsenal.common;

import android.widget.EditText;

/**
 * Created by DC on 5/7/2016.
 * Basic Utils contains all commonly used Methods throughout the app
 */
public final class BasicUtils {
  public static String getTextFromEt(EditText editText) {
    return editText.getText().toString().trim();
  }

}
