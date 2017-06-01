package com.cnbit.nimmasarkara.network;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.LogUtils;
import com.cnbitstols.datastore.network.RestCallback;
import com.cnbit.nimmasarkara.model.response.FailedResponse;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by DC on 10/20/2016.
 * Media upload service that uploads media in background using @{@link IntentService}
 */

public class MediaUploadService extends IntentService {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final int mNotificationId = 1002;
    private ShriSaiRestService service = new ShriSaiRestService();
    private List<ChosenVideo> videoList;
    private List<ChosenAudio> audioList;
    private List<ChosenImage> imageList;

    String shareimagepath,sharevideopath,shareaudiopath;

    private ArrayList<String> mCaptions;
    private int mUserId;
    private int mCurrentUploadCount;
    private NotificationManager mNotificationManager;

    public MediaUploadService() {
        super("Media_upload_service");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mUserId = intent.getIntExtra(AppConstantsUtils.USER_ID, -1);
        String type = intent.getStringExtra(AppConstantsUtils.TYPE);


        if (type.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
            imageList = intent.getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA);
        } else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_IMAGE_TYPE)) {

            shareimagepath = intent.getStringExtra(AppConstantsUtils.SHARING_IMAGE_PATH);
        }

        else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_VIDEO_TYPE)) {

            sharevideopath = intent.getStringExtra(AppConstantsUtils.SHARING_VIDEO_PATH);
        }

        else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_AUDIO_TYPE)) {

            shareaudiopath = intent.getStringExtra(AppConstantsUtils.SHARING_AUDIO_PATH);
        }



        else if (type.equalsIgnoreCase(AppConstantsUtils.VIDEO)) {
            videoList = intent.getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA);
        } else {
            audioList = intent.getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA);
        }

        mCaptions = intent.getStringArrayListExtra(AppConstantsUtils.CAPTION_LIST);

        if (type.equalsIgnoreCase(AppConstantsUtils.PICTURE)) {
            initiatePicUpload();
        } else if (type.equalsIgnoreCase(AppConstantsUtils.VIDEO)) {
            initiateVideoUpload();
        } else if (type.equalsIgnoreCase(AppConstantsUtils.AUDIO)) {
            initiateAudioUpload();
        } else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_IMAGE_TYPE)) {
            initiateSharingPicUpload();

        }
        else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_VIDEO_TYPE)) {
            initiateSharingVidUpload();

        }
        else if (type.equalsIgnoreCase(AppConstantsUtils.SHARING_AUDIO_TYPE)) {
            initiateSharingAudUpload();

        }
    }


    private void initiateVideoUpload() {
        uploadVideo(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("video", videoList.get(mCurrentUploadCount).getOriginalPath()));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    private void initiateAudioUpload() {
        uploadAudio(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("audio", audioList.get(mCurrentUploadCount).getOriginalPath()));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    public void uploadVideo(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadVideo(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != videoList.size()) {
                    LogUtils.debugOut(responseObj.toString());
                    initiateVideoUpload();
                } else {
                    mNotificationManager.cancel(mNotificationId);
                    LogUtils.debugOut("All Upload Complete");
                    stopSelf();
                }
            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != videoList.size()) {
                    LogUtils.debugOut(errorObj + "---" + reason);
                    initiateVideoUpload();
                    createNotification(mCurrentUploadCount, mCaptions.size());
                } else {
                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();
                }
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String path) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        File file = new File(path);
        // create RequestBody instance from file
        okhttp3.RequestBody requestFile =
                okhttp3.RequestBody.create(okhttp3.MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    private void initiatePicUpload() {
        uploadPicture(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("picture", imageList.get(mCurrentUploadCount).getOriginalPath()));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    private void initiateSharingPicUpload() {
        uploadSharePicture(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("picture", shareimagepath));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    private void initiateSharingVidUpload() {
        uploadShareVideo(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("video", sharevideopath));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    private void initiateSharingAudUpload() {
        uploadShareAudio(mUserId, mCaptions.get(mCurrentUploadCount), "",
                prepareFilePart("audio", shareaudiopath));
        createNotification(mCurrentUploadCount, mCaptions.size());
    }


    public void uploadAudio(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadAudio(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != audioList.size()) {
                    LogUtils.debugOut(responseObj.toString());
                    initiateAudioUpload();
                } else {
                    mNotificationManager.cancel(mNotificationId);
                    LogUtils.debugOut("All Upload Complete");
                    stopSelf();
                }
            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != audioList.size()) {
                    LogUtils.debugOut(errorObj + "---" + reason);
                    initiateAudioUpload();
                    createNotification(mCurrentUploadCount, mCaptions.size());
                } else {
                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();
                }
            }
        });
    }


    public void uploadPicture(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadPicture(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != imageList.size())
                {
                    LogUtils.debugOut(responseObj.toString());
                    initiatePicUpload();
                } else {
                    mNotificationManager.cancel(mNotificationId);
                    LogUtils.debugOut("All Upload Complete");
                    stopSelf();
                }
            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {
                mCurrentUploadCount++;
                if (mCurrentUploadCount != imageList.size()) {
                    LogUtils.debugOut(errorObj + "---" + reason);
                    initiatePicUpload();
                    createNotification(mCurrentUploadCount, mCaptions.size());
                } else {
                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();
                }
            }
        });
    }


    public void uploadSharePicture(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadPicture(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {

                mNotificationManager.cancel(mNotificationId);
                LogUtils.debugOut("All Upload Complete");
                stopSelf();

            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {

                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();

            }
        });
    }



    public void uploadShareAudio(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadAudio(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {

                    mNotificationManager.cancel(mNotificationId);
                    LogUtils.debugOut("All Upload Complete");
                    stopSelf();

            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {

                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();

            }
        });
    }



    public void uploadShareVideo(int userId, String title, String desc, MultipartBody.Part file) {
        Call<FailedResponse> call =
                service.createService(ShriSaiApi.class).uploadVideo(userId, title, desc, file);
        call.enqueue(new RestCallback<FailedResponse, FailedResponse>(new FailedResponse()) {
            @Override
            public void sendSuccessResponse(@NonNull FailedResponse responseObj) {

                    mNotificationManager.cancel(mNotificationId);
                    LogUtils.debugOut("All Upload Complete");
                    stopSelf();

            }

            @Override
            public void sendErrorResponse(@Nullable FailedResponse errorObj, @Nullable String reason) {

                    LogUtils.debugOut("All Upload Complete/ERROR");
                    mNotificationManager.cancel(mNotificationId);
                    stopSelf();
                }

        });
    }

    //  mNotificationManager.notify(mNotificationId, notification);



    private void createNotification(int current, int total) {
        String content = "Uploading " + (current + 1) + " out of " + total;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.shri_logo)
                        .setContentTitle("Uploading Media")
                        .setContentText(content);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(mNotificationId, notification);

    }
}
