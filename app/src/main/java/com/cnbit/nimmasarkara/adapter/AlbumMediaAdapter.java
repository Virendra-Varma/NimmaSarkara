package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DC on 11/2/2016.
 */

public class AlbumMediaAdapter extends BaseAdapter<MediaResponse> {
  private Context context;
  public AlbumMediaAdapter(Context context,List<MediaResponse> mDataList,
      @LayoutRes int layoutResource) {
    super(mDataList, layoutResource);
    this.context = context;
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

  @Override void bindData(RecyclerView.ViewHolder holder, MediaResponse data) {
    AlbumMediaHolder mediaHolder = (AlbumMediaHolder) holder;
    if (data.getMedia().endsWith(".mp4")) {
      new AlbumMediaAdapter.BackTask(mediaHolder.media).execute(
          AppConstantsUtils.BASE_URL + data.getMedia());
    } else {
      Picasso.with(context)
          .load(AppConstantsUtils.BASE_URL + data.getMedia())
          .into(mediaHolder.media);
    }
  }

  @Override RecyclerView.ViewHolder getCurrentViewHolder(View view) {
    return new AlbumMediaHolder(view);
  }

  @Override void sendLoadMoreEvent() {

  }

  private class AlbumMediaHolder extends RecyclerView.ViewHolder {
    private ImageView media;
    private TextView more;

    AlbumMediaHolder(View itemView) {
      super(itemView);
      media = (ImageView) itemView.findViewById(R.id.media);
      more = (TextView) itemView.findViewById(R.id.txtMore);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BusProvider.getInstance().post(new SaiEvent.AlbumMediaItemClick(mDataList,getLayoutPosition()));
        }
      });
    }
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
