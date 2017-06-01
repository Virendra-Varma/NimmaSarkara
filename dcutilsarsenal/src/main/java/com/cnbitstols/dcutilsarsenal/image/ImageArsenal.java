package com.cnbitstols.dcutilsarsenal.image;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;

/**
 * Created by DC on 10/21/2016.
 * Basic Image Related methods
 */

public class ImageArsenal {

  /**
   * Method for return file path of Gallery image
   *
   * @param context context from which the method is called
   * @param uri uri of the image file
   * @return path of the selected image file from gallery
   */
  @TargetApi(19) public static String getPath(final Context context, final Uri uri) {

    //check here to KITKAT or new version
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri =
            ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                Long.valueOf(id));

        return getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] {
            split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

      // Return the remote address
      if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();

      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }

  /**
   * Get the value of the data column for this Uri. This is useful for
   * MediaStore Uris, and other file-based ContentProviders.
   *
   * @param context The context.
   * @param uri The Uri to query.
   * @param selection (Optional) Filter used in the query.
   * @param selectionArgs (Optional) Selection arguments used in the query.
   * @return The value of the _data column, which is typically a file path.
   */
  public static String getDataColumn(Context context, Uri uri, String selection,
      String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {
        column
    };

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null) cursor.close();
    }
    return null;
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is ExternalStorageProvider.
   */
  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is Google Photos.
   */
  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }

  public static void getBitmapImage(String path, Context context,
      @NonNull BitmapFetchedListener listener) {
    new BitmapFetchTask(path, context, listener).execute();
  }

  public static void getBitmapImage(Uri uri, Context context,
      @NonNull BitmapFetchedListener listener) {
    new BitmapFetchTask(uri, context, listener).execute();
  }

  public static void getBitmapImage(File file, Context context,
      @NonNull BitmapFetchedListener listener) {
    new BitmapFetchTask(file, context, listener).execute();
  }

  public static void getBitmapImage(int drawable, Context context,
      @NonNull BitmapFetchedListener listener) {
    new BitmapFetchTask(drawable, context, listener).execute();
  }

  public interface BitmapFetchedListener {
    void onBitmapFetched(Bitmap bitmap);
  }

  private static class BitmapFetchTask extends AsyncTask<Void, Void, Bitmap> {
    private String path;
    private Uri uri;
    private File file;
    private int drawable;
    private BitmapFetchedListener listener;
    private Context context;

    public BitmapFetchTask(int drawable, Context context, BitmapFetchedListener listener) {
      this.drawable = drawable;
      this.context = context;
      this.listener = listener;
    }

    public BitmapFetchTask(File file, Context context, BitmapFetchedListener listener) {
      this.context = context;
      this.file = file;
      this.listener = listener;
    }

    public BitmapFetchTask(Uri uri, Context context, BitmapFetchedListener listener) {
      this.context = context;
      this.uri = uri;
      this.listener = listener;
    }

    public BitmapFetchTask(String path, Context context, BitmapFetchedListener listener) {
      this.context = context;
      this.path = path;
      this.listener = listener;
    }

    @Override protected Bitmap doInBackground(Void... params) {
      try {
        if (path != null) {
          return Picasso.with(context).load(path).get();
        } else if (uri != null) {
          return Picasso.with(context).load(uri).get();
        } else if (file != null) {
          return Picasso.with(context).load(file).get();
        } else if (drawable != 0) {
          return Picasso.with(context).load(drawable).get();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
        return null;
      }
      return null;
    }

    @Override protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      if (listener != null) {
        listener.onBitmapFetched(bitmap);
      }
    }
  }
}
