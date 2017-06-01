package com.cnbitstols.dcutilsarsenal.datetime;

import android.support.annotation.NonNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DC on 10/14/2016.
 * This class contains date formatting related
 */

public final class DateArsenal {
  private static final String EMPTY_STRING = "";

  public static String parseDate(@NonNull String dtStart, @NonNull String fromFormat,
      @NonNull String toFormat) {
    SimpleDateFormat format = new SimpleDateFormat(fromFormat, Locale.US);
    try {
      Date date = format.parse(dtStart);
      return new SimpleDateFormat(toFormat, Locale.US).format(date);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return EMPTY_STRING;
  }

  public static String getCurrentDate(String dateformat) {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateformat, Locale.US);
    return sdf.format(c.getTime());
  }
}
