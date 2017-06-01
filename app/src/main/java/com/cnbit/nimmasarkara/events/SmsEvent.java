package com.cnbit.nimmasarkara.events;

import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.model.response.FailedResponse;

/**
 * Created by DC on 4/29/2016.
 */
public class SmsEvent {
  public static class OnSmsRecieved {
    private String otp;

    public OnSmsRecieved(String otp) {

      this.otp = otp;
    }

    public String getOtp() {
      return otp;
    }
  }

  public static class OtpSent {
    private OtpResponse response;

    public OtpSent(OtpResponse response) {

      this.response = response;
    }

    public OtpResponse getResponse() {
      return response;
    }
  }

  public static class OtpSentFailed {
    private FailedResponse failedResponse;
  private String reason;

    public String getReason() {
      return reason;
    }

    public OtpSentFailed(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }
  }
}
