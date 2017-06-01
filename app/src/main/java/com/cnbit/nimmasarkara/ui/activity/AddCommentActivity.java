package com.cnbit.nimmasarkara.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.squareup.otto.Subscribe;

public class AddCommentActivity extends AppCompatActivity {

  AppUtils appUtils;
  private Button mPostBtn;
  private EditText mCommentEt;
  private String mPostId;
  private ProgressDialog mProgressDialog;
  private Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_comment);
    mPostBtn = (Button) findViewById(R.id.post_btn);
    mCommentEt = (EditText) findViewById(R.id.etComment);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setupToolbar();
    mPostId = getIntent().getStringExtra(AppConstantsUtils.POST_ID);
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setMessage("Posting Comment");
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setCanceledOnTouchOutside(false);
    appUtils = new AppUtils(this);
    mPostBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        initiateServiceCall();
      }
    });
  }

  private void setupToolbar() {
    setSupportActionBar(mToolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
      ab.setTitle("Post Comment");
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initiateServiceCall() {
    if (isValidForm()) {
      if (NetworkUtils.haveNetworkConnection(this)) {
        RestDataService service = new RestDataService();
        service.postComment(appUtils.getUserId(), mPostId, BasicUtils.getTextFromEt(mCommentEt));
        mProgressDialog.show();
      } else {
        ToastUtils.showNoInternetSnackBar(mPostBtn);
      }
    } else {
      ToastUtils.showToast(this, "Comments cannot be empty");
    }
  }

  private boolean isValidForm() {
    return !BasicUtils.getTextFromEt(mCommentEt).isEmpty();
  }

  @Override protected void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void onCommentPosted(SaiEvent.CommentPosted posted) {
    cancelProgress();
    mCommentEt.setText("");
    appUtils.commented(true);
    if (!posted.getResponseList().isEmpty()) {
      ToastUtils.showToast(this, posted.getResponseList().get(0).getStatus());
    }
    this.finish();
  }

  @Subscribe public void onCommentNotPosted(SaiEvent.CommentNotPosted notPosted) {
    cancelProgress();
    if (notPosted.getReason() != null) {
      ToastUtils.showSnackBar(mPostBtn, notPosted.getReason());
    } else {
      ToastUtils.showSnackBar(mPostBtn, notPosted.getFailedResponse().getMsg());
    }
  }

  private void cancelProgress() {
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }
  }
}
