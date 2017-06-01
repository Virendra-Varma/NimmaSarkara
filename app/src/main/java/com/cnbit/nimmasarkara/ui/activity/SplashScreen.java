package com.cnbit.nimmasarkara.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.AppUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_splash_screen);
    Handler handler = new Handler();
    final AppUtils appUtils = new AppUtils(this);
    Runnable runnable = new Runnable() {
      @Override public void run() {
        printKeyHash();
       if (appUtils.isOTPVerified()) {
          forwardToLandingPage();
        } else {
          showOtpScreen();
        }
      }
    };

    handler.postDelayed(runnable, 5000);
  }

  private void printKeyHash() {
    try {
      PackageInfo info =
          getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
    } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  private void forwardToLandingPage() {
    //Intent intent = new Intent(this, LandingPageActivity.class);
    Intent intent = new Intent(this, Landingpagepro.class);
    //Intent intent = new Intent(this, DashboardActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

  private void showOtpScreen() {
    Intent intent = new Intent(this, OtpActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }
}
