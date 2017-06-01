package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/9/2016.
 */

public class PostLikes {
  @SerializedName("status")
  @Expose
  public String status;

  public PostLikes(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override public String toString() {
    return "PostLikes{" +
        "status='" + status + '\'' +
        '}';
  }
}
