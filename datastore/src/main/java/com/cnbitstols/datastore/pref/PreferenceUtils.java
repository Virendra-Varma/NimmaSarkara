package com.cnbitstols.datastore.pref;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

/**
 * Created by DC on 3/24/2016.
 * Preference Utils is a customize class responsible for all shared Preference works like saving
 * key
 * value
 */
public class PreferenceUtils {
  private static final String APP_TAG = "dc_data_store_shared_pref";
  private SharedPreferences sPreferenceInstance;
  private Context context;

  public PreferenceUtils(Context context) {
    this.context = context;
  }

  public synchronized SharedPreferences getSharedPreference() {
    if (sPreferenceInstance == null) {
      sPreferenceInstance = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
    }
    return sPreferenceInstance;
  }

  public synchronized SharedPreferences.Editor getPrefEditor() {
    return getSharedPreference().edit();
  }

  public synchronized void clearSharedPref() {
    getPrefEditor().clear().apply();
  }

  /**
   * Complex preference - saving complex object to @{@link SharedPreferences}
   * using @{@link Gson} library
   */
  public void saveObject(String key, Object object) {
    Gson gson = new Gson();
    if (object == null) {
      throw new IllegalArgumentException("Object is null");
    }
    if (key == null || key.isEmpty()) {
      throw new IllegalArgumentException("Key is empty or null");
    }
    getPrefEditor().putString(key, gson.toJson(object)).apply();
  }

  public <T> T getObject(String key, Class<T> a) {
    Gson gson = new Gson();
    String gsonStr = getSharedPreference().getString(key, null);
    if (gsonStr == null) {
      return null;
    } else {
      try {
        return gson.fromJson(gsonStr, a);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Object stored with key " + key + " is instance of other class");
      }
    }
  }
  /* end Complex preferences */
}
