package com.cnbitstols.datastore.network;

import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DC on 9/23/2016.
 * Interceptor for adding header for authorization api key
 * Authorization: api_key
 */

public final class AuthorizationInterceptor implements Interceptor {
  private String apiKey;

  public AuthorizationInterceptor(@NonNull String key) {
    this.apiKey = key;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request newRequest = chain.request().newBuilder().addHeader("Authorization", apiKey).build();
    return chain.proceed(newRequest);
  }
}
