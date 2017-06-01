package com.cnbitstols.dcutilsarsenal.common;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by DC on 4/22/2016.
 */
public class KeyboardArsenal {
  public static void closeKeyBoard(View view, Context context) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void showSoftKeyboard(View view, Context context) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    view.requestFocus();
    inputMethodManager.showSoftInput(view, 0);
  }
}
