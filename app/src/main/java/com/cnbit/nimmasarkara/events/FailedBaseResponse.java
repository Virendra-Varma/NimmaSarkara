package com.cnbit.nimmasarkara.events;

import com.cnbit.nimmasarkara.model.response.FailedResponse;

/**
 * Created by DC on 10/17/2016.
 */

public class FailedBaseResponse {
  private FailedResponse failedResponse;
  private String reason;

  public FailedResponse getFailedResponse() {
    return failedResponse;
  }

  public String getReason() {
    return reason;
  }

  public FailedBaseResponse(FailedResponse failedResponse, String reason) {

    this.failedResponse = failedResponse;
    this.reason = reason;
  }
}
