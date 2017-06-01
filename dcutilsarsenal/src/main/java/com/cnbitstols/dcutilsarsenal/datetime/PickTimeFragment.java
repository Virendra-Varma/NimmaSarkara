package com.cnbitstols.dcutilsarsenal.datetime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by DC on 10/17/2016.
 */

public class PickTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
  public static final String TAG = PickTimeFragment.class.getSimpleName();
  private TimePickListener timePickListener;

  public static PickTimeFragment getInstance() {
    return new PickTimeFragment();
  }

  public void setTimePickListener(TimePickListener timePickListener) {
    this.timePickListener = timePickListener;
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    // Create a new instance of TimePickerDialog and return it
    return new TimePickerDialog(getActivity(), this, hour, minute,
        DateFormat.is24HourFormat(getActivity()));
  }

  @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    try {
      String time = hourOfDay + ":" + minute;
      if (timePickListener != null) {
        timePickListener.onTimePicked(new TimePickEvent(time, hourOfDay, minute));
      }
    } catch (Resources.NotFoundException ex) {
      ex.printStackTrace();
    }
  }

  public interface TimePickListener {
    void onTimePicked(TimePickEvent timePickEvent);
  }

  public static class TimePickEvent {
    private String time;
    private int hour;
    private int min;

    public TimePickEvent(String time, int hour, int min) {

      this.time = time;
      this.hour = hour;
      this.min = min;
    }

    public TimePickEvent(String time) {

      this.time = time;
    }

    public int getHour() {
      return hour;
    }

    public int getMin() {
      return min;
    }

    public String getTime() {
      return time;
    }
  }
}
