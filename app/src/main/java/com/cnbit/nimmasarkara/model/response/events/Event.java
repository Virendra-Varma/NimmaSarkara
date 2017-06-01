package com.cnbit.nimmasarkara.model.response.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/16/2016.
 */
public class Event {

  @SerializedName("eventid") @Expose public String eventid;
  @SerializedName("title") @Expose public String title;
  @SerializedName("description") @Expose public String description;
  @SerializedName("datetime") @Expose public String datetime;

  @Override public String toString() {
    return "Event{" +
        "eventid='" + eventid + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", datetime='" + datetime + '\'' +
        '}';
  }

  public String getEventid() {
    return eventid;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getDatetime() {
    return datetime;
  }
}
