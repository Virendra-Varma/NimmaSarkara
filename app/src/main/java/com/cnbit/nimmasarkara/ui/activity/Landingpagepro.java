package com.cnbit.nimmasarkara.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.LogUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.util.ArrayList;
import java.util.List;

import it.chengdazhi.decentbanner.DecentBanner;

/**
 * Created by Harry on 2/16/2017.
 */

public class Landingpagepro extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ImagePickerCallback, VideoPickerCallback, AudioPickerCallback {
    ImageView images, video, audio, profile;
    Animation show_fab_1;
    Animation hide_fab_1;
    private CameraVideoPicker cameraVideoPicker;
    private CameraImagePicker cameraPicker;
    Animation show_fab_2;
    private boolean isToTake;
    private NavigationView mNavigationView;
    Animation hide_fab_2;
    Animation main_fab_animation_show;
    Animation main_fab_animation_hide;
    private FloatingActionButton mFab1;
    private FloatingActionButton mFab2;
    private FloatingActionButton mMainFab;
    private boolean FAB_Status = false;
    private String pickerPath;


    private ImagePicker imagePicker;
    private VideoPicker videoPicker;
    private AudioPicker audioPicker;
    private DecentBanner decentBanner;
    private List<View> views;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page_two);
       // ticker = (TextView)findViewById(R.id.scrollingTicker);
      //  ticker.setSelected(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        decentBanner = (DecentBanner) findViewById(R.id.decent_banner);
        View view1 = getLayoutInflater().inflate(R.layout.popular_layout, null);
        View view2 = getLayoutInflater().inflate(R.layout.daily_layout, null);
        View view3 = getLayoutInflater().inflate(R.layout.recommend_layout, null);

        views = new ArrayList<>();
        views.add(view3);
        views.add(view2);
        views.add(view1);

        titles = new ArrayList<>();
        titles.add("AUDIOS");
        titles.add("VIDEOS");
        titles.add("IMAGE");
        decentBanner.start(views, titles, 3, 500, R.drawable.textlogo);










        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View drawerHeader = mNavigationView.getHeaderView(0);
        TextView name = (TextView) drawerHeader.findViewById(R.id.username);
        TextView mobileNo = (TextView) drawerHeader.findViewById(R.id.mobile_no);


        AppUtils appUtils = new AppUtils(this);
        OtpResponse response = appUtils.getObject("OTP_RESPONSE", OtpResponse.class);
        if (response != null) {
            name.setText(response.getName());
            mobileNo.setText(response.getMobile());
        }

        mMainFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        mFab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        main_fab_animation_show =
                AnimationUtils.loadAnimation(getApplication(), R.anim.main_fab_animation_show);
        main_fab_animation_hide =
                AnimationUtils.loadAnimation(getApplication(), R.anim.main_fab_animation_hide);

        mMainFab.setOnClickListener(this);
        mFab1.setOnClickListener(this);
        mFab2.setOnClickListener(this);


        images = (ImageView) findViewById(R.id.images);
        video = (ImageView) findViewById(R.id.video);
        audio = (ImageView) findViewById(R.id.audio);
        profile = (ImageView) findViewById(R.id.profile);
      //  home = (ImageView) findViewById(R.id.myhome);


        images.setOnClickListener(this);
        video.setOnClickListener(this);
        audio.setOnClickListener(this);
        profile.setOnClickListener(this);
       // home.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

       //disable "hamburger to arrow" drawable
        /*toggle.setHomeAsUpIndicator(R.mipmap.hamburger);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });*/
      //  drawer.setDrawerListener(toggle);
      //  toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {

        if (v == images) {
            Intent image = new Intent(getApplicationContext(), LandingPageActivity.class);
            image.putExtra(AppConstantsUtils.PICTURE, "1");
            startActivity(image);
        }
        if (v == video) {
            Intent image = new Intent(getApplicationContext(), LandingPageActivity.class);
            image.putExtra(AppConstantsUtils.PICTURE, "2");
            startActivity(image);
        }
        if (v == audio) {
            Intent image = new Intent(getApplicationContext(), LandingPageActivity.class);
            image.putExtra(AppConstantsUtils.PICTURE, "3");
            startActivity(image);
        }
        if (v == profile) {
            startActivity(new Intent(getApplicationContext(), ViewProfile.class));
        }
     //   if (v == home) {
     //       startActivity(new Intent(getApplicationContext(), MyHome.class));
     //   }

        switch (v.getId()) {
            case R.id.fab:
                if (!FAB_Status) {
                    //Display FAB menu
                    mMainFab.startAnimation(main_fab_animation_show);
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    mMainFab.startAnimation(main_fab_animation_hide);
                    hideFAB();
                    FAB_Status = false;
                }
                break;
            case R.id.fab_1:
                isToTake = false;
                mMainFab.performClick();
                showAlertDialogForCamera();
                break;
            case R.id.fab_2:
                isToTake = true;
                mMainFab.performClick();
                showAlertDialogForCamera();
                break;
        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Choose From");
        alertDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isToTake = true;
                showAlertDialogForCamera();
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isToTake = false;
                showAlertDialogForCamera();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showAlertDialogForCamera() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Choose");
        alertDialogBuilder.setPositiveButton("Video", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isToTake) {
                    takeVideo();
                } else {
                    pickVideo();
                }
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isToTake) {
                    takePicture();
                } else {
                    pickPicture();
                }
                dialog.cancel();
            }
        });


        alertDialogBuilder.setNeutralButton("Audio", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isToTake) {
                    pickAudio();
                } else {
                    pickAudio();
                }
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();

        //show it
        alertDialog.show();
    }

    private void hideFAB() {

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab1.getLayoutParams();
        lp.leftMargin -= (int) (mFab1.getWidth() * 1.00);
        lp.bottomMargin -= (int) (mFab1.getHeight() * 1.25);
        mFab1.setLayoutParams(lp);
        mFab1.startAnimation(hide_fab_1);
        mFab1.setClickable(false);


        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) mFab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (mFab2.getWidth() * 1.00);
        layoutParams2.bottomMargin -= (int) (mFab2.getHeight() * 1.25);
        mFab2.setLayoutParams(layoutParams2);
        mFab2.startAnimation(hide_fab_2);
        mFab2.setClickable(false);
    }

    private void expandFAB() {
        //Floating Action Button 1
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab1.getLayoutParams();
        lp.leftMargin += (mFab1.getWidth() * 1.00);
        lp.bottomMargin += (int) (mFab1.getHeight() * 1.25);
        mFab1.setLayoutParams(lp);
        mFab1.startAnimation(show_fab_1);
        mFab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) mFab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (mFab2.getWidth() * 1.00);
        layoutParams2.bottomMargin += (int) (mFab2.getHeight() * 1.25);
        mFab2.setLayoutParams(layoutParams2);
        mFab2.startAnimation(show_fab_2);
        mFab2.setClickable(true);
    }


    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setImagePickerCallback(this);
        //cameraPicker.shouldGenerateMetadata(true);
        //cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.ensureMaxSize(800, 800);
        pickerPath = cameraPicker.pickImage();
    }


    private void pickPicture() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(this);
        imagePicker.ensureMaxSize(800, 800);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private void pickAudio() {
        audioPicker = new AudioPicker(this);
        audioPicker.setAudioPickerCallback(this);
        audioPicker.allowMultiple();
        audioPicker.pickAudio();
    }


    private void takeVideo() {
        cameraVideoPicker = new CameraVideoPicker(this, pickerPath);
        cameraVideoPicker.setVideoPickerCallback(this);
        cameraVideoPicker.shouldGenerateMetadata(true);
        cameraVideoPicker.shouldGeneratePreviewImages(true);
        //cameraPicker.setCacheLocation(CacheLocation.INTERNAL_APP_DIR);
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // Set the duration of the video
        extras.putInt(MediaStore.EXTRA_DURATION_LIMIT, 5);
        pickerPath = cameraVideoPicker.pickVideo();
    }

    private void pickVideo() {
        videoPicker = new VideoPicker(this);
        videoPicker.setVideoPickerCallback(this);
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.allowMultiple();
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.pickVideo();
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        LogUtils.debugOut(list.toString());
        if (!list.isEmpty()) {
            Intent intent = new Intent(this, MultiSelectionActivity.class);
            ArrayList<ChosenImage> arrayList = new ArrayList<>(list);
            intent.putParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA, arrayList);
            intent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.PICTURE);
            startActivity(intent);
        }
    }

    @Override
    public void onError(String s) {
        ToastUtils.showToast(this, s);
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> list) {
        LogUtils.debugOut(list.toString());
        if (!list.isEmpty()) {
            Intent intent = new Intent(this, MultiSelectionActivity.class);
            ArrayList<ChosenVideo> arrayList = new ArrayList<>(list);
            intent.putParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA, arrayList);
            intent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.VIDEO);
            startActivity(intent);
        }
    }


    @Override
    public void onAudiosChosen(List<ChosenAudio> list) {
        if (!list.isEmpty()) {
            Intent intent = new Intent(this, MultiSelectionActivity.class);
            ArrayList<ChosenAudio> arrayList = new ArrayList<>(list);
            intent.putParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA, arrayList);
            intent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.AUDIO);
            startActivity(intent);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Picker.PICK_IMAGE_CAMERA:
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
                break;
            case Picker.PICK_VIDEO_CAMERA:
                if (cameraVideoPicker == null) {
                    cameraVideoPicker = new CameraVideoPicker(this, pickerPath);
                }
                cameraVideoPicker.submit(data);
                break;
            case Picker.PICK_IMAGE_DEVICE:
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
                break;
            case Picker.PICK_VIDEO_DEVICE:
                if (videoPicker == null) {
                    videoPicker = new VideoPicker(this);
                    videoPicker.setVideoPickerCallback(this);
                }
                videoPicker.submit(data);
                break;

            case Picker.PICK_AUDIO:
                if (audioPicker == null) {
                    audioPicker = new AudioPicker(this);
                    audioPicker.setAudioPickerCallback(this);
                }
                audioPicker.submit(data);
                break;
        }
        if (requestCode == Picker.PICK_IMAGE_CAMERA) {
            if (cameraPicker == null) {
                cameraPicker = new CameraImagePicker(this);
                cameraPicker.setImagePickerCallback(this);
                cameraPicker.reinitialize(pickerPath);
            }
            cameraPicker.submit(data);
        }
    }
}
