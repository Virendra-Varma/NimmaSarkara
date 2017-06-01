package com.cnbit.nimmasarkara.ui.activity;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.TouchImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FullScreenPhotoActivity extends AppCompatActivity {
  private TouchImageView image;
  DecimalFormat df;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_full_screen_photo);
    image = (TouchImageView) findViewById(R.id.full_screen_photo);
    image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
      @Override
      public void onMove() {
        PointF point = image.getScrollPosition();
        RectF rect = image.getZoomedRect();
        float currentZoom = image.getCurrentZoom();
        boolean isZoomed = image.isZoomed();

      }
    });
    df = new DecimalFormat("#.##");
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
    }
    String url =
        AppConstantsUtils.BASE_URL + getIntent().getStringExtra(AppConstantsUtils.MEDIA_LINK);
    Picasso.with(this).load(url).error(R.drawable.sai_image).into(image);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }



}



