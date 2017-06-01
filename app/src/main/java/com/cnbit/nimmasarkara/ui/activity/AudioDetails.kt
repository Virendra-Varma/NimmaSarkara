package com.cnbit.nimmasarkara.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import com.cnbit.nimmasarkara.R
import com.cnbit.nimmasarkara.utils.AppConstantsUtils
import com.cnbit.nimmasarkara.utils.AppUtils
import com.cnbit.nimmasarkara.utils.ToastUtils

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

/**
 * Created by Harry on 12/30/2016.
 */

class AudioDetails : AppCompatActivity(), View.OnClickListener {
    private val playbackPosition = 0
    internal var media: String = ""
    internal var appUtils: AppUtils = AppUtils(this)
    internal var title: String = ""
    internal var downloadedSize = 0
    internal var totalSize = 0
    internal var likes: String = ""
    internal var postidid: String = ""
    internal var audiosample = AppConstantsUtils.BASE_URL + media
    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null
    internal var dialog: Dialog = null!!
    internal var pb: ProgressBar
    internal var like: TextView
    internal var cur_val: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_detail)
        val back = findViewById(R.id.backbutton) as ImageView
        back.setOnClickListener { onBackPressed() }

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        val intent = intent
        postidid = intent.getStringExtra(TAG_ID)
        title = intent.getStringExtra(TAG_TITLE)
        val description = intent.getStringExtra(TAG_DESC)
        likes = intent.getStringExtra(TAG_LIKES)
        val postedby = intent.getStringExtra(TAG_POSTEDBY)
        val datetime = intent.getStringExtra(TAG_DATETIME)
        media = intent.getStringExtra(TAG_MEDIA)
        mediaPlayer = MediaPlayer()

        val userid = appUtils.userId

        like = findViewById(R.id.likerr) as TextView
        like.setOnClickListener(this)
        like.text = likes
        (findViewById(R.id.now_playing_text) as TextView).text = title
        seekBar = findViewById(R.id.seekBar) as SeekBar


        val perfection = title.replace("\"".toRegex(), "")
        val file = File(Environment.getExternalStorageDirectory().path + "/Apna/" + perfection + ".mp3")

        if (file.exists()) {
            try {
                mediaPlayer!!.setDataSource(Environment.getExternalStorageDirectory().path + "/Apna/" + perfection + ".mp3")
                mediaPlayer!!.prepareAsync()

                mediaPlayer!!.setOnPreparedListener { mp ->
                    mp.start()
                    mRunnable.run()
                }

            } catch (e: IOException) {
                val a = this
                a.finish()
                Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show()
            }

        } else {
            val dw = Down()
            dw.execute()
        }


    }

    private val mHandler = Handler()
    private val mRunnable = object : Runnable {

        override fun run() {
            if (mediaPlayer != null) {

                //set max value
                val mDuration = mediaPlayer!!.duration

                seekBar!!.max = mDuration

                //update total time text view
                val totalTime = findViewById(R.id.totalTime) as TextView
                totalTime.text = getTimeString(mDuration.toLong())

                //set progress to current position
                val mCurrentPosition = mediaPlayer!!.currentPosition
                seekBar!!.progress = mCurrentPosition

                //update current time text view
                val currentTime = findViewById(R.id.currentTime) as TextView
                currentTime.text = getTimeString(mCurrentPosition.toLong())

                //handle drag on seekbar
                seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer!!.seekTo(progress)
                        }
                    }
                })


            }

            //repeat above code every second
            mHandler.postDelayed(this, 10)
        }
    }


    private inner class Down : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            showProgress()
        }

        override fun doInBackground(vararg params: Void): Void? {

            downloadFile()
            return null
        }

        override fun onPostExecute(aVoid: Void) {
            super.onPostExecute(aVoid)
        }
    }


    private fun showProgress() {
        dialog = Dialog(this@AudioDetails)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.myprogressdialog)
        dialog.setTitle("Download Progress")


        cur_val = dialog.findViewById(R.id.cur_pg_tv) as TextView
        cur_val.text = "Starting download..."
        dialog.show()

        pb = dialog.findViewById(R.id.progress_bar) as ProgressBar
        pb.progress = 0
        pb.progressDrawable = applicationContext.resources.getDrawable(R.drawable.green_progress)
    }

    private fun downloadFile() {

        try {
            // showProgress();
            val url = URL(AppConstantsUtils.BASE_URL + media)
            val urlConnection = url.openConnection() as HttpURLConnection

            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true

            //connect
            urlConnection.connect()
            val folder_main = "Apna"
            //set the path where we want to save the file
            /*   File SDCardRoot =new File(Environment.getExternalStorageDirectory(),folder_main);

            if (!SDCardRoot.exists()) {
                SDCardRoot.mkdirs();
            }

            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,title);*/


            val wallpaperDirectory = File(Environment.getExternalStorageDirectory(), folder_main)
            //have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs()
            //create a File object for the output file
            val perfection = title.replace("\"".toRegex(), "")
            val outputFile = File(wallpaperDirectory, perfection + ".mp3")
            //now attach the OutputStream to the file object, instead of a String representation

            val fileOutput = FileOutputStream(outputFile)

            //Stream used for reading the data from the internet
            val inputStream = urlConnection.inputStream

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.contentLength

            runOnUiThread {
                pb.max = totalSize
            }

            //create a buffer...
            val buffer = ByteArray(1024)
            var bufferLength = inputStream.read(buffer)

            while ((bufferLength) > 0) {
                fileOutput.write(buffer, 0, bufferLength)
                downloadedSize += bufferLength
                // update the progressbar //
                runOnUiThread {
                    pb.progress = downloadedSize
                    val per = downloadedSize.toFloat() / totalSize * 100
                    cur_val.text = "Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + per.toInt() + "%)"
                }
                bufferLength = inputStream.read(buffer)
            }
            //close the output stream when complete //
            fileOutput.close()
            runOnUiThread {
                dialog.dismiss()



                try {
                    mediaPlayer!!.setDataSource(Environment.getExternalStorageDirectory().path + "/Apna/" + perfection + ".mp3")//Write your location here
                    mediaPlayer!!.prepareAsync()
                    //  mp.start();
                    mediaPlayer!!.setOnPreparedListener { mp ->
                        mp.start()
                        mRunnable.run()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } catch (e: MalformedURLException) {
            showError("Error : MalformedURLException ")
            Log.d("dfdsfsd", e.toString())
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d("dfdsfsd", e.toString())
            showError("File Not Found on Server")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.d("dfdsfsd", e.toString())
            showError("Error : Please check your internet connection ")
        }

    }

    private fun showError(err: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, err, Toast.LENGTH_LONG).show()
            Log.d("dfdsfsd", err)
        }
    }


    fun play(view: View) {

        mediaPlayer!!.start()
    }


    fun pause(view: View) {

        mediaPlayer!!.pause()

    }

    fun stop(view: View) {

        mediaPlayer!!.seekTo(0)
        mediaPlayer!!.pause()

    }


    fun seekForward(view: View) {

        //set seek time
        val seekForwardTime = 5000

        // get current song position
        val currentPosition = mediaPlayer!!.currentPosition
        // check if seekForward time is lesser than song duration
        if (currentPosition + seekForwardTime <= mediaPlayer!!.duration) {
            // forward song
            mediaPlayer!!.seekTo(currentPosition + seekForwardTime)
        } else {
            // forward to end position
            mediaPlayer!!.seekTo(mediaPlayer!!.duration)
        }

    }

    fun seekBackward(view: View) {

        //set seek time
        val seekBackwardTime = 5000

        // get current song position
        val currentPosition = mediaPlayer!!.currentPosition
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer!!.seekTo(currentPosition - seekBackwardTime)
        } else {
            // backward to starting position
            mediaPlayer!!.seekTo(0)
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        finish()
    }

    private fun getTimeString(millis: Long): String {
        val buf = StringBuffer()

        val hours = millis / (1000 * 60 * 60)
        val minutes = millis % (1000 * 60 * 60) / (1000 * 60)
        val seconds = millis % (1000 * 60 * 60) % (1000 * 60) / 1000

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds))

        return buf.toString()
    }

    override fun onClick(v: View) {
        val likecount = Integer.parseInt(likes)
        val count = likecount + 1
        val likefinal = count.toString()
        like.text = likefinal

        val pl = Postlikesasync()
        pl.execute()

    }

    internal inner class Postlikesasync : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): String {

            val nameValuePairs = ArrayList<NameValuePair>()
            nameValuePairs.add(BasicNameValuePair("user_id", appUtils.userId))
            nameValuePairs.add(BasicNameValuePair("post_id", postidid))
            try {
                val client = DefaultHttpClient()
                val post = HttpPost(AppConstantsUtils.BASE_URL + "postLike")
                post.entity = UrlEncodedFormEntity(nameValuePairs)
                val response = client.execute(post)
                val code = response.statusLine.statusCode
                val recode = code.toString()
                val entity = response.entity
                val `is`: InputStream
                `is` = entity.content
                val bufferedReader = BufferedReader(InputStreamReader(`is`))
                val Json_Out = bufferedReader.readLine()
                try {
                    val jsonObject = JSONObject(Json_Out)
                    val status = jsonObject.getString("status")
                    val msg = jsonObject.getString("msg")
                    return status
                } catch (e: JSONException) {
                    return e.toString()
                }

            } catch (e: UnsupportedEncodingException) {
                return e.toString()
            } catch (e: ClientProtocolException) {
                return e.toString()
            } catch (e: IOException) {
                return e.toString()
            }

        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            if (s == "200") {
                ToastUtils.showToast(applicationContext, s)
            } else {
                ToastUtils.showToast(applicationContext, s)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private val TAG_ID = "id"
        private val TAG_TITLE = "title"
        private val TAG_DESC = "description"
        private val TAG_LIKES = "likes"
        private val TAG_POSTEDBY = "postedby"
        private val TAG_DATETIME = "datetime"
        private val TAG_MEDIA = "media"
    }
}
