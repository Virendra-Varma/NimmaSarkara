package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.cnbit.nimmasarkara.R;
import android.widget.TextView;

import com.cnbit.nimmasarkara.model.response.AlbumResponse;

import java.util.List;

/**
 * Created by DC on 10/16/2016.
 */

public class AlbumAdapter extends BaseAdapter<AlbumResponse> {
  private Context context;
  public AlbumAdapter(Context context,List<AlbumResponse> mDataList, @LayoutRes int layoutResource,
      RecyclerView recyclerView) {
    super(mDataList, layoutResource, recyclerView);
    this.context = context;
  }

  @Override void bindData(RecyclerView.ViewHolder holder, AlbumResponse data) {
    AlbumHolder albumHolder = (AlbumHolder) holder;
    albumHolder.albumTitle.setText(data.getAlbumname());
    albumHolder.adapter = new AlbumMediaAdapter(context,data.getMedia(), R.layout.item_album_media);
    albumHolder.albumMediaRv.setAdapter(albumHolder.adapter);
  }

  @Override RecyclerView.ViewHolder getCurrentViewHolder(View view) {
    return new AlbumHolder(view);
  }

  @Override void sendLoadMoreEvent() {

  }

  private static class AlbumHolder extends RecyclerView.ViewHolder {
    private TextView albumTitle;
    private RecyclerView albumMediaRv;
    private AlbumMediaAdapter adapter;

    AlbumHolder(View itemView) {
      super(itemView);
      albumTitle = (TextView) itemView.findViewById(R.id.album_title);
      albumMediaRv = (RecyclerView) itemView.findViewById(R.id.album_media_rv);
      //albumMediaRv.setHasFixedSize(true);
      albumMediaRv.setItemAnimator(new DefaultItemAnimator());
      albumMediaRv.setLayoutManager(
          new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      /*itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          BusProvider.getInstance().post(new SaiEvent.AlbumClick(getLayoutPosition()));
        }
      });*/
    }
  }
}
