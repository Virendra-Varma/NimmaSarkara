package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/9/2016.
 */

public class OtpResponse {
  @SerializedName("id") @Expose public String id;
  @SerializedName("admin") @Expose public String admin;
  @SerializedName("name") @Expose public String name;
  @SerializedName("mobile") @Expose public String mobile;
  @SerializedName("OTP") @Expose public String oTP;
  @SerializedName("regDate") @Expose public String regDate;

  public String getAdmin() {
    return admin;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getMobile() {
    return mobile;
  }

  public String getoTP() {
    return oTP;
  }

  public String getRegDate() {
    return regDate;
  }

  @Override public String toString() {
    return "OtpResponse{" +
        "id='" + id + '\'' +
        ", admin='" + admin + '\'' +
        ", name='" + name + '\'' +
        ", mobile='" + mobile + '\'' +
        ", oTP='" + oTP + '\'' +
        ", regDate='" + regDate + '\'' +
        '}';
  }
}
