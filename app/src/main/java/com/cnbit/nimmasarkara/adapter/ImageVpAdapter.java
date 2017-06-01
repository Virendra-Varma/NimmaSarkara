package com.cnbit.nimmasarkara.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cnbit.nimmasarkara.R;
import android.widget.LinearLayout;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.squareup.picasso.Picasso;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DC on 10/15/2016.
 * Gallery Like Image Slider Adapter
 */

public class ImageVpAdapter extends PagerAdapter {
  public static final int DRAWABLE = 1;
  public static final int URL = 2;
  private List<Object> mImagePaths;
  private @ImageResoure int type;

  public ImageVpAdapter(List<Object> imagePaths) {
    this.mImagePaths = imagePaths;
    this.type = DRAWABLE;
  }

  public ImageVpAdapter(List<Object> imagePaths, @ImageResoure int type) {
    this.mImagePaths = imagePaths;
    this.type = type;
  }

  private static Bitmap retrieveVideoFrameFromVideo(String videoPath) {
    Bitmap bitmap = null;
    MediaMetadataRetriever mediaMetadataRetriever = null;
    try {
      mediaMetadataRetriever = new MediaMetadataRetriever();
      if (Build.VERSION.SDK_INT >= 14) {
        mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
      } else {
        mediaMetadataRetriever.setDataSource(videoPath);
      }
      //   mediaMetadataRetriever.setDataSource(videoPath);
      bitmap = mediaMetadataRetriever.getFrameAtTime();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (mediaMetadataRetriever != null) {
        mediaMetadataRetriever.release();
      }
    }
    return bitmap;
  }

  @Override public int getCount() {
    return this.mImagePaths.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    ImageView imgDisplay;
    View viewLayout = LayoutInflater.from(container.getContext())
        .inflate(R.layout.item_image_slider, container, false);
    imgDisplay = (ImageView) viewLayout.findViewById(R.id.slider_image);
    if (type == DRAWABLE) {
      Picasso.with(container.getContext())
          .load((Integer) mImagePaths.get(position))
          .into(imgDisplay);
    } else if (type == URL) {

      MediaResponse response = (MediaResponse) mImagePaths.get(position);
      if (response.getMedia().endsWith(".mp4")) {
        new ImageVpAdapter.BackTask(imgDisplay).execute(
            AppConstantsUtils.BASE_URL + response.getMedia());
      } else {
        Picasso.with(container.getContext())
            .load(AppConstantsUtils.BASE_URL + response.getMedia())
            .into(imgDisplay);
      }
    }

    container.addView(viewLayout);
    return viewLayout;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((LinearLayout) object);
  }

  @Retention(RetentionPolicy.SOURCE)

  @IntDef({ DRAWABLE, URL }) public @interface ImageResoure {
  }

  private class BackTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    BackTask(ImageView imageView) {
      this.imageView = imageView;
    }

    @Override protected Bitmap doInBackground(String... params) {
      return retrieveVideoFrameFromVideo(params[0]);
    }

    @Override protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      imageView.setImageBitmap(bitmap);
    }
  }
}