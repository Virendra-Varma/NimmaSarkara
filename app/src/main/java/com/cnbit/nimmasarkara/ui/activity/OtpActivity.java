package com.cnbit.nimmasarkara.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SmsEvent;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.squareup.otto.Subscribe;

import static com.cnbit.nimmasarkara.utils.BasicUtils.getTextFromEt;

public class OtpActivity extends AppCompatActivity {
  private ImageView mForwardIv;
  private EditText mEtUserName;
  private EditText mEtMobileNumber;
  private EditText mVerificationCodeEt;
  private Button mVerifyBtn;
  private boolean isNameEntered;
  private ProgressDialog mProgressDialog;
  private LinearLayout mVerificationCodeLayout;
  private LinearLayout mOtpSendLayout;
  private AppUtils mAppUtils;
  private String mUserNameStr;
  private TextView mOtpLbl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_otp);
    mForwardIv = (ImageView) findViewById(R.id.next);
    mEtUserName = (EditText) findViewById(R.id.etUserName);
    mVerificationCodeEt = (EditText) findViewById(R.id.etVerificationCode);
    mOtpLbl = (TextView) findViewById(R.id.otp_lbl);
    mVerifyBtn = (Button) findViewById(R.id.btn_verify_code);
    mEtMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
    mVerificationCodeLayout = (LinearLayout) findViewById(R.id.code_verification_layout);
    mOtpSendLayout = (LinearLayout) findViewById(R.id.otpSendLayout);
    mAppUtils = new AppUtils(this);
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setCanceledOnTouchOutside(false);
    mProgressDialog.setMessage(getString(R.string.sending_otp));
    mForwardIv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!isNameEntered) {
          if (isValidName()) {
            isNameEntered = true;
            mUserNameStr = BasicUtils.getTextFromEt(mEtUserName);
            mEtUserName.setVisibility(View.GONE);
            mEtMobileNumber.setVisibility(View.VISIBLE);
            mOtpLbl.setText(getString(R.string.enter_no));
          } else {
            ToastUtils.showSnackBar(mEtUserName, "Enter Valid Name");
          }
        } else {
          initiateServiceCall();
        }
      }
    });
    mVerifyBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        initiateVerificationCall();
      }
    });
    mEtMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          initiateServiceCall();
        }
        return false;
      }
    });
  }

  private void forwardToLandingPage() {
    //Intent intent = new Intent(this, DashboardActivity.class);
   // Intent intent = new Intent(this, LandingPageActivity.class);
    Intent intent = new Intent(this, Landingpagepro.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

  private void initiateServiceCall() {
    if (isValidForm()) {
      callOtpService();
    } else {
      ToastUtils.showSnackBar(mEtUserName, "Invalid Inputs");
    }
  }

  private void initiateVerificationCall() {
    if (isValidCode()) {
      OtpResponse otpResponse = mAppUtils.getObject("OTP_RESPONSE", OtpResponse.class);
      if (otpResponse != null) {
        mAppUtils.setUserId(otpResponse.getId());
        mAppUtils.setOtpVerified();
        if (otpResponse.getAdmin().equalsIgnoreCase(AppConstantsUtils.NO)) {
          mAppUtils.setUserType(false);
        } else {
          mAppUtils.setUserType(true);
        }
        forwardToLandingPage();
      }
    } else {
      ToastUtils.showSnackBar(mEtUserName, "Invalid Otp Code");
    }
  }

  private boolean isValidCode() {
    String str = getTextFromEt(mVerificationCodeEt);
    return (!str.isEmpty() && str.equalsIgnoreCase(mAppUtils.getCurrentOtp()));
  }

  private boolean isValidForm() {
    return isValidName() && !getTextFromEt(mEtUserName).isEmpty();
  }

  private boolean isValidName() {
    String name = getTextFromEt(mEtUserName);
    return !name.isEmpty() && (name.length() > 2);
  }

  private void callOtpService() {
    if (NetworkUtils.haveNetworkConnection(this)) {
      RestDataService service = new RestDataService();
      service.sendOtp(getTextFromEt(mEtMobileNumber), getTextFromEt(mEtUserName));
      mProgressDialog.show();
    } else {
      ToastUtils.showNoInternetSnackBar(mEtUserName);
    }
  }

  @Override protected void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  private void cancelProgress() {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }

  @Subscribe public void onOtpSent(SmsEvent.OtpSent otpSent) {
    cancelProgress();
    mOtpSendLayout.setVisibility(View.GONE);
    mVerificationCodeLayout.setVisibility(View.VISIBLE);
    mAppUtils.setCurrentOtp(otpSent.getResponse().getoTP());
    mAppUtils.saveObject("OTP_RESPONSE", otpSent.getResponse());
  }

  @Subscribe public void onOtpNotSent(SmsEvent.OtpSentFailed otpSentFailed) {
    cancelProgress();
    if (otpSentFailed.getReason() != null) {
      ToastUtils.showToast(this, otpSentFailed.getReason());
    } else if (otpSentFailed.getFailedResponse() != null) {
      ToastUtils.showToast(this, otpSentFailed.getFailedResponse().getMsg());
    }
  }
}
