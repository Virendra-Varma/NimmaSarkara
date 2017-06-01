package com.cnbitstols.datastore.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ServiceGenerator {
  private static Retrofit.Builder mBasicBuilder =
      new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create());
  private Retrofit.Builder mBuilder = mBasicBuilder.baseUrl(getBaseUrl());

  private OkHttpClient.Builder mHttpClient =
      new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS)
          .writeTimeout(120, TimeUnit.SECONDS);

  static Retrofit retrofit() {
    return mBasicBuilder.build();
  }

  public <S> S createService(@NonNull Class<S> apiClass) {
    return createService(apiClass, null, null);
  }

  protected <S> S createService(@NonNull Class<S> apiClass, @Nullable Interceptor arg0) {
    return createService(apiClass, arg0, null);
  }

  protected <S> S createService(@NonNull Class<S> apiClass, @Nullable Interceptor arg0,
      @Nullable Interceptor arg1) {
    Retrofit retrofit = mBuilder.client(getOkClient(arg0, arg1)).build();
    return retrofit.create(apiClass);
  }

  private OkHttpClient getOkClient(@Nullable Interceptor arg0, @Nullable Interceptor arg1) {
    if (arg0 != null) {
      mHttpClient.addInterceptor(arg0);
    }
    if (arg1 != null) {
      mHttpClient.addInterceptor(arg1);
    }
    return mHttpClient.build();
  }

  protected abstract String getBaseUrl();
}