package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by DC on 10/10/2016.
 * <p>
 * Video Adapter for video list take basic methods from @{@link BaseAdapter}
 */

public class VideoAdapter extends BaseAdapter<MediaResponse> {

    Context context;
    private LruCache<String, Bitmap> mMemoryCache;



    public VideoAdapter(List<MediaResponse> mDataList, @LayoutRes int layoutResource,
                        RecyclerView recyclerView, Context context) {
        super(mDataList, layoutResource, recyclerView);

        this.context = context;

       final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    private static Bitmap retrieveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            } else {
                mediaMetadataRetriever.setDataSource(videoPath);
            }
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    void bindData(RecyclerView.ViewHolder holder, final MediaResponse data) {
        final PhotoHolder photoHolder = (PhotoHolder) holder;
        photoHolder.setIsRecyclable(false);
        photoHolder.mTitle.setText(BasicUtils.removeDoubleQuotes(data.getTitle()));
        photoHolder.mPostedBy.setText(data.getPostedby());
        photoHolder.upvote.setText(String.valueOf(data.getLikes()));
        photoHolder.date.setText(
                BasicUtils.parseDate(data.getDatetime(), AppConstantsUtils.FROM_DATE_FORMAT,
                        AppConstantsUtils.TO_DATE_FORMAT));


        Bitmap bitmap = getBitmapFromMemCache(AppConstantsUtils.BASE_URL + data.getMedia());

        if (bitmap != null) {
            Log.e("NOTNULLBITMAP","NOTNULLBITMAP");
            photoHolder.mMediaPic.setImageBitmap(bitmap);
        }
        else {

            new AsyncTask<String, String, String>() {
                Bitmap bitmapVideo;
                String location;


                @Override
                protected String doInBackground(String... strings) {
                    try {
                        //Your method call here
                        bitmapVideo = retrieveVideoFrameFromVideo(strings[0]);
                        location=strings[0];
                      //  addBitmapToMemoryCache(location, bitmapVideo);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String id) {
                    super.onPostExecute(id);

                    if (bitmapVideo != null) {
                        //Load your bitmap here
                        photoHolder.mMediaPic.setImageBitmap(bitmapVideo);
                        addBitmapToMemoryCache(location, bitmapVideo);


                    } else {
                        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sai_image);
                        photoHolder.mMediaPic.setImageBitmap(largeIcon);
                        addBitmapToMemoryCache(location, largeIcon);

                    }
                }
            }.execute(AppConstantsUtils.BASE_URL + data.getMedia());

        }




/*
        Bitmap bitmap = getBitmapFromMemCache(AppConstantsUtils.BASE_URL + data.getMedia());

        if (bitmap != null) {
            Log.e("NOTNULLBITMAP","NOTNULLBITMAP");
            photoHolder.mMediaPic.setImageBitmap(bitmap);
        } else {
            Log.e("Downloading","Downloading image");
        new BackTask(photoHolder.mMediaPic).execute(
                AppConstantsUtils.BASE_URL + data.getMedia());
       }
*/

    }


    @Override
    RecyclerView.ViewHolder getCurrentViewHolder(View view) {
        return new PhotoHolder(view);
    }

    @Override
    void sendLoadMoreEvent() {
        BusProvider.getInstance().post(new SaiEvent.LoadMoreVideos());
    }

    private static class PhotoHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mPostedBy;
        TextView upvote;
        ImageView mMediaPic;
        TextView date;

        PhotoHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mPostedBy = (TextView) itemView.findViewById(R.id.username);
            upvote = (TextView) itemView.findViewById(R.id.txtUpVoteItemGrid);
            date = (TextView) itemView.findViewById(R.id.date);
            mMediaPic = (ImageView) itemView.findViewById(R.id.image_sai);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusProvider.getInstance().post(new SaiEvent.VideoTitleClick(getLayoutPosition()));
                }
            };
            mTitle.setOnClickListener(clickListener);
            mMediaPic.setOnClickListener(clickListener);
        }
    }

    private class BackTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;
        String location;

        BackTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.e("URLHARRY", params[0]);
            // return retrieveVideoFrameFromVideo(params[0]);
            location=params[0];
            final Bitmap bitmap = retrieveVideoFrameFromVideo(params[0]);
            if (params[0] != null && bitmap != null) {
                addBitmapToMemoryCache(params[0], bitmap);
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ToastUtils.showToast(context,"Suckers punch");

            if (bitmap != null) {

                imageView.setImageBitmap(bitmap);
                Log.d("Hai", imageView.toString());
                addBitmapToMemoryCache(location, bitmap);

            } else {
                // imageView.setImageBitmap(null);
                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sai_image);
                imageView.setImageBitmap(largeIcon);
                addBitmapToMemoryCache(location, largeIcon);

                Log.d("Hai", imageView.toString());
            }


        }
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public final void evictAll (){
        mMemoryCache.evictAll();
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


}
