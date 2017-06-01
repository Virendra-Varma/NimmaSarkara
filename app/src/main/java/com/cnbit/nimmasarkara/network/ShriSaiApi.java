package com.cnbit.nimmasarkara.network;

import com.cnbit.nimmasarkara.model.response.CountUser;
import com.cnbit.nimmasarkara.model.response.PostCommentResponse;
import com.cnbit.nimmasarkara.model.response.comment.CommentResponse;
import com.cnbit.nimmasarkara.model.response.events.EventResponse;
import com.cnbit.nimmasarkara.model.response.AlbumResponse;
import com.cnbit.nimmasarkara.model.response.FailedResponse;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.model.response.SuccessResponse;

import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by DC on 10/10/2016.
 */

public interface ShriSaiApi {
  @FormUrlEncoded @POST("sendOTP") Call<OtpResponse> sentOtp(
      @Field("mobile") String mobileNo, @Field("name") String name);

  @FormUrlEncoded @POST("getMedia") Call<List<List<MediaResponse>>> getMedia(
      @Field("type") String type, @Field("pageno") int pageNo, @Field("album_id") int albumId);

  @FormUrlEncoded @POST("myMedia") Call<List<List<MediaResponse>>> getYourMedia(
      @Field("type") String type, @Field("pageno") int pageNo, @Field("user_id") int userId);

  @FormUrlEncoded @POST("getComments") Call<List<CommentResponse>> getComments(
      @Field("post_id") String postId);

  @FormUrlEncoded @POST("postComment") Call<List<PostCommentResponse>> postComment(
      @Field("user_id") String userId, @Field("post_id") String postId,
      @Field("comment") String comment);

  @FormUrlEncoded @POST("postLike") Call<List<PostCommentResponse>> postLike(
      @Field("user_id") String userId, @Field("post_id") String postId);

  @Multipart @POST("uploadVideo") Call<FailedResponse> uploadVideo(
      @Part("user_id") int userId, @Part("title") String title, @Part("description") String desc,
      @Part MultipartBody.Part file);


  @Multipart @POST("uploadAudio") Call<FailedResponse> uploadAudio(
          @Part("user_id") int userId, @Part("title") String title, @Part("description") String desc,
          @Part MultipartBody.Part file);


  @Multipart @POST("uploadPicture") Call<FailedResponse> uploadPicture(
      @Part("user_id") int userId, @Part("title") String title, @Part("description") String desc,
      @Part MultipartBody.Part file);

  @FormUrlEncoded @POST("searchMedia") Call<List<List<MediaResponse>>> search(
      @Field("keyword") String keyword);

  @FormUrlEncoded @POST("myMedia") Call<List<List<MediaResponse>>> myMedia(
      @Field("user_id") int userId, @Field("type") String type);

  @GET("getEvents") Call<List<EventResponse>> getEvents();

  @FormUrlEncoded @POST("addEvent") Call<List<SuccessResponse>> addEvent(
      @Field("user_id") int eventId, @Field("title") String title,
      @Field("description") String description, @Field("datetime") String dateTime);

  @FormUrlEncoded @POST("editEvent") Call<List<SuccessResponse>> editEvent(
      @Field("event_id") int eventId, @Field("title") String title,
      @Field("description") String description, @Field("datetime") String dateTime);

  @FormUrlEncoded @POST("deleteEvent") Call<List<SuccessResponse>> deleteEvent(
      @Field("event_id") int eventId);

  @GET("getAlbums") Call<List<AlbumResponse>> getAlbums();



  @FormUrlEncoded
  @POST("countMedia")
  Call<List<CountUser>> getCount(
          @Field("user_id") String count);
}
