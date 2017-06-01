package com.cnbit.nimmasarkara.network;

import android.support.annotation.NonNull;
import com.cnbitstols.datastore.network.LoggingInterceptor;
import com.cnbitstols.datastore.network.ServiceGenerator;

/**
 * Created by DC on 10/10/2016.
 */

public class ShriSaiRestService extends ServiceGenerator {
  @Override protected String getBaseUrl() {
    return "http://ec2-34-207-53-238.compute-1.amazonaws.com/nimmasarkar/";
  }

  @Override public <S> S createService(@NonNull Class<S> apiClass) {
    return super.createService(apiClass, new LoggingInterceptor());
  }
}
