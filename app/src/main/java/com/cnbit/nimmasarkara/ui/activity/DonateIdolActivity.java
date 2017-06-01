package com.cnbit.nimmasarkara.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cnbit.nimmasarkara.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DonateIdolActivity extends AppCompatActivity {

  public static final String TAG = DonateIdolActivity.class.getSimpleName();

  public static String hashCal(String type, String str) {
    byte[] hashseq = str.getBytes();
    StringBuilder hexString = new StringBuilder();
    try {
      MessageDigest algorithm = MessageDigest.getInstance(type);
      algorithm.reset();
      algorithm.update(hashseq);
      byte messageDigest[] = algorithm.digest();
      for (byte aMessageDigest : messageDigest) {
        String hex = Integer.toHexString(0xFF & aMessageDigest);
        if (hex.length() == 1) {
          hexString.append("0");
        }
        hexString.append(hex);
      }
    } catch (NoSuchAlgorithmException nsae) {
      nsae.printStackTrace();
    }
    return hexString.toString();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donate_idol);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
    }
    Button donateBtn = (Button) findViewById(R.id.donate_idol);
    donateBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //initDonation();
        //startActivity(new Intent(DonateIdolActivity.this, PayUMoneyActivity.class));
      }
    });
    //Merchant ID - 5651504

    //Key : rjQUPktU
    //· Salt : e5iIg1jwi8

  }

 /* private void initDonation() {
    String udf1, udf2, udf3, udf4, udf5;
    udf1 = udf2 = udf3 = udf4 = udf5 = "";
    String key = "rjQUPktU";
    String txnId = "0nf7" + System.currentTimeMillis();
    double amount = 0.1;
    String productInfo = "Donation";
    String firstName = "Dinesh";
    String email = "dineshsuneldcs@gmail.com";
    String salt = "e5iIg1jwi8";
    PayUmoneySdkInitilizer.PaymentParam.Builder builder =
        new PayUmoneySdkInitilizer.PaymentParam.Builder().setMerchantId("5651504")
            .setKey(key)
            .setIsDebug(true)
            .setAmount(amount)
            .setTnxId(txnId)
            .setPhone("777276910")
            .setProductName(productInfo)
            .setFirstName(firstName)
            .setEmail(email)
            //.setsUrl("http://www.cnbitsols.com/onsuccess")
            .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
            .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
            //.setfUrl("http://www.cnbitsols.com/onfail")
            .setUdf1("")
            .setUdf2("")
            .setUdf3("")
            .setUdf4("")
            .setUdf5("");
    PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

    String mHash = hashCal("SHA-512", key + "|" +
        txnId + "|" +
        amount + "|" +
        productInfo + "|" +
        email + "|" +
        firstName + "|" +
        udf1 + "|" +
        udf2 + "|" +
        udf3 + "|" +
        udf4 + "|" +
        udf5 + "|" +
        salt);

    paymentParam.setMerchantHash(mHash);
    PayUmoneySdkInitilizer.startPaymentActivityForResult(DonateIdolActivity.this, paymentParam);
  }*/

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

 /* @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
      if (data != null && data.hasExtra("result")) {
        String responsePayUmoney = data.getStringExtra("result");
        if (SdkHelper.checkForValidString(responsePayUmoney)) showDialogMessage(responsePayUmoney);
        LogUtils.debugOut(responsePayUmoney);
      } else {
        showDialogMessage("Unable to get Status of Payment");
      }
      if (resultCode == RESULT_OK) {
        Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
        String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
        showDialogMessage("Payment Success Id : " + paymentId);
      } else if (resultCode == RESULT_CANCELED) {
        Log.i(TAG, "failure");
        showDialogMessage("cancelled");
      } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
        Log.i("app_activity", "failure");

        if (data != null) {
          if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

          } else {
            showDialogMessage("failure");
          }
        }
        //Write your code if there's no result
      } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
        Log.i(TAG, "User returned without login");
        showDialogMessage("User returned without login");
      }
    }
  }*/

  private void showDialogMessage(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(TAG);
    builder.setMessage(message);
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.show();
  }
}
