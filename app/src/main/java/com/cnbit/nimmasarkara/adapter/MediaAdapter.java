package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by DC on 10/18/2016.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

  private ArrayList<Object> mDataList;
  private Context mContext;
  private String type;

  public MediaAdapter(ArrayList<Object> mDataList, Context mContext, String type) {
    this.mDataList = mDataList;
    this.mContext = mContext;
    this.type = type;
  }

  @Override public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_media_selected, parent, false);
    return new MediaHolder(view);
  }

  @Override public void onBindViewHolder(MediaHolder holder, int position) {
    if (type.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
      ChosenImage obj = (ChosenImage) mDataList.get(position);
      Picasso.with(mContext).load(obj.getQueryUri()).into(holder.imageView);
    } else if(type.equalsIgnoreCase(AppConstantsUtils.VIDEO)){
      ChosenVideo chosenVideo = (ChosenVideo) mDataList.get(position);
      holder.imageView.setImageBitmap(createThumbnailFromPath(chosenVideo.getOriginalPath(),
          MediaStore.Images.Thumbnails.MINI_KIND));
    }
    else{
      ChosenAudio chosenAudio = (ChosenAudio) mDataList.get(position);
      holder.imageView.setImageResource(R.drawable.audiofileicom);
    //  holder.imageView.setImageBitmap(createThumbnailFromPath(chosenAudio.getOriginalPath(),
           //   MediaStore.Images.Thumbnails.MINI_KIND));
    }
  }

  public Bitmap createThumbnailFromPath(String filePath, int type) {
    return ThumbnailUtils.createVideoThumbnail(filePath, type);
  }

  @Override public int getItemCount() {
    return mDataList != null ? mDataList.size() : 0;
  }

  class MediaHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public MediaHolder(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.slider_image);
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BusProvider.getInstance().post(new SaiEvent.AlbumClick(getLayoutPosition()));
        }
      });
    }
  }
}
