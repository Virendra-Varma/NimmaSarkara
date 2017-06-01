package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.bumptech.glide.Glide;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by DC on 10/6/2016.
 * Photo Adapter for video list take basic methods from @{@link BaseAdapter}
 */

public class PhotoAdapter extends BaseAdapter<MediaResponse> {
  private Context context;

  public PhotoAdapter(Context context, List<MediaResponse> mDataList, @LayoutRes int layoutResource,
      RecyclerView recyclerView) {
    super(mDataList, layoutResource, recyclerView);
    this.context = context;
  }

  @Override void bindData(RecyclerView.ViewHolder holder, MediaResponse data) {
    PhotoHolder photoHolder = (PhotoHolder) holder;
    photoHolder.mTitle.setText(BasicUtils.removeDoubleQuotes(data.getTitle()));
    photoHolder.mPostedBy.setText(data.getPostedby());
    photoHolder.upvote.setText(String.valueOf(data.getLikes()));
    photoHolder.date.setText(
        BasicUtils.parseDate(data.getDatetime(), AppConstantsUtils.FROM_DATE_FORMAT,
            AppConstantsUtils.TO_DATE_FORMAT));


   Picasso.with(context)
        .load(AppConstantsUtils.BASE_URL + data.getMedia())
        .error(R.mipmap.ic_launcher)
        .into(photoHolder.mMediaPic);

  /*  Glide
            .with(context)
            .load(AppConstantsUtils.BASE_URL + data.getMedia())
            .centerCrop()
           // .placeholder(R.drawable.loading_spinner)
            .crossFade()
            .into(photoHolder.mMediaPic);*/


  }

  @Override RecyclerView.ViewHolder getCurrentViewHolder(View view) {
    return new PhotoHolder(view);
  }

  @Override void sendLoadMoreEvent() {
    BusProvider.getInstance().post(new SaiEvent.LoadMorePhotos());
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
}
