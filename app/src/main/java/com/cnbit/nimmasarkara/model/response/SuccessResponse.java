package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/16/2016.
 */

public class SuccessResponse {
  @SerializedName("status") @Expose private String status;

  @Override public String toString() {
    return "SuccessResponse{" +
        "status='" + status + '\'' +
        '}';
  }

  public String getStatus() {
    return status;
  }
}
