package com.cnbit.nimmasarkara.model.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DC on 10/16/2016.
 */

public class AlbumResponse implements Parcelable {
  public static final Parcelable.Creator<AlbumResponse> CREATOR =
      new Parcelable.Creator<AlbumResponse>() {
        @Override public AlbumResponse createFromParcel(Parcel source) {
          return new AlbumResponse(source);
        }

        @Override public AlbumResponse[] newArray(int size) {
          return new AlbumResponse[size];
        }
      };
  @SerializedName("albumid") @Expose public String albumid;
  @SerializedName("albumname") @Expose public String albumname;
  @SerializedName("media") @Expose public List<MediaResponse> media = new ArrayList<>();
  @SerializedName("userid") @Expose public String userid;

  @Override public String toString() {
    return "AlbumResponse{" +
        "albumid='" + albumid + '\'' +
        ", albumname='" + albumname + '\'' +
        ", media=" + media +
        ", userid='" + userid + '\'' +
        ", createdby='" + createdby + '\'' +
        '}';
  }

  @SerializedName("createdby") @Expose public String createdby;

  public AlbumResponse() {
  }

  protected AlbumResponse(Parcel in) {
    this.albumid = in.readString();
    this.albumname = in.readString();
    this.media = in.createTypedArrayList(MediaResponse.CREATOR);
    this.userid = in.readString();
    this.createdby = in.readString();
  }

  public String getAlbumid() {
    return albumid;
  }

  public String getAlbumname() {
    return albumname;
  }

  public List<MediaResponse> getMedia() {
    return media;
  }

  public String getUserid() {
    return userid;
  }

  public String getCreatedby() {
    return createdby;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.albumid);
    dest.writeString(this.albumname);
    dest.writeTypedList(this.media);
    dest.writeString(this.userid);
    dest.writeString(this.createdby);
  }
}
