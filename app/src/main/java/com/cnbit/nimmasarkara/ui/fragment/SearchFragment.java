package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.adapter.SearchAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by DC on 10/18/2016.
 */

public class SearchFragment extends MediaBaseFragment {
  private SearchAdapter mAdapter;
  private String mKeyword;
  private String mediaType;

  public static SearchFragment getInstance(String keyword, String type) {
    SearchFragment fragment = new SearchFragment();
    fragment.mKeyword = keyword;
    fragment.mediaType = type;
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mediaType != null && !mediaType.isEmpty()) {
      mType = mediaType;
      isYourMedia = true;
    } else {
      isSearchPage = true;
    }
  }

  @Override public void onResume() {
    super.onResume();
    AppUtils appUtils = new AppUtils(getActivity());
    if (appUtils.isLikedPic()) {
      appUtils.setLikedPic(false);
      isRefreshed = true;
      if (isSearchPage) {
        initiateSearchServiceCall(mKeyword);
      }
    }
  }

  @Override RecyclerView.Adapter createAdapter() {
    mAdapter = new SearchAdapter(getActivity(), mDataList, R.layout.item_rv_sai, mRecyclerView);
    return mAdapter;
  }

  @Override RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  @Override public String getKeyword() {
    return mKeyword;
  }


  @Subscribe public void onPicturesFetched(SaiEvent.PictureFetched fetched) {
    mediaFetched(fetched.getResponseList());
  }

  @Subscribe public void loadMorePhotos(SaiEvent.LoadMorePhotos loadMorePhotos) {
    loadMoreMedia();
  }

  @Subscribe public void onPictureTitleClick(SaiEvent.TitleClick onClick) {
    searchResultclick(onClick.getPosition(),mediaType);

  //  photoclick(mDataList,onClick.getPosition(),onClick.getPosition());
  }



  @Subscribe public void onPicturesFetchedFailed(SaiEvent.PictureFetchFailed fetchFailed) {
    mediaFetchFailed(fetchFailed.getFailedResponse(), fetchFailed.getReason());
  }
}
