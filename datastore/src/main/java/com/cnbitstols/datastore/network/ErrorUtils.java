package com.cnbitstols.datastore.network;

import android.support.annotation.NonNull;
import java.io.IOException;
import java.lang.annotation.Annotation;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by DC on 10/3/2016.
 * Parse @{@link ResponseBody} for error body to a T type class
 */

public final class ErrorUtils {

  /*public static FailedResponse parseError(Response<?> response) {

    Converter<ResponseBody, FailedResponse> converter =
        ServiceGenerator.retrofit().responseBodyConverter(FailedResponse.class, new Annotation[0]);

    FailedResponse error;

    try {
      error = converter.convert(response.errorBody());
    } catch (IOException e) {
      return new FailedResponse();
    }
    return error;
  }
*/
  public static <E> E parseError(Response<?> response, @NonNull E emptyObj) {

    Converter<ResponseBody, E> converter =
        ServiceGenerator.retrofit().responseBodyConverter(emptyObj.getClass(), new Annotation[0]);

    E error = emptyObj;
    try {
      error = converter.convert(response.errorBody());
    } catch (IOException e) {
      return error;
    }
    return error;
  }
}
