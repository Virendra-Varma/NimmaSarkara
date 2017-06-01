package com.cnbitstols.datastore.network;

import android.support.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DC on 10/7/2016.
 * Wrapper callback of @{@link Callback} which wraps the response in
 * custom methods @sendSuccessResponse , @sendErrorResponse ,@sendGenericError to
 * be send back to calling class
 */

public abstract class RestCallback<T, E> extends NetworkDataService<T, E> implements Callback<T> {
  private E object;

  protected RestCallback(@Nullable E obj) {
    this.object = obj;
  }

  protected RestCallback() {
    this(null);
  }

  @Override public void onResponse(Call<T> call, Response<T> response) {
    if (response.isSuccessful()) {
      sendSuccessResponse(response.body());
    } else {
      if (object != null) {
        sendErrorResponse(ErrorUtils.parseError(response, object), null);
      } else {
        sendErrorResponse(null, response.errorBody().toString());
      }
    }
  }

  @Override public void onFailure(Call<T> call, Throwable t) {
    if (checkError(t)) {
      sendErrorResponse(null, t.getMessage());
    } else {
      sendErrorResponse(null, UNKNOWN_REASON);
    }
  }
}
