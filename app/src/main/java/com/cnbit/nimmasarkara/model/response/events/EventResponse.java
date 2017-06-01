package com.cnbit.nimmasarkara.model.response.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DC on 10/16/2016.
 */

public class EventResponse {
  @SerializedName("events") @Expose public List<Event> events = new ArrayList<>();

  @Override public String toString() {
    return "EventResponse{" +
        "events=" + events +
        '}';
  }

  public List<Event> getEvents() {
    return events;
  }
}

