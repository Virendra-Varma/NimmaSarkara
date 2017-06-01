package com.cnbit.nimmasarkara.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.bumptech.glide.Glide;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.CommentsAdapter;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.model.response.comment.Comment;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.utils.ViewStateSwitcher;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener , EasyVideoCallback {
  protected ViewStateSwitcher mStateSwitcher;
  private Toolbar mToolbar;
  private TextView mUserName;
  private TextView mDate;
  private TextView mTitle;
  private ImageView mSaiImage;
  private TextView mUpVote;
  private TextView mComments;
  private TextView mShareit;
  private EasyVideoPlayer mVideoView;
  private RecyclerView mCommentsRv;
  private View mEmptyState;
  private View mErrorState;
  private View mProgressState;
  private ImageView play;
  private MediaResponse mMediaResponse;
  private CommentsAdapter commentsAdapter;
  private List<Comment> mCommentList;
  private int mCurrentLikeCount;
  private AppUtils appUtils;
  private String mType;

  List<String> viewpageradapter;
  int position;
  List<String> medialist;
  String currentviduri;
  String s,type;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    setupUi();

    play=(ImageView)findViewById(R.id.play);

    viewpageradapter=new ArrayList<String>();
    Intent gettingintent=getIntent();
    viewpageradapter= (List<String>) gettingintent.getSerializableExtra(AppConstantsUtils.IMAGE_SWIPE);
    position=gettingintent.getIntExtra(AppConstantsUtils.CURRENT_POS,0);
    type=gettingintent.getStringExtra(AppConstantsUtils.TYPE);
   // ToastUtils.showToast(this,s);
    if(type!=null) {
      if (type.equals(AppConstantsUtils.PICTURE)) {
        play.setImageResource(R.drawable.playbutton);
      } else if (type.equals(AppConstantsUtils.VIDEO)) {
        play.setImageResource(R.drawable.fullscreen);
      }
    }

    if(mType!=null){
       if (mType.equals(AppConstantsUtils.VIDEO)) {
        play.setImageResource(R.drawable.fullscreen);
      }
    }

    play.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        medialist=new ArrayList<String>();
        Gettingjson();
        if(type!=null) {
          if (type.equals(AppConstantsUtils.PICTURE)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("images", (Serializable) medialist);
            bundle.putInt("position", position);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(ft, "slideshow");
          } else {
            //onBackPressed();

            Intent fullscreen = new Intent(getApplicationContext(), FullscreenVideoplaying.class);
            fullscreen.putExtra("Viduri", currentviduri);
            startActivity(fullscreen);
          }
        }
        else if(mType!=null){
          if(mType.equals(AppConstantsUtils.VIDEO)){
            Intent fullscreen = new Intent(getApplicationContext(), FullscreenVideoplaying.class);
            fullscreen.putExtra("Viduri", currentviduri);
            startActivity(fullscreen);
          }
        }
//        Log.v("ListJson",viewpageradapter.toString());

     //   Log.v("media",medialist.toString());
      }
    });

  }

   public void Gettingjson(){
     String json = new Gson().toJson(viewpageradapter);
     try {
       JSONArray array = new JSONArray(json);
       int length = array.length();
       for (int i = 0; i < length; i++) {
         JSONObject obj = array.getJSONObject(i);
         if(obj.has("media")){
           String nam=obj.getString("media");
           medialist.add(nam);
         }
       }
     }catch (JSONException e) {
       e.printStackTrace();
     }

   }

  private void setupUi() {

    mToolbar = (Toolbar) findViewById(R.id.toolbar);

    mToolbar.setNavigationIcon(R.mipmap.backwhite);
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       /* Intent intent=new Intent(getApplicationContext(),LandingPageActivity.class);
        intent.putExtra(AppConstantsUtils.PICTURE, "2");
        startActivity(intent);
        finish();*/
      //  ToastUtils.showToast(getApplicationContext(),"dfsdfsdfsd");
       // finish();
        if(type!=null) {
          if (type.equals(AppConstantsUtils.VIDEO)) {
            Intent intentSettings = new Intent(DetailActivity.this, LandingPageActivity.class);
            intentSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentSettings.putExtra(AppConstantsUtils.PICTURE, "2");
            startActivity(intentSettings);
            finish();
            ///  onBackPressed();

          } else {
            onBackPressed();
            // moveTaskToBack(false);
          }
        }
        else{
          onBackPressed();
        }
      }
    });

    mUserName = (TextView) findViewById(R.id.username);
    mDate = (TextView) findViewById(R.id.date);
    mTitle = (TextView) findViewById(R.id.title);
    mUpVote = (TextView) findViewById(R.id.upvote);
    mComments = (TextView) findViewById(R.id.comment);
    mShareit=(TextView)findViewById(R.id.shareit);
    mVideoView = (EasyVideoPlayer) findViewById(R.id.video_view);
    mSaiImage = (ImageView) findViewById(R.id.image_sai);
    mCommentsRv = (RecyclerView) findViewById(R.id.comments_rv);
    mEmptyState = findViewById(R.id.empty_state);
    mProgressState = findViewById(R.id.progress_state);
    mErrorState = findViewById(R.id.error_state);
  //  setupToolbar();

    mUpVote.setOnClickListener(this);
    mShareit.setOnClickListener(this);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
    mCommentsRv.setLayoutManager(mLayoutManager);
    mCommentsRv.setHasFixedSize(true);
    mCommentsRv.setItemAnimator(new DefaultItemAnimator());
    mCommentList = new ArrayList<>();
    commentsAdapter = new CommentsAdapter(mCommentList);
    mCommentsRv.setAdapter(commentsAdapter);
    appUtils = new AppUtils(this);
    mStateSwitcher = new ViewStateSwitcher.with(this).addContentState(mCommentsRv)
        .addEmptyState(mEmptyState)
        .addProgressState(mProgressState)
        .addErrorState(mErrorState)
        .build();
    mMediaResponse = getIntent().getParcelableExtra(AppConstantsUtils.MEDIA_DETAIL);
    mType = getIntent().getStringExtra(AppConstantsUtils.TYPE);
    if (mType == null) {
      if (mMediaResponse.getMedia().endsWith(".mp4")) {
        mType = AppConstantsUtils.VIDEO;
      } else {
        mType = AppConstantsUtils.PICTURE;
      }
    }

    if (mType.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
      mVideoView.setVisibility(View.GONE);
      mSaiImage.setVisibility(View.VISIBLE);
    } else {
      mVideoView.setVisibility(View.VISIBLE);
      mSaiImage.setVisibility(View.GONE);
    }

    mSaiImage.setClickable(true);
    mSaiImage.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mType.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
          Intent intent = new Intent(DetailActivity.this, FullScreenPhotoActivity.class);
          intent.putExtra(AppConstantsUtils.MEDIA_LINK, mMediaResponse.getMedia());
          startActivity(intent);
        }
      }
    });
    setupContent();
    initiateServiceCall();
  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    //moveTaskToBack(true);
  }

  private void setupContent() {

    if (mMediaResponse != null) {
      mUserName.setText(mMediaResponse.getPostedby());
      mTitle.setText(BasicUtils.removeDoubleQuotes(mMediaResponse.getTitle()));
      mCurrentLikeCount = Math.round(mMediaResponse.getLikes());
      mUpVote.setText(String.valueOf(mCurrentLikeCount));
      mDate.setText(
          BasicUtils.parseDate(mMediaResponse.getDatetime(), AppConstantsUtils.FROM_DATE_FORMAT,
              AppConstantsUtils.TO_DATE_FORMAT));

      if (mType.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {


       Picasso.with(this)
            .load(AppConstantsUtils.BASE_URL + mMediaResponse.getMedia())
            .error(R.drawable.sai_image)
            .into(mSaiImage);



       /* Glide
                .with(this)
                .load(AppConstantsUtils.BASE_URL + mMediaResponse.getMedia())
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(mSaiImage);*/
      }

      else {


        Uri uri = Uri.parse(AppConstantsUtils.BASE_URL + mMediaResponse.getMedia());
        currentviduri=String.valueOf(uri);

        mVideoView.setCallback(this);
        mVideoView.setSource(uri);
        mVideoView.setAutoPlay(true);

      }
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }


  private void setupToolbar() {
    setSupportActionBar(mToolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
      ab.setTitle(null);
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (appUtils.isCommented()) {
      appUtils.commented(false);
      initiateServiceCall();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    //Inflate the menu;this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_detail_screen, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;
      case R.id.menu_add_comment:
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra(AppConstantsUtils.POST_ID, mMediaResponse.getId());
        startActivity(intent);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void initiateServiceCall() {
    if (NetworkUtils.haveNetworkConnection(this)) {
      RestDataService service = new RestDataService();
      service.getComments(mMediaResponse.getId());
      mStateSwitcher.showProgressStates();
    } else {
      ToastUtils.showNoInternetSnackBar(mUserName);
    }
  }

  private void initiateUpvoteServiceCall() {
    if (NetworkUtils.haveNetworkConnection(this)) {
      RestDataService service = new RestDataService();
      AppUtils appUtils = new AppUtils(this);
      service.postLike(appUtils.getUserId(), mMediaResponse.getId());
    } else {
      ToastUtils.showNoInternetSnackBar(mUserName);
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

  @Subscribe public void onCommentsFetched(SaiEvent.CommentsFetched commentsFetched) {
    if (commentsFetched.getResponseList().isEmpty()) {
      mStateSwitcher.showEmptyStates();
    } else {
      mStateSwitcher.showContentStates();
      mCommentList.clear();
      mComments.setText(
          String.valueOf(commentsFetched.getResponseList().get(0).getComments().size()));
      mCommentList.addAll(commentsFetched.getResponseList().get(0).getComments());
      if (commentsAdapter != null) {
        commentsAdapter.notifyDataSetChanged();
      }
    }
  }

  @Subscribe public void OnCommentsFetchFailed(SaiEvent.CommentsFetchFailed commentsFetchFailed) {
    mStateSwitcher.showErrorStates();
    if (commentsFetchFailed.getReason() != null) {
      ToastUtils.showSnackBar(mCommentsRv, commentsFetchFailed.getReason());
    } else {
      ToastUtils.showSnackBar(mCommentsRv, commentsFetchFailed.getFailedResponse().getMsg());
    }
  }

  @Subscribe public void onCommentPosted(SaiEvent.CommentPosted posted) {
    if (mType.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
      appUtils.setLikedPic(true);
    } else {
      appUtils.setLikedVideo(true);
    }
    if (!posted.getResponseList().isEmpty()) {
      ToastUtils.showToast(this, posted.getResponseList().get(0).getStatus());
    }
  }

  @Subscribe public void onCommentNotPosted(SaiEvent.CommentNotPosted notPosted) {
    if (notPosted.getReason() != null) {
      ToastUtils.showSnackBar(mUpVote, notPosted.getReason());
    } else {
      ToastUtils.showSnackBar(mUpVote, notPosted.getFailedResponse().getMsg());
    }
  }

 /* @Override public void onBackPressed() {
    // super.onBackPressed();

  /*  if (s.equals(AppConstantsUtils.PICTURE)) {

      Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
      intent.putExtra(AppConstantsUtils.PICTURE, "1");
      startActivity(intent);
      this.finish();
    }

    else {
      Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
      intent.putExtra(AppConstantsUtils.PICTURE, "2");
      startActivity(intent);
      this.finish();
    }

    super.onBackPressed();

  }*/




 /* private void shareIt() {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    Uri shareimage=Uri.parse(AppConstantsUtils.BASE_URL + mMediaResponse.getMedia());
    sharingIntent.setType("image/*");
    sharingIntent.putExtra(Intent.EXTRA_STREAM, shareimage);
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
  }*/

  public void onShareItem() {

    Uri bmpUri = getLocalBitmapUri(mSaiImage);
    if (bmpUri != null) {
      Intent shareIntent = new Intent();
      shareIntent.setAction(Intent.ACTION_SEND);
      shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
      shareIntent.setType("image/*");
      startActivity(Intent.createChooser(shareIntent, "Share Image"));
    } else {
      Toast.makeText(getApplicationContext(), "Error while sharing image", Toast.LENGTH_SHORT).show();

    }
  }
  public Uri getLocalBitmapUri(ImageView imageView) {

    Drawable drawable = imageView.getDrawable();
    Bitmap bmp = null;
    if (drawable instanceof BitmapDrawable){
      bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    } else {
      return null;
    }
    Uri bmpUri = null;
    try {
      File file =  new File(Environment.getExternalStoragePublicDirectory(
              Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
      file.getParentFile().mkdirs();
      FileOutputStream out = new FileOutputStream(file);
      bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.close();
      bmpUri = Uri.fromFile(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bmpUri;
  }




  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.upvote:
        if (mMediaResponse != null) {
          mCurrentLikeCount++;
          mUpVote.setText(String.valueOf(mCurrentLikeCount));
        }
        initiateUpvoteServiceCall();
        break;
     /* case R.id.image_sai:
        Intent in = new Intent(DetailActivity.this, PlayVideoActivity.class);
        startActivity(in);*/
      case R.id.shareit:
        onShareItem();

    }
  }

  @Override
  public void onStarted(EasyVideoPlayer player) {

  }

  @Override
  public void onPaused(EasyVideoPlayer player) {

  }

  @Override
  public void onPreparing(EasyVideoPlayer player) {

  }

  @Override
  public void onPrepared(EasyVideoPlayer player) {

  }

  @Override
  public void onBuffering(int percent) {

  }

  @Override
  public void onError(EasyVideoPlayer player, Exception e) {

  }

  @Override
  public void onCompletion(EasyVideoPlayer player) {

  }

  @Override
  public void onRetry(EasyVideoPlayer player, Uri source) {

  }

  @Override
  public void onSubmit(EasyVideoPlayer player, Uri source) {

  }

  @Override
  protected void onPause() {
    super.onPause();
    if(mVideoView!=null){
      mVideoView.pause();
    }
  }

}
