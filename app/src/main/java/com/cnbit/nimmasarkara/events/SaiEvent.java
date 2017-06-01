package com.cnbit.nimmasarkara.events;

import com.cnbit.nimmasarkara.model.response.PostCommentResponse;
import com.cnbit.nimmasarkara.model.response.AlbumResponse;
import com.cnbit.nimmasarkara.model.response.FailedResponse;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.model.response.SuccessResponse;
import com.cnbit.nimmasarkara.model.response.comment.CommentResponse;
import com.cnbit.nimmasarkara.model.response.events.Event;
import java.util.List;

/**
 * Created by DC on 10/6/2016.
 */

public class SaiEvent {

  public static class AlbumMediaItemClick {
    private List<MediaResponse> albumData;
    private int position;

    public AlbumMediaItemClick(List<MediaResponse> albumData, int position) {

      this.albumData = albumData;
      this.position = position;
    }

    public List<MediaResponse> getAlbumData() {
      return albumData;
    }

    public int getPosition() {
      return position;
    }
  }

  public static class VideoTitleClick {
    private int position;

    public VideoTitleClick(int position) {

      this.position = position;
    }

    public int getPosition() {
      return position;
    }
  }
  public static class AudioTitleClick {
    private int position;

    public AudioTitleClick(int position) {

      this.position = position;
    }

    public int getPosition() {
      return position;
    }
  }

  public static class TitleClick {
    private int position;

    public TitleClick(int position) {

      this.position = position;
    }

    public int getPosition() {
      return position;
    }
  }

  public static class AlbumClick {
    private int position;

    public AlbumClick(int position) {

      this.position = position;
    }

    public int getPosition() {
      return position;
    }
  }

  public static class PictureFetched {
    private List<List<MediaResponse>> responseList;

    public PictureFetched(List<List<MediaResponse>> responseList) {

      this.responseList = responseList;
    }

    public List<List<MediaResponse>> getResponseList() {
      return responseList;
    }
  }

  public static class VideoFetched {
    private List<List<MediaResponse>> responseList;

    public VideoFetched(List<List<MediaResponse>> responseList) {

      this.responseList = responseList;
    }

    public List<List<MediaResponse>> getResponseList() {
      return responseList;
    }
  }

  public static class AudioFetched {
    private List<List<MediaResponse>> responseList;

    public AudioFetched(List<List<MediaResponse>> responseList) {

      this.responseList = responseList;
    }

    public List<List<MediaResponse>> getResponseList() {
      return responseList;
    }
  }

  public static class PictureFetchFailed {
    private FailedResponse failedResponse;
    private String reason;

    public PictureFetchFailed(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class AudioFetchFailed {
    private FailedResponse failedResponse;
    private String reason;

    public AudioFetchFailed(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class VideoFetchFailed {
    private FailedResponse failedResponse;
    private String reason;

    public VideoFetchFailed(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class CommentsFetched {
    private List<CommentResponse> responseList;

    public CommentsFetched(List<CommentResponse> responseList) {

      this.responseList = responseList;
    }

    public List<CommentResponse> getResponseList() {
      return responseList;
    }
  }

  public static class CommentsFetchFailed {
    private FailedResponse failedResponse;
    private String reason;

    public CommentsFetchFailed(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class CommentPosted {
    private List<PostCommentResponse> responseList;

    public CommentPosted(List<PostCommentResponse> responseList) {

      this.responseList = responseList;
    }

    public List<PostCommentResponse> getResponseList() {
      return responseList;
    }
  }

  public static class CommentNotPosted {
    private FailedResponse failedResponse;
    private String reason;

    public CommentNotPosted(FailedResponse failedResponse, String reason) {

      this.failedResponse = failedResponse;
      this.reason = reason;
    }

    public FailedResponse getFailedResponse() {
      return failedResponse;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class MediaUploaded {
    private FailedResponse response;

    public MediaUploaded(FailedResponse response) {

      this.response = response;
    }

    public FailedResponse getResponse() {
      return response;
    }
  }

  public static class MediaNotUploaded {
    private FailedResponse response;
    private String reason;

    public MediaNotUploaded(FailedResponse response, String reason) {

      this.response = response;
      this.reason = reason;
    }

    public FailedResponse getResponse() {
      return response;
    }

    public String getReason() {
      return reason;
    }
  }

  public static class RefreshVideoList {
  }

  public static class RefreshPhotoList {
  }
  public static class RefreshAudioList {
  }

  public static class LoadMoreVideos {
  }

  public static class LoadMorePhotos {
  }

  public static class EventsFetched {
    private List<Event> events;

    public EventsFetched(List<Event> events) {

      this.events = events;
    }

    public List<Event> getEvents() {
      return events;
    }
  }

  public static class EventsFetchFailed extends FailedBaseResponse {
    public EventsFetchFailed(FailedResponse failedResponse, String reason) {
      super(failedResponse, reason);
    }
  }

  public static class EventAdded {
    private SuccessResponse response;

    public EventAdded(SuccessResponse response) {

      this.response = response;
    }

    public SuccessResponse getResponse() {
      return response;
    }
  }

  public static class EventNotAdded extends FailedBaseResponse {
    public EventNotAdded(FailedResponse failedResponse, String reason) {
      super(failedResponse, reason);
    }
  }

  public static class DeleteEvent {
    private int eventId;

    public DeleteEvent(int eventId) {

      this.eventId = eventId;
    }

    public int getEventId() {
      return eventId;
    }
  }

  public static class EventDeleted {
    private SuccessResponse response;

    public EventDeleted(SuccessResponse response) {

      this.response = response;
    }

    public SuccessResponse getResponse() {
      return response;
    }
  }

  public static class EventNotDeleted extends FailedBaseResponse {
    public EventNotDeleted(FailedResponse failedResponse, String reason) {
      super(failedResponse, reason);
    }
  }

  public static class AlbumsFetched {
    private List<AlbumResponse> responseList;

    public AlbumsFetched(List<AlbumResponse> responseList) {

      this.responseList = responseList;
    }

    public List<AlbumResponse> getResponseList() {
      return responseList;
    }
  }

  public static class AlbumsNotFetched extends FailedBaseResponse {
    public AlbumsNotFetched(FailedResponse failedResponse, String reason) {
      super(failedResponse, reason);
    }
  }
}
