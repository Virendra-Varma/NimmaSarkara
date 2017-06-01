package com.cnbit.nimmasarkara.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.MediaAdapter;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.network.MediaUploadService;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.utils.LogUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiSelectionActivity extends AppCompatActivity {
  private String mType;
  private ImageView coverImage;
  private ArrayList<Object> list;
  private ArrayList<String> captions;
  private ArrayList<String> imagePaths;
  private EditText mCaptionEt;
  private int lastPosition;
  private Uri imgUri,videoUri,audioUri;
  String originalPath;
  private Map<Integer, String> captionMap;
  Button rotate;
  int provoteX=0,provoteY=90;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_multi_selection);
    coverImage = (ImageView) findViewById(R.id.current_selected_pic);
    mCaptionEt = (EditText) findViewById(R.id.caption);
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send);
    final AppUtils appUtils = new AppUtils(this);
    rotate=(Button)findViewById(R.id.rotate);
    rotate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      //  Matrix matrix = new Matrix();
      //  coverImage.setScaleType(ImageView.ScaleType.MATRIX);   //required
      //  matrix.postRotate( 180f, coverImage.getDrawable().getBounds().width()/2, coverImage.getDrawable().getBounds().height()/2);
      //  coverImage.setImageMatrix(matrix);
          coverImage.setRotation(provoteX+provoteY);
          provoteY=provoteY+provoteX;
          provoteX=90;
      }
    });
    //getting shared image
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    String getaction = intent.getAction();
    String type = intent.getType();


    mType = getIntent().getStringExtra(AppConstantsUtils.TYPE);

    list = new ArrayList<>();


    if(mType!=null) {
      switch (mType) {
        case AppConstantsUtils.PICTURE:
          list.addAll(getIntent().getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA));
          if (!list.isEmpty())
          {
            ChosenImage chosenImage = (ChosenImage) list.get(0);
            Picasso.with(this).load(chosenImage.getQueryUri()).into(coverImage);
          }
          break;
        case AppConstantsUtils.VIDEO:
          list.addAll(getIntent().getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA));
          ChosenVideo chosenVideo = (ChosenVideo) list.get(0);
          coverImage.setImageBitmap(createThumbnailFromPath(chosenVideo.getOriginalPath(),
                  MediaStore.Images.Thumbnails.MINI_KIND));
          break;
        case AppConstantsUtils.AUDIO:
          list.addAll(getIntent().getParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA));
          ChosenAudio chosenAudio = (ChosenAudio) list.get(0);
          coverImage.setImageResource(R.drawable.audiofileicom);
          break;
      }
    }



     else if (type.startsWith("image/")) {
      if (Intent.ACTION_SEND.equals(getaction)) {
        if (extras.containsKey(Intent.EXTRA_STREAM)) {
          imgUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
          try {
            originalPath=getFilePath(getApplicationContext(),imgUri);
          } catch (URISyntaxException e) {
            ToastUtils.showToast(getApplicationContext(),e.toString());
          }
         // ToastUtils.showToast(getApplicationContext(),String.valueOf(originalPath));
        }
      }

      Picasso.with(this).load(imgUri).into(coverImage);
    }


    else if (type.startsWith("video/")){
      if (Intent.ACTION_SEND.equals(getaction)) {
        if (extras.containsKey(Intent.EXTRA_STREAM)) {
          videoUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
          try {
            originalPath=getFilePath(getApplicationContext(),videoUri);
          } catch (URISyntaxException e) {
            ToastUtils.showToast(getApplicationContext(),e.toString());
          }
        //  ToastUtils.showToast(getApplicationContext(),String.valueOf(originalPath));
        }
      }
      Glide.with(getApplicationContext())
              .load(Uri.fromFile( new File(originalPath) ) )
              .into(coverImage);
    }



    else if (type.startsWith("audio/")){
      if (Intent.ACTION_SEND.equals(getaction)) {
        if (extras.containsKey(Intent.EXTRA_STREAM)) {
          audioUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
          try {
            originalPath=getFilePath(getApplicationContext(),audioUri);
          } catch (URISyntaxException e) {
            ToastUtils.showToast(getApplicationContext(),e.toString());
          }
        //  ToastUtils.showToast(getApplicationContext(),String.valueOf(originalPath));
        }
      }

      coverImage.setImageResource(R.drawable.audiofileicom);
    }


    captionMap = new HashMap<>();
    int size = list.size();
    for (int i = 0; i < size; i++)
    {
      captionMap.put(i, "");
    }
    MediaAdapter adapter = new MediaAdapter(list, this, mType);
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.selected_pics_rv);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    recyclerView.setAdapter(adapter);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        captionMap.put(lastPosition, BasicUtils.getTextFromEt(mCaptionEt));
        Intent intent = new Intent(MultiSelectionActivity.this, MediaUploadService.class);
        if(imgUri!=null)
        {
          intent.putExtra(AppConstantsUtils.USER_ID, Integer.valueOf(appUtils.getUserId()));
          intent.putExtra(AppConstantsUtils.SELECTED_MEDIA,imgUri);
          captions = new ArrayList<>(captionMap.values());
          intent.putStringArrayListExtra(AppConstantsUtils.CAPTION_LIST, captions);
          intent.putExtra(AppConstantsUtils.TYPE,AppConstantsUtils.SHARING_IMAGE_TYPE);
          intent.putExtra(AppConstantsUtils.SHARING_IMAGE_PATH,originalPath);
        }

        else if(videoUri!=null)
        {
          intent.putExtra(AppConstantsUtils.USER_ID, Integer.valueOf(appUtils.getUserId()));
          intent.putExtra(AppConstantsUtils.SELECTED_MEDIA,videoUri);
          captions = new ArrayList<>(captionMap.values());
          intent.putStringArrayListExtra(AppConstantsUtils.CAPTION_LIST, captions);
          intent.putExtra(AppConstantsUtils.TYPE,AppConstantsUtils.SHARING_VIDEO_TYPE);
          intent.putExtra(AppConstantsUtils.SHARING_VIDEO_PATH,originalPath);
        }


        else if(audioUri!=null)
        {
          intent.putExtra(AppConstantsUtils.USER_ID, Integer.valueOf(appUtils.getUserId()));
          intent.putExtra(AppConstantsUtils.SELECTED_MEDIA,audioUri);
          captions = new ArrayList<>(captionMap.values());
          intent.putStringArrayListExtra(AppConstantsUtils.CAPTION_LIST, captions);
          intent.putExtra(AppConstantsUtils.TYPE,AppConstantsUtils.SHARING_AUDIO_TYPE);
          intent.putExtra(AppConstantsUtils.SHARING_AUDIO_PATH,originalPath);
        }



        else
        {
          intent.putExtra(AppConstantsUtils.USER_ID, Integer.valueOf(appUtils.getUserId()));
          intent.putExtra(AppConstantsUtils.TYPE, mType);
          captions = new ArrayList<>(captionMap.values());
          intent.putStringArrayListExtra(AppConstantsUtils.CAPTION_LIST, captions);
          intent.putExtra(AppConstantsUtils.SELECTED_MEDIA, list);
        }
        startService(intent);
        ToastUtils.showToast(MultiSelectionActivity.this,"Uploading media...");
        finish();
      }
    });
    LogUtils.debugOut(list.toString());
  }







  public Bitmap createThumbnailFromPath(String filePath, int type) {
    return ThumbnailUtils.createVideoThumbnail(filePath, type);
  }

  @Override protected void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void onMediaClick(SaiEvent.AlbumClick onClick) {

    captionMap.put(lastPosition, BasicUtils.getTextFromEt(mCaptionEt));
    lastPosition = onClick.getPosition();
    if (captionMap.get(lastPosition) != null) {
      mCaptionEt.setText(captionMap.get(lastPosition));
    } else {
      mCaptionEt.setText("");
    }
    switch (mType) {
      case AppConstantsUtils.PICTURE:
        ChosenImage chosenImage = (ChosenImage) list.get(onClick.getPosition());
        Picasso.with(this).load(chosenImage.getQueryUri()).into(coverImage);
        break;
      case AppConstantsUtils.VIDEO:
        ChosenVideo chosenVideo = (ChosenVideo) list.get(onClick.getPosition());
        coverImage.setImageBitmap(createThumbnailFromPath(chosenVideo.getOriginalPath(),
            MediaStore.Images.Thumbnails.MINI_KIND));
        break;
    }
  }




  @SuppressLint("NewApi")
  public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
    String selection = null;
    String[] selectionArgs = null;
    // Uri is different in versions after KITKAT (Android 4.4), we need to
    if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        return Environment.getExternalStorageDirectory() + "/" + split[1];
      } else if (isDownloadsDocument(uri)) {
        final String id = DocumentsContract.getDocumentId(uri);
        uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
      } else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        if ("image".equals(type)) {
          uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        selection = "_id=?";
        selectionArgs = new String[]{
                split[1]
        };
      }
    }
    if ("content".equalsIgnoreCase(uri.getScheme())) {
      String[] projection = {
              MediaStore.Images.Media.DATA
      };
      Cursor cursor = null;
      try {
        cursor = context.getContentResolver()
                .query(uri, projection, selection, selectionArgs, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
          return cursor.getString(column_index);
        }
      } catch (Exception e) {
      }
    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }
    return null;
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }
}
