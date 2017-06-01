package com.cnbit.nimmasarkara.utils;

import android.content.Context;
import com.cnbitstols.datastore.pref.PreferenceUtils;

/**
 * Created by DC on 10/6/2016.
 */

public class AppUtils extends PreferenceUtils {

  public AppUtils(Context context) {
    super(context);
  }

  public boolean isOTPVerified() {
    return getSharedPreference().getBoolean(AppConstantsUtils.OTP_VERIFIED, false);
  }

  public void setOtpVerified() {
    getPrefEditor().putBoolean(AppConstantsUtils.OTP_VERIFIED, true).apply();
  }

  public String getCurrentOtp() {
    return getSharedPreference().getString(AppConstantsUtils.OTP, null);
  }

  public void setCurrentOtp(String otp) {
    getPrefEditor().putString(AppConstantsUtils.OTP, otp).apply();
  }

  public String getUserId() {
    return getSharedPreference().getString(AppConstantsUtils.USER_ID, null);
  }

  public void setUserId(String userId) {
    getPrefEditor().putString(AppConstantsUtils.USER_ID, userId).apply();
  }

  public void commented(boolean flag) {
    getPrefEditor().putBoolean(AppConstantsUtils.COMMENTED, flag).apply();
  }

  public boolean isCommented() {
    return getSharedPreference().getBoolean(AppConstantsUtils.COMMENTED, false);
  }

  public boolean isLikedPic() {
    return getSharedPreference().getBoolean(AppConstantsUtils.LIKED_PIC, false);
  }

  public boolean isLikedAudio() {
    return getSharedPreference().getBoolean(AppConstantsUtils.LIKED_AUDIO, false);
  }

  public void setLikedPic(boolean b) {
    getPrefEditor().putBoolean(AppConstantsUtils.LIKED_PIC, b).apply();
  }

  public void setLikedAudio(boolean b) {
    getPrefEditor().putBoolean(AppConstantsUtils.LIKED_AUDIO, b).apply();

  }

  public boolean isLikedVideo() {
    return getSharedPreference().getBoolean(AppConstantsUtils.LIKED_VIDEO, false);
  }

  public void setLikedVideo(boolean b) {
    getPrefEditor().putBoolean(AppConstantsUtils.LIKED_VIDEO, b).apply();
  }

  public void setUserType(boolean flag) {
    getPrefEditor().putBoolean(AppConstantsUtils.USER_TYPE, flag).apply();
  }

  public boolean isUserIsAdmin() {
    return getSharedPreference().getBoolean(AppConstantsUtils.USER_TYPE, false);
  }
}
