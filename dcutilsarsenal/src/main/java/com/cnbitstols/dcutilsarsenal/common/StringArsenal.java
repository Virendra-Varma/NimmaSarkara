package com.cnbitstols.dcutilsarsenal.common;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

/**
 * Created by DC on 10/14/2016.
 *
 * String Related utils methods
 */

@SuppressWarnings("ALL") public final class StringArsenal {
  public static String removeDoubleQuotes(String str) {
    if (str == null) return str;
    return str.replaceAll("^\"|\"$", "");
  }

  public static String removeLastChar(String s) {
    if (s == null || s.length() == 0) {
      return s;
    }
    return s.substring(0, s.length() - 1);
  }

  public static SpannableString getSpannableText(@NonNull String text, int count) {
    String placeholder = text + '(' + count + ')';
    int textLength = text.length();
    SpannableString ss1 = new SpannableString(placeholder);
    ss1.setSpan(new RelativeSizeSpan(2f), 0, textLength, 0); // set size
    ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, textLength, 0);// set color
    ss1.setSpan(new ForegroundColorSpan(Color.GRAY), textLength + 1, placeholder.length(), 0);
    return ss1;
  }
}
