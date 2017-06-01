package com.cnbit.nimmasarkara.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import com.cnbit.nimmasarkara.events.BusProvider;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DC on 10/17/2016.
 * Date Picker Fragment
 */

public class PickDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
  public static final String TAG = PickDateFragment.class.getSimpleName();
  private static String dateFormat = "dd-MMM-yy";

  public static PickDateFragment newInstance(String dateFormat) {
    PickDateFragment fragment = new PickDateFragment();
    Bundle bundle = new Bundle();
    bundle.putString("DATE_FORMAT", dateFormat);
    fragment.setArguments(bundle);
    return fragment;
  }

  private static String formatDate(int year, int month, int day) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(0);
    cal.set(year, month, day);
    Date date = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    return sdf.format(date);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      dateFormat = getArguments().getString("DATE_FORMAT");
    }
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    try {
      String formattedDate = formatDate(year, monthOfYear, dayOfMonth);
      BusProvider.getInstance()
          .post(new DatePickEvent(formattedDate, year, monthOfYear, dayOfMonth));
    } catch (Resources.NotFoundException ex) {
      ex.printStackTrace();
    }
  }

  public static class DatePickEvent {
    private String date;
    private int year;
    private int month;
    private int day;

    public DatePickEvent(String date) {
      this.date = date;
    }

    public DatePickEvent(String date, int year, int month, int day) {
      this.date = date;
      this.year = year;
      this.month = month;
      this.day = day;
    }

    public int getYear() {
      return year;
    }

    public int getMonth() {
      return month;
    }

    public int getDay() {
      return day;
    }

    public String getDate() {
      return date;
    }
  }
}

