package com.cnbit.nimmasarkara.model.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DC on 10/9/2016.
 */

public class MediaResponse implements Parcelable {

  @SerializedName("id") @Expose public String id;
  @SerializedName("title") @Expose public String title;
  @SerializedName("description") @Expose public String description;
  @SerializedName("likes") @Expose public Long likes;
  @SerializedName("postedby") @Expose public String postedby;
  @SerializedName("datetime") @Expose public String datetime;
  @SerializedName("media") @Expose public String media;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getLikes() {
    return likes;
  }

  public void setLikes(Long likes) {
    this.likes = likes;
  }

  public String getPostedby() {
    return postedby;
  }

  public void setPostedby(String postedby) {
    this.postedby = postedby;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public String getMedia() {
    return media;
  }

  public void setMedia(String media) {
    this.media = media;
  }

  @Override public String toString() {
    return "MediaResponse{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", likes=" + likes +
        ", postedby='" + postedby + '\'' +
        ", datetime='" + datetime + '\'' +
        ", media='" + media + '\'' +
        '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeValue(this.likes);
    dest.writeString(this.postedby);
    dest.writeString(this.datetime);
    dest.writeString(this.media);
  }

  protected MediaResponse(Parcel in) {
    this.id = in.readString();
    this.title = in.readString();
    this.description = in.readString();
    this.likes = (Long) in.readValue(Long.class.getClassLoader());
    this.postedby = in.readString();
    this.datetime = in.readString();
    this.media = in.readString();
  }

  public static final Parcelable.Creator<MediaResponse> CREATOR =
      new Parcelable.Creator<MediaResponse>() {
        @Override public MediaResponse createFromParcel(Parcel source) {
          return new MediaResponse(source);
        }

        @Override public MediaResponse[] newArray(int size) {
          return new MediaResponse[size];
        }
      };
}
