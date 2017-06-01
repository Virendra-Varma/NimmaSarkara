package com.cnbitstols.dcutilsarsenal.parsing;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by DC on 9/6/2016.
 * Parsing Object using @{@link Gson} to JSON
 *  If not in use can remove the compile 'com.google.code.gson:gson:2.6.2'
 *  from gradle file
 */
public final class ParseJsonUtils {
  public static Object parseResponse(@NonNull String response, @NonNull Class obj) {
    Gson gson = new Gson();
    return gson.fromJson(response, obj);
  }

  public static <T> List<T> parseArrayResponse(@NonNull String response, @NonNull T obj) {
    Gson gson = new Gson();
    Type listType = new TypeToken<List<T>>() {
    }.getType();
    return gson.fromJson(response, listType);
  }

  public static String convertToJson(Object object) {
    return new Gson().toJson(object);
  }
}
