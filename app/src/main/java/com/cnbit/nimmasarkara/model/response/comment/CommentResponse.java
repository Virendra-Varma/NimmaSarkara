package com.cnbit.nimmasarkara.model.response.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DC on 10/9/2016.
 */

public class CommentResponse {
  @SerializedName("comments") @Expose public List<Comment> comments = new ArrayList<>();

  @Override public String toString() {
    return "CommentResponse{" +
        "comments=" + comments +
        '}';
  }

  public List<Comment> getComments() {
    return comments;
  }
}