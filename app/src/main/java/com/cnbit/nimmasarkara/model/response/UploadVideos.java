package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/9/2016.
 */

public class UploadVideos {
  @SerializedName("status")
  @Expose
  public String status;
  @SerializedName("msg")
  @Expose
  public String msg;

  public UploadVideos(String status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override public String toString() {
    return "UploadVideos{" +
        "status='" + status + '\'' +
        ", msg='" + msg + '\'' +
        '}';
  }
}
