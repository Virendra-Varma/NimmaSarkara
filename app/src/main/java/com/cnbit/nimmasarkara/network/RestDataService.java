package com.cnbit.nimmasarkara.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.model.response.PostCommentResponse;
import com.cnbit.nimmasarkara.model.response.comment.CommentResponse;
import com.cnbit.nimmasarkara.model.response.events.EventResponse;
import com.cnbitstols.datastore.network.RestCallback;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SmsEvent;
import com.cnbit.nimmasarkara.model.response.AlbumResponse;
import com.cnbit.nimmasarkara.model.response.FailedResponse;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.model.response.SuccessResponse;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by DC on 10/10/2016.
 */

public class RestDataService {

  private ShriSaiRestService service;

  public RestDataService() {
    this.service = new ShriSaiRestService();
  }

  public Call<OtpResponse> sendOtp(String mobileNo, String name) {
    Call<OtpResponse> call = service.createService(ShriSaiApi.class).sentOtp(mobileNo, name);
    call.enqueue(new RestCallback<OtpResponse, FailedResponse>(new FailedResponse()) {
      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SmsEvent.OtpSentFailed(errorObj, reason));
      }

      @Override public void sendSuccessResponse(@NonNull OtpResponse responseObj) {
        BusProvider.getInstance().post(new SmsEvent.OtpSent(responseObj));
      }
    });

    return call;
  }

  public void getMedia(final String type, int pageNo, int albumId) {
    Call<List<List<MediaResponse>>> call =
        service.createService(ShriSaiApi.class).getMedia(type, pageNo, albumId);
    call.enqueue(new RestCallback<List<List<MediaResponse>>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<List<MediaResponse>> responseObj) {
        if (type.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
          BusProvider.getInstance().post(new SaiEvent.PictureFetched(responseObj));
        } else if (type.equalsIgnoreCase(AppConstantsUtils.VIDEO)){
          BusProvider.getInstance().post(new SaiEvent.VideoFetched(responseObj));
        } else {
          BusProvider.getInstance().post(new SaiEvent.AudioFetched(responseObj));}
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        if (type.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
          BusProvider.getInstance().post(new SaiEvent.PictureFetchFailed(errorObj, reason));
        } else {
          BusProvider.getInstance().post(new SaiEvent.VideoFetchFailed(errorObj, reason));
        }
      }
    });
  }

  public void getYourMedia(String type, int pageNo, int userId) {
    Call<List<List<MediaResponse>>> call =
        service.createService(ShriSaiApi.class).getYourMedia(type, pageNo, userId);
    call.enqueue(new RestCallback<List<List<MediaResponse>>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<List<MediaResponse>> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.PictureFetched(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.PictureFetchFailed(errorObj, reason));
      }
    });
  }

  public void search(String keyword, int pageNo) {
    Call<List<List<MediaResponse>>> call = service.createService(ShriSaiApi.class).search(keyword);
    call.enqueue(new RestCallback<List<List<MediaResponse>>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<List<MediaResponse>> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.PictureFetched(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.PictureFetchFailed(errorObj, reason));
      }
    });
  }

  public void getComments(String postId) {
    Call<List<CommentResponse>> call = service.createService(ShriSaiApi.class).getComments(postId);
    call.enqueue(new RestCallback<List<CommentResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<CommentResponse> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.CommentsFetched(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.CommentsFetchFailed(errorObj, reason));
      }
    });
  }

  public void postComment(String userId, String postId, String comment) {
    Call<List<PostCommentResponse>> call =
        service.createService(ShriSaiApi.class).postComment(userId, postId, comment);
    call.enqueue(new RestCallback<List<PostCommentResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<PostCommentResponse> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.CommentPosted(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.CommentNotPosted(errorObj, reason));
      }
    });
  }

  public void postLike(String userId, String postId) {
    Call<List<PostCommentResponse>> call =
        service.createService(ShriSaiApi.class).postLike(userId, postId);
    call.enqueue(new RestCallback<List<PostCommentResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<PostCommentResponse> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.CommentPosted(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.CommentNotPosted(errorObj, reason));
      }
    });
  }

  public void uploadPicture(int userId, String title, String desc, MultipartBody.Part file) {
    Call<FailedResponse> call =
        service.createService(ShriSaiApi.class).uploadPicture(userId, title, desc, file);
    call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull FailedResponse responseObj) {
        BusProvider.getInstance().post(new SaiEvent.MediaUploaded(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.MediaNotUploaded(errorObj, reason));
      }
    });
  }

  public void uploadVideo(int userId, String title, String desc, MultipartBody.Part file) {
    Call<FailedResponse> call =
        service.createService(ShriSaiApi.class).uploadVideo(userId, title, desc, file);
    call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull FailedResponse responseObj) {
        BusProvider.getInstance().post(new SaiEvent.MediaUploaded(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.MediaNotUploaded(errorObj, reason));
      }
    });
  }


  public void getEvents() {
    Call<List<EventResponse>> call = service.createService(ShriSaiApi.class).getEvents();
    call.enqueue(new RestCallback<List<EventResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<EventResponse> responseObj) {
        if (!responseObj.isEmpty()) {
          BusProvider.getInstance()
              .post(new SaiEvent.EventsFetched(responseObj.get(0).getEvents()));
        }
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.EventsFetchFailed(errorObj, reason));
      }
    });
  }

  public void addEvent(int userId, String title, String description, String dateTime) {
    Call<List<SuccessResponse>> call =
        service.createService(ShriSaiApi.class).addEvent(userId, title, description, dateTime);
    call.enqueue(new RestCallback<List<SuccessResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<SuccessResponse> responseObj) {
        if (!responseObj.isEmpty()) {
          BusProvider.getInstance().post(new SaiEvent.EventAdded(responseObj.get(0)));
        }
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.EventNotAdded(errorObj, reason));
      }
    });
  }

  public void deleteEvent(int eventId) {
    Call<List<SuccessResponse>> call = service.createService(ShriSaiApi.class).deleteEvent(eventId);
    call.enqueue(new RestCallback<List<SuccessResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<SuccessResponse> responseObj) {
        if (!responseObj.isEmpty()) {
          BusProvider.getInstance().post(new SaiEvent.EventDeleted(responseObj.get(0)));
        } else {
          BusProvider.getInstance().post(new SaiEvent.EventNotDeleted(null, "Empty Response"));
        }
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.EventNotDeleted(errorObj, reason));
      }
    });
  }

  public void getAlbums() {
    Call<List<AlbumResponse>> call = service.createService(ShriSaiApi.class).getAlbums();
    call.enqueue(new RestCallback<List<AlbumResponse>, FailedResponse>(new FailedResponse()) {
      @Override public void sendSuccessResponse(@NonNull List<AlbumResponse> responseObj) {
        BusProvider.getInstance().post(new SaiEvent.AlbumsFetched(responseObj));
      }

      @Override
      public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
        BusProvider.getInstance().post(new SaiEvent.AlbumsNotFetched(errorObj, reason));
      }
    });
  }
}
