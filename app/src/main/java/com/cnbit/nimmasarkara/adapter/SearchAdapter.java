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
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DC on 10/18/2016.
 */

public class SearchAdapter extends BaseAdapter<MediaResponse> {
  private Context context;

  public SearchAdapter(Context context, List<MediaResponse> mDataList,
      @LayoutRes int layoutResource, RecyclerView recyclerView) {
    super(mDataList, layoutResource, recyclerView);
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
    PhotoHolder photoHolder = (PhotoHolder) holder;
    photoHolder.mTitle.setText(BasicUtils.removeDoubleQuotes(data.getTitle()));
    photoHolder.mPostedBy.setText(data.getPostedby());
    photoHolder.upvote.setText(String.valueOf(data.getLikes()));
    photoHolder.date.setText(
        BasicUtils.parseDate(data.getDatetime(), AppConstantsUtils.FROM_DATE_FORMAT,
            AppConstantsUtils.TO_DATE_FORMAT));
    if (data.getMedia().endsWith(".mp4")) {
      new BackTask(photoHolder.mMediaPic).execute(AppConstantsUtils.BASE_URL + data.getMedia());
    } else {
      Picasso.with(context)
          .load(AppConstantsUtils.BASE_URL + data.getMedia())
          .error(R.drawable.sai_image)
          .into(photoHolder.mMediaPic);
    }
  }

  @Override RecyclerView.ViewHolder getCurrentViewHolder(View view) {
    return new PhotoHolder(view);
  }

  @Override void sendLoadMoreEvent() {
    //BusProvider.getInstance().post(new SaiEvent.LoadMorePhotos());
  }

  private static class PhotoHolder extends RecyclerView.ViewHolder {
    TextView mTitle;
    TextView mPostedBy;
    TextView upvote;
    ImageView mMediaPic;
    TextView date;

    PhotoHolder(View itemView) {
      super(itemView);
      mTitle = (TextView) itemView.findViewById(R.id.title);
      mPostedBy = (TextView) itemView.findViewById(R.id.username);
      upvote = (TextView) itemView.findViewById(R.id.txtUpVoteItemGrid);
      date = (TextView) itemView.findViewById(R.id.date);
      mMediaPic = (ImageView) itemView.findViewById(R.id.image_sai);

      View.OnClickListener clickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
          BusProvider.getInstance().post(new SaiEvent.TitleClick(getLayoutPosition()));
        }
      };
      mTitle.setOnClickListener(clickListener);
      mMediaPic.setOnClickListener(clickListener);
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
