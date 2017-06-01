package com.cnbit.nimmasarkara.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harry on 2/17/2017.
 */

public class CountUser {
    @SerializedName("picture")
    String image;

    @SerializedName("video")
    String video;

    @SerializedName("audio")
    String audio;

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

}
