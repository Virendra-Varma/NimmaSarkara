package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/9/2016.
 */

public class PostCommentResponse {
  @SerializedName("status") @Expose public String status;

  public String getStatus() {
    return status;
  }

  @Override public String toString() {
    return "PostCommentResponse{" +
        "status='" + status + '\'' +
        '}';
  }
}
