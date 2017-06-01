package com.cnbit.nimmasarkara.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DC on 5/7/2016.
 */
public class BasicUtils {
  public static String getTextFromEt(EditText editText) {
    return editText.getText().toString().trim();
  }

  public static String parseDate(String dtStart, String fromFormat, String toFormat) {
    SimpleDateFormat format = new SimpleDateFormat(fromFormat, Locale.US);
    try {
      Date date = format.parse(dtStart);
      SimpleDateFormat postFormater = new SimpleDateFormat(toFormat, Locale.US);
      return postFormater.format(date);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }

    return "";
  }

  public static String parseDate(int year, int month, int day, int hour, int minute) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(0);
    cal.set(year, month, day, hour, minute);
    Date date = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    return sdf.format(date);
  }

  public static String removeDoubleQuotes(String str) {
    return str.replaceAll("^\"|\"$", "");
  }

  public static String removeLastChar(String s) {
    if (s == null || s.length() == 0) {
      return s;
    }
    return s.substring(0, s.length() - 1);
  }

  /*public static int getRandomColor(String key){
    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
    // generate random color
    //int color1 = generator.getRandomColor();
    // generate color based on a key (same key returns the same color), useful for list/grid views
    return generator.getColor(key);
  }*/

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
