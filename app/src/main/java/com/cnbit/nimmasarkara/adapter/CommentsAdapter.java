package com.cnbit.nimmasarkara.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.cnbit.nimmasarkara.R;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.model.response.comment.Comment;

import java.util.List;

/**
 * Created by DC on 10/6/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
  private List<Comment> commentList;

  public CommentsAdapter(List<Comment> commentList) {
    this.commentList = commentList;
  }

  @Override public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
    return new MyViewHolder(view);
  }

  @Override public void onBindViewHolder(CommentsAdapter.MyViewHolder holder, int position) {
    Comment comment = commentList.get(position);
    holder.comment.setText(comment.getComment());
  }

  @Override public int getItemCount() {
    return commentList.size();
  }

  class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView userPic;
    TextView comment;

    MyViewHolder(View itemView) {
      super(itemView);

      userPic = (ImageView) itemView.findViewById(R.id.userPic);
      comment = (TextView) itemView.findViewById(R.id.comment);
    }
  }
}
