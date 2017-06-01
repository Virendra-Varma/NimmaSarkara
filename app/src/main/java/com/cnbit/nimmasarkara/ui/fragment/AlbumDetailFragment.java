package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.ImageVpAdapter;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailFragment extends Fragment {
  private View mRootView;
  private ViewPager mAlbumSliderVp;
  private List<Object> mList = new ArrayList<>();
  private ImageVpAdapter adapter;
  private int currentPosition;

  public AlbumDetailFragment() {
    // Required empty public constructor
  }

  public static AlbumDetailFragment getInstance(int currentPosition,
      List<MediaResponse> mediaResponses) {
    AlbumDetailFragment fragment = new AlbumDetailFragment();
    fragment.currentPosition = currentPosition;
    fragment.mList.addAll(mediaResponses);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mRootView == null) {
      mRootView = inflater.inflate(R.layout.fragment_album_detail, container, false);
      setupUi(mRootView);
    }
    return mRootView;
  }

  private void setupUi(View view) {
    mAlbumSliderVp = (ViewPager) view.findViewById(R.id.album_photo_vp);
    adapter = new ImageVpAdapter(mList, ImageVpAdapter.URL);
    mAlbumSliderVp.setAdapter(adapter);
    mAlbumSliderVp.setCurrentItem(currentPosition);
  }

  @Override public void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override public void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }
}
