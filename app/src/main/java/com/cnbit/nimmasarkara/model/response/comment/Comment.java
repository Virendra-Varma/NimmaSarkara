package com.cnbit.nimmasarkara.model.response.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/10/2016.
 */
public class Comment {

  @SerializedName("comment") @Expose public String comment;
  @SerializedName("postedby") @Expose private String postedby;
  @SerializedName("datetime") @Expose private String datetime;

  @Override public String toString() {
    return "Comment{" +
        "postedby='" + postedby + '\'' +
        ", datetime='" + datetime + '\'' +
        ", comment='" + comment + '\'' +
        '}';
  }

  public String getPostedby() {
    return postedby;
  }

  public String getDatetime() {
    return datetime;
  }

  public String getComment() {
    return comment;
  }
}
