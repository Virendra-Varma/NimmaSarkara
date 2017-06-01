package com.cnbit.nimmasarkara.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.cnbit.nimmasarkara.R;

/**
 * Created by Harry on 3/28/2017.
 */

public class FullscreenVideoplaying extends AppCompatActivity implements EasyVideoCallback {
    private EasyVideoPlayer mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenvidplay);
        mVideoView = (EasyVideoPlayer) findViewById(R.id.player);
        Intent intent=getIntent();
        String uri=intent.getStringExtra("Viduri");
       //ToastUtils.showToast(this,uri);
        mVideoView.setCallback(this);
        mVideoView.setSource(Uri.parse(uri));
        mVideoView.setAutoPlay(true);
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
