package com.cnbit.nimmasarkara.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.ui.fragment.DashBoardFragment;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.utils.LogUtils;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
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
import com.squareup.otto.Subscribe;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;

public class LandingPageActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, ImagePickerCallback,
    VideoPickerCallback, View.OnClickListener, AudioPickerCallback {
  public static final String TAG = LandingPageActivity.class.getSimpleName();
  public static final String MULTIPART_FORM_DATA = "multipart/form-data";
  final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 110;
  Animation show_fab_1;
  Animation hide_fab_1;
  Animation show_fab_2;
  Animation hide_fab_2;
  Animation main_fab_animation_show;
  Animation main_fab_animation_hide;
//  private DrawerLayout mDrawerLayout;
 // private NavigationView mNavigationView;
  private String pickerPath;
  private CameraImagePicker cameraPicker;
  private ImagePicker imagePicker;
  private VideoPicker videoPicker;
  private AudioPicker audioPicker;
  private CameraVideoPicker cameraVideoPicker;
  private boolean isToTake;
  private ProgressDialog mProgressDialog;
  private String titleStr;
  private String descStr;
  private String mCurrentMediaUploadType;
  private SearchView mSearchView;
  private FloatingActionButton mFab1;
  private FloatingActionButton mFab2;
  private FloatingActionButton mMainFab;
  private boolean FAB_Status = false;
  String type;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_landing_page);

    Intent in=getIntent();
    type=in.getStringExtra(AppConstantsUtils.PICTURE);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    toolbar.setTitle(getString(R.string.app_name));
    toolbar.setNavigationIcon(R.mipmap.backwhite);
    setSupportActionBar(toolbar);
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(type.equals("2")) {
       //   onBackPressed();
          startActivity(new Intent(getApplicationContext(),Landingpagepro.class));
          finish();
        }
        else{
          onBackPressed();
        }
      }
    });

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
    /*fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showDialog();
      }
    });*/
    mMainFab.setOnClickListener(this);
    mFab1.setOnClickListener(this);
    mFab2.setOnClickListener(this);



  /*  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    toggle.setDrawerIndicatorEnabled(false);
    mDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();
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
    }*/

    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCanceledOnTouchOutside(false);
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setMessage("Uploading...");
    replaceFragment(DashBoardFragment.getInstance(), false);
    insertPermission();
  }

  private void showDialog() {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    View view = LayoutInflater.from(this).inflate(R.layout.dialog_media_details, null);
    alertDialogBuilder.setView(view);
    final EditText titleEt = (EditText) view.findViewById(R.id.title_et);
    final EditText descEt = (EditText) view.findViewById(R.id.description_et);
    Button selectMediaBtn = (Button) view.findViewById(R.id.select_media);
    final AlertDialog dialog = alertDialogBuilder.create();
    selectMediaBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        titleStr = BasicUtils.getTextFromEt(titleEt);
        descStr = BasicUtils.getTextFromEt(descEt);
        if (!titleStr.isEmpty()) {
          showAlertDialog();
          dialog.cancel();
        } else {
          ToastUtils.showToast(LandingPageActivity.this, "Title required");
        }
      }
    });
    dialog.show();
  }

  private void showAlertDialog() {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    alertDialogBuilder.setTitle("Choose From");
    alertDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        isToTake = true;
        showAlertDialogForCamera();
        dialog.cancel();
      }
    });
    alertDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
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
      @Override public void onClick(DialogInterface dialog, int which) {
        if (isToTake) {
          takeVideo();
        } else {
          pickVideo();
        }
        dialog.cancel();
      }
    });
    alertDialogBuilder.setNegativeButton("Picture", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        if (isToTake) {
          takePicture();
        } else {
          pickPicture();
        }
        dialog.cancel();
      }
    });


    alertDialogBuilder.setNeutralButton("Audio", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
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

 /* @Override public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
*/
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.landing_page, menu);
    mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()) {
          Intent i = new Intent(LandingPageActivity.this, SearchActivity.class);
          i.putExtra(AppConstantsUtils.KEYWORD, query);
          startActivity(i);
        }
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.

    if (item.getItemId() == R.id.nav_share) {
      shareApp();
    } else if (item.getItemId() == R.id.nav_your_photos) {
      Intent photoIntent = new Intent(this, SearchActivity.class);
      photoIntent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.PICTURE);
      startActivity(photoIntent);
    } else if (item.getItemId() == R.id.nav_your_videos) {
      Intent photoIntent = new Intent(this, SearchActivity.class);
      photoIntent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.VIDEO);
      startActivity(photoIntent);
    }

//    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private void shareApp() {
    int applicationNameId = getApplicationInfo().labelRes;
    final String appPackageName = getPackageName();
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("text/plain");
    i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
    String text = "Install this cool application: ";
    String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
    i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
    startActivity(Intent.createChooser(i, "Share link:"));
  }

  @Override protected void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void onMediaUploaded(SaiEvent.MediaUploaded uploaded) {
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }
    if (mCurrentMediaUploadType != null && mCurrentMediaUploadType.equalsIgnoreCase(
        AppConstantsUtils.PICTURE)) {
      BusProvider.getInstance().post(new SaiEvent.RefreshPhotoList());
    } else if (mCurrentMediaUploadType != null && mCurrentMediaUploadType.equalsIgnoreCase(
        AppConstantsUtils.PICTURE)) {
      BusProvider.getInstance().post(new SaiEvent.RefreshVideoList());
    }
    ToastUtils.showToast(this, uploaded.getResponse().getMsg());
  }

  @Subscribe public void onMediaNotUploaded(SaiEvent.MediaNotUploaded notUploaded) {
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }
    if (notUploaded.getReason() != null) {
      ToastUtils.showToast(this, notUploaded.getReason());
    } else {
      ToastUtils.showToast(this, notUploaded.getResponse().getMsg());
    }
  }

  public void replaceFragment(Fragment fragment, boolean addToBackStack) {
    String backStateName = fragment.getClass().getName();
    Bundle bundle = new Bundle();
    bundle.putString(AppConstantsUtils.TYPE, type);
    fragment.setArguments(bundle);
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.content_landing_page, fragment);

    if (addToBackStack) mFragmentTransaction.addToBackStack(backStateName);
    mFragmentTransaction.commitAllowingStateLoss();
  }

  @Override public void onImagesChosen(List<ChosenImage> list) {
    LogUtils.debugOut(list.toString());
    if (!list.isEmpty()) {
      Intent intent = new Intent(this, MultiSelectionActivity.class);
      ArrayList<ChosenImage> arrayList = new ArrayList<>(list);
      intent.putParcelableArrayListExtra(AppConstantsUtils.SELECTED_MEDIA, arrayList);
      intent.putExtra(AppConstantsUtils.TYPE, AppConstantsUtils.PICTURE);
      startActivity(intent);
    }
  }

  @Override public void onError(String s) {
    ToastUtils.showToast(this, s);
  }

  @Override public void onVideosChosen(List<ChosenVideo> list) {
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


  @NonNull private MultipartBody.Part prepareFilePart(String partName, String path) {
    // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
    // use the FileUtils to get the actual file by uri

    File file = new File(path);
    // create RequestBody instance from file
    okhttp3.RequestBody requestFile =
        okhttp3.RequestBody.create(okhttp3.MediaType.parse(MULTIPART_FORM_DATA), file);

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
  }

  private void initiateServiceCall(ChosenImage chosenImage) {
    if (NetworkUtils.haveNetworkConnection(this)) {
      AppUtils appUtils = new AppUtils(this);
      RestDataService service = new RestDataService();
      service.uploadPicture(Integer.valueOf(appUtils.getUserId()), titleStr, descStr,
          prepareFilePart("picture", chosenImage.getOriginalPath()));
      mCurrentMediaUploadType = AppConstantsUtils.PICTURE;
      mProgressDialog.show();
    } else {
      ToastUtils.showToast(getApplicationContext(),"No Internet");
    }
  }

  /* permission methods */

  private void initiateVideoUploadServiceCall(ChosenVideo chosenVideo) {
    if (NetworkUtils.haveNetworkConnection(this)) {
      AppUtils appUtils = new AppUtils(this);
      RestDataService service = new RestDataService();
      service.uploadVideo(Integer.valueOf(appUtils.getUserId()), titleStr, descStr,
          prepareFilePart("video", chosenVideo.getOriginalPath()));
      mCurrentMediaUploadType = AppConstantsUtils.VIDEO;
      mProgressDialog.show();
    } else {
      ToastUtils.showToast(getApplicationContext(),"No Internet");
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
        Map<String, Integer> perms = new HashMap<>();
        // Initial
        perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
        // Fill with results
        for (int i = 0; i < permissions.length; i++)
          perms.put(permissions[i], grantResults[i]);
        // Check for ACCESS_FINE_LOCATION
        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
          // All Permissions Granted
          insertPermission();
        } else {
          // Permission Denied
          Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
        }
      }
      break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void insertPermission() {
    if (Build.VERSION.SDK_INT >= 23) {
      List<String> permissionsNeeded = new ArrayList<>();

      final List<String> permissionsList = new ArrayList<>();
      if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        permissionsNeeded.add("Storage");
      }
      if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
        permissionsNeeded.add("Camera");
      }

      if (permissionsList.size() > 0) {
        if (permissionsNeeded.size() > 0) {
          // Need Rationale
          String message = "You need to grant access to " + permissionsNeeded.get(0);
          for (int i = 1; i < permissionsNeeded.size(); i++)
            message = message + ", " + permissionsNeeded.get(i);
          showMessageOKCancel(message, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
              }
            }
          });
          return;
        }
        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        return;
      }
    }
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(this).setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }

  private boolean addPermission(List<String> permissionsList, String permission) {
    if (Build.VERSION.SDK_INT >= 23) {
      if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        permissionsList.add(permission);
        // Check for Rationale Option
        if (!shouldShowRequestPermissionRationale(permission)) return false;
      }
      return true;
    }
    return true;
  }

  @Override public void onClick(View v) {
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

  private void hideFAB() {
    //Floating Action Button 1
    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab1.getLayoutParams();
    lp.leftMargin -= (int) (mFab1.getWidth() * 1.00);
    lp.bottomMargin -= (int) (mFab1.getHeight() * 1.25);
    mFab1.setLayoutParams(lp);
    mFab1.startAnimation(hide_fab_1);
    mFab1.setClickable(false);

    //Floating Action Button 2
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

  }
