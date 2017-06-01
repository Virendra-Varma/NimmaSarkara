package com.cnbitstols.dcutilsarsenal.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by DC on 9/22/2016.
 * Get current location using google location api
 * If not using this class Then we can remove the 'com.google.android.gms:play-services-location:9.4.0'
 * from gradle file
 */
public class GetCurrentLocation
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  GoogleApiClient mGoogleApiClient;
  Location mLastLocation;
  private Context mContext;
  private OnLocationListener mOnLocationListener;

  public GetCurrentLocation(Context mContext) {
    this.mContext = mContext;
  }

  public void setOnLocationListener(OnLocationListener onLocationListener) {
    buildGoogleApiClient();

    if (mGoogleApiClient != null) {
      mGoogleApiClient.connect();
    } else {
      Toast.makeText(mContext, "Not connected...", Toast.LENGTH_SHORT).show();
    }

    this.mOnLocationListener = onLocationListener;
  }

  @Override public void onConnected(@Nullable Bundle bundle) {

    if (ActivityCompat.checkSelfPermission(mContext,
        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(mContext,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    if (mOnLocationListener != null) {
      mOnLocationListener.onLocationFetched(mLastLocation);
    }
  }

  @Override public void onConnectionSuspended(int i) {
    Toast.makeText(mContext, "Connection suspended...", Toast.LENGTH_SHORT).show();
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(mContext, "Failed to connect...", Toast.LENGTH_SHORT).show();
  }

  private void buildGoogleApiClient() {
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }
  }

  public interface OnLocationListener {

    void onLocationFetched(Location location);
  }
}
