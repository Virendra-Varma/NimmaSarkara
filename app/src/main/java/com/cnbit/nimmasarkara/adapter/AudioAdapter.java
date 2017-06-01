package com.cnbit.nimmasarkara.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Harry on 12/27/2016.
 */

public class AudioAdapter extends android.widget.BaseAdapter
{

    Uri uri;
    private Dialog dialog;
    TextView cur_val;
    Activity act;
    private Boolean isButtonClicked=false;
    private LruCache<String, Bitmap> mMemoryCache;
    private Context mcontext;
    AppUtils appUtils;
    MediaPlayer mp=new MediaPlayer();
    ArrayList<HashMap<String, String>> listname;
    ProgressBar pb;
    int downloadedSize = 0;
    int totalSize = 0;
    String media,title;
    SeekBar seekbar;


    public AudioAdapter(Context context,Activity act, ArrayList<HashMap<String, String>> value)
    {
        mcontext=context;
        listname=value;
        this.act = act;

        // Memory Cache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        appUtils = new AppUtils(mcontext);

    }


    @Override
    public int getCount() {
        return listname.size();
    }

    @Override
    public Object getItem(int position) {
        return listname.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final ViewHolder holder;
        holder = new ViewHolder();
        convertView=View.inflate(mcontext, R.layout.audioreff,null);

        holder.title=(TextView)convertView.findViewById(R.id.audiotitle);
        holder.postedby=(TextView)convertView.findViewById(R.id.postedby);
        holder.postedon=(TextView)convertView.findViewById(R.id.date);
        holder.likes=(TextView)convertView.findViewById(R.id.likes);
       // holder.play=(Button)convertView.findViewById(R.id.butplay);
     //  holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);


        convertView.setTag(holder);


        HashMap<String, String> result=listname.get(position);

        final String titlee=result.get("title");
        String postedy=result.get("postedby");
        String postedon=result.get("datetime");
        String likes=result.get("likes");
        final String medi=result.get("media");



        holder.title.setText(titlee);
        holder.postedby.setText(postedy);
        holder.postedon.setText(postedon);
        holder.likes.setText(likes);


     /*   final View finalConvertView = convertView;
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekbar = holder.seekBar = (SeekBar) finalConvertView.findViewById(R.id.seekBar);
                isButtonClicked = !isButtonClicked; // toggle the boolean flag
                v.setBackgroundResource(isButtonClicked ? R.drawable.buttonplay : R.drawable.pausebutton);

                media = medi;
                title = titlee;
                Toast.makeText(mcontext, media + position, Toast.LENGTH_SHORT).show();
                Log.d("", AppConstantsUtils.BASE_URL + medi);
                String perfection = title.replaceAll("\"", "");
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Apna/" + perfection + ".mp3");
                if (file.exists()) {
                    try {
                        mp.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/Apna/" + perfection + ".mp3");//Write your location here
                        mp.prepareAsync();
                        //  mp.start();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();

                                mRunnable.run();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                else {

                    //downloadFile();
                     Down dw=new Down();
                           dw.execute();

                        }




            }
        });*/


        return convertView;
    }




    static class ViewHolder
    {
        TextView title,postedby,likes,postedon;
        Button play;
        SeekBar seekBar;
    }




    private class Down extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {

            downloadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }








    private void downloadFile(){

        try {
           // showProgress();
            URL url = new URL(AppConstantsUtils.BASE_URL+media);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();
            final String folder_main = "Apna";
            //set the path where we want to save the file
         /*   File SDCardRoot =new File(Environment.getExternalStorageDirectory(),folder_main);

            if (!SDCardRoot.exists()) {
                SDCardRoot.mkdirs();
            }

            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,title);*/



            File wallpaperDirectory = new     File(Environment.getExternalStorageDirectory(),folder_main);
            //have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
            //create a File object for the output file
            final String perfection=title.replaceAll("\"","");
            File outputFile = new File(wallpaperDirectory, perfection+".mp3");
            //now attach the OutputStream to the file object, instead of a String representation






            FileOutputStream fileOutput = new FileOutputStream(outputFile);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            act.runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            act.runOnUiThread(new Runnable() {
                public void run() {


                    dialog.dismiss();



                    try{
                        mp.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/Apna/"+perfection+".mp3");//Write your location here
                        mp.prepareAsync();
                        //  mp.start();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                mRunnable.run();
                            }
                        });


                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            Log.d("dfdsfsd", e.toString());
            e.printStackTrace();
        } catch (final IOException e) {
            Log.d("dfdsfsd", e.toString());
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            Log.d("dfdsfsd", e.toString());
            showError("Error : Please check your internet connection " + e);
        }
    }

    private void showError(final String err){
        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(mcontext, err, Toast.LENGTH_LONG).show();
                Log.d("dfdsfsd", err);
            }
        });
    }

    private void showProgress(){
        dialog = new Dialog(mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");


        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(mcontext.getResources().getDrawable(R.drawable.green_progress));
    }




    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if(mp != null) {
                mHandler.removeCallbacks(mRunnable);
                //set max value
                int mDuration = mp.getDuration();
                seekbar.setMax(0);
                seekbar.setMax(mDuration);

                //update total time text view

                //set progress to current position

                int mCurrentPosition = mp.getCurrentPosition();

                seekbar.setProgress(mCurrentPosition);
             //  ToastUtils.showToast(mcontext,String.valueOf(mCurrentPosition));

                //update current time text view

                //handle drag on seekbar
                seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mp != null && fromUser){
                           // ToastUtils.showToast(mcontext,String.valueOf(progress));
                            mp.seekTo(progress);
                        }
                    }
                });


            }

            //repeat above code every second
            mHandler.postDelayed(this, 10);
        }
    };
}