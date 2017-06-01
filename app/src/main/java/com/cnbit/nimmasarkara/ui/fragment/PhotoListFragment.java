package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.PhotoAdapter;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.squareup.otto.Subscribe;

/**
 * Created by DC on 10/10/2016.
 */

public class PhotoListFragment extends MediaBaseFragment {
  private PhotoAdapter mAdapter;

  public static PhotoListFragment getInstance() {
    PhotoListFragment fragment = new PhotoListFragment();
    Bundle bundle = new Bundle();
    bundle.putString(AppConstantsUtils.TYPE, AppConstantsUtils.PICTURE);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mType = AppConstantsUtils.PICTURE;
  }

  @Override public void onResume() {
    super.onResume();
    AppUtils appUtils = new AppUtils(getActivity());
    if (appUtils.isLikedPic()) {
      appUtils.setLikedPic(false);
      isRefreshed = true;
      initiateServiceCall();
    }
  }

  @Override RecyclerView.Adapter createAdapter() {
    mAdapter = new PhotoAdapter(getActivity(), mDataList, R.layout.item_rv_sai, mRecyclerView);
    return mAdapter;
  }

  @Override RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  @Subscribe public void onPicturesFetched(SaiEvent.PictureFetched fetched) {
    mediaFetched(fetched.getResponseList());
  }

  @Subscribe public void toRefresh(SaiEvent.RefreshPhotoList refreshPhotoList) {
    isRefreshed = true;
    mAdapter.setIsLoading(false);
    initiateServiceCall();
  }

  @Subscribe public void loadMorePhotos(SaiEvent.LoadMorePhotos loadMorePhotos) {
    loadMoreMedia();
  }

  @Subscribe public void onPictureTitleClick(SaiEvent.TitleClick onClick) {
   // mediaItemClick(onClick.getPosition());
    photoclick(mDataList,onClick.getPosition(),onClick.getPosition());
  }

  @Subscribe public void onPicturesFetchedFailed(SaiEvent.PictureFetchFailed fetchFailed) {
    mediaFetchFailed(fetchFailed.getFailedResponse(), fetchFailed.getReason());
  }
}
