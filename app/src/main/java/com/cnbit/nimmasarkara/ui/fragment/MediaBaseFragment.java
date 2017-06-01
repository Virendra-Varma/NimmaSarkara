package com.cnbit.nimmasarkara.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.adapter.PhotoAdapter;
import com.cnbit.nimmasarkara.adapter.SearchAdapter;
import com.cnbit.nimmasarkara.adapter.VideoAdapter;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.model.response.FailedResponse;
import com.cnbit.nimmasarkara.model.response.MediaResponse;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.ui.activity.DetailActivity;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.ViewStateSwitcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MediaBaseFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener {
  protected ViewStateSwitcher mStateSwitcher;
  protected boolean isRefreshed;
  protected RecyclerView mRecyclerView;
  protected List<MediaResponse> mDataList;
  protected boolean isLoadingPhotos;
  protected String mType;
  protected boolean isSearchPage;
  protected boolean isYourMedia;
  protected int mCurrentPageNo = 1;
  private View mEmptyState;
  private View mProgressState;
  private View mErrorState;
  private View mRootView;
  private SwipeRefreshLayout mSwipeRefresh;

  public MediaBaseFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mRootView == null) {
      mRootView = inflater.inflate(R.layout.fragment_media_list, container, false);
      setupUi(mRootView);
    }
    return mRootView;
  }

  private void setupUi(View view) {
    mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_sai);
    mEmptyState = view.findViewById(R.id.empty_state);
    mProgressState = view.findViewById(R.id.progress_state);
    mErrorState = view.findViewById(R.id.error_state);
    mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
    mStateSwitcher = new ViewStateSwitcher.with(getActivity()).addContentState(mRecyclerView)
        .addEmptyState(mEmptyState)
        .addProgressState(mProgressState)
        .addErrorState(mErrorState)
        .build();
    mSwipeRefresh.setOnRefreshListener(this);
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mDataList = new ArrayList<>();
    mRecyclerView.setAdapter(createAdapter());
    mStateSwitcher.showContentStates();
    if (isSearchPage) {
      initiateSearchServiceCall(getKeyword());
    } else if (isYourMedia) {
      initiateYourMediaServiceCall();
    } else {
      initiateServiceCall();
    }
  }

  public String getKeyword() {
    return "";
  }

  public int getAlbumId() {
    return 0;
  }

  abstract RecyclerView.Adapter createAdapter();

  abstract RecyclerView.Adapter getAdapter();

  protected void initiateYourMediaServiceCall() {
    if (NetworkUtils.haveNetworkConnection(getActivity())) {
      RestDataService service = new RestDataService();
      AppUtils appUtils = new AppUtils(getActivity());
      service.getYourMedia(mType, mCurrentPageNo, Integer.valueOf(appUtils.getUserId()));
      if (!isLoadingPhotos) {
        mStateSwitcher.showProgressStates();
      }
    } else {
      ToastUtils.showNoInternetSnackBar(mRecyclerView);
    }
  }

  protected void initiateServiceCall() {
    if (NetworkUtils.haveNetworkConnection(getActivity())) {
      RestDataService service = new RestDataService();
      service.getMedia(mType, mCurrentPageNo, getAlbumId());
      if (!isLoadingPhotos) {
        mStateSwitcher.showProgressStates();
      }
    } else {
      ToastUtils.showNoInternetSnackBar(mRecyclerView);
    }
  }

  protected void initiateSearchServiceCall(String keyword) {
    if (NetworkUtils.haveNetworkConnection(getActivity())) {
      RestDataService service = new RestDataService();
      service.search(keyword, 0);
      if (!isLoadingPhotos) {
        mStateSwitcher.showProgressStates();
      }
    } else {
      ToastUtils.showNoInternetSnackBar(mRecyclerView);
    }
  }

  protected void setRefreshed() {
    isRefreshed = false;
    mSwipeRefresh.setRefreshing(false);
  }

  @Override public void onRefresh() {
    mStateSwitcher.showProgressStates();
    isRefreshed = true;
    mCurrentPageNo = 1;
    if (isSearchPage) {
      initiateSearchServiceCall(getKeyword());
    } else if (isYourMedia) {
      initiateYourMediaServiceCall();
    } else {
      initiateServiceCall();
    }
  }

  @Override public void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override public void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  protected void mediaItemClick(int position) {
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra(AppConstantsUtils.MEDIA_DETAIL, mDataList.get(position));
    intent.putExtra(AppConstantsUtils.TYPE, mType);
    startActivity(intent);
  }

  protected void searchResultclick(int position,String mtype) {
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra(AppConstantsUtils.MEDIA_DETAIL, mDataList.get(position));
    intent.putExtra(AppConstantsUtils.TYPE, mtype);
    startActivity(intent);
  }


  protected void photoclick(List<MediaResponse> list,int position,int pos) {
    Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra(AppConstantsUtils.IMAGE_SWIPE, (Serializable) list);
    intent.putExtra(AppConstantsUtils.MEDIA_DETAIL, mDataList.get(position));
    intent.putExtra(AppConstantsUtils.CURRENT_POS,pos);
    intent.putExtra(AppConstantsUtils.TYPE, mType);
    startActivity(intent);
  }

  protected void mediaFetched(List<List<MediaResponse>> list) {
    mStateSwitcher.showContentStates();
    if (list != null && !list.isEmpty()) {
      if (getAdapter() != null && getAdapter() instanceof PhotoAdapter) {
        PhotoAdapter adapter = (PhotoAdapter) getAdapter();
        if (isRefreshed) {
          setRefreshed();
          adapter.refreshAdapter(list.get(0));
        } else {
          if (isLoadingPhotos && mDataList != null && !mDataList.isEmpty()) {
            mDataList.remove(mDataList.size() - 1);
            isLoadingPhotos = false;
          }
          adapter.updateAdapter(list.get(0));
        }
        adapter.setIsLoading(false);
      } else if (getAdapter() != null && getAdapter() instanceof VideoAdapter) {
        VideoAdapter adapter = (VideoAdapter) getAdapter();
        if (isRefreshed) {
          setRefreshed();
          adapter.refreshAdapter(list.get(0));
        } else {
          if (isLoadingPhotos && mDataList != null && !mDataList.isEmpty()) {
            mDataList.remove(mDataList.size() - 1);
            isLoadingPhotos = false;
          }
          adapter.updateAdapter(list.get(0));
        }
        adapter.setIsLoading(false);
      } else if (getAdapter() != null && getAdapter() instanceof SearchAdapter) {
        SearchAdapter adapter = (SearchAdapter) getAdapter();
        if (isRefreshed) {
          setRefreshed();
          adapter.refreshAdapter(list.get(0));
        } else {
          if (isLoadingPhotos && mDataList != null && !mDataList.isEmpty()) {
            mDataList.remove(mDataList.size() - 1);
            isLoadingPhotos = false;
          }
          adapter.updateAdapter(list.get(0));
        }
        adapter.setIsLoading(false);
      }
      /*else {
        AudioAdapter adapter = (AudioAdapter) getAdapter();
        if (isRefreshed) {
          setRefreshed();
          adapter.refreshAdapter(list.get(0));
        } else {
          if (isLoadingPhotos && mDataList != null && !mDataList.isEmpty()) {
            mDataList.remove(mDataList.size() - 1);
            isLoadingPhotos = false;
          }
          adapter.updateAdapter(list.get(0));
        }
        adapter.setIsLoading(false);
      }*/
    }
  }

  protected void mediaFetchFailed(FailedResponse failedResponse, String reason) {
    if (isRefreshed) {
      setRefreshed();
    }
    if (!isLoadingPhotos) {
      mStateSwitcher.showErrorStates();
      if (reason != null) {
        ToastUtils.showSnackBar(getView(), reason);
      } else {
        ToastUtils.showSnackBar(getView(), failedResponse.getMsg());
      }
    } else {
      if ((mDataList.get(mDataList.size() - 1) == null)) {
        mDataList.remove(mDataList.size() - 1);
        if (getAdapter() != null) {
          getAdapter().notifyItemRemoved(mDataList.size() - 1);
        }
      }
    }
  }

  protected void loadMoreMedia() {
    isLoadingPhotos = true;
    if (mDataList != null && !(mDataList.get(mDataList.size() - 1) == null)) {
      mDataList.add(null);
      if (getAdapter() != null) {
        getAdapter().notifyItemInserted(mDataList.size() - 1);
        if (isSearchPage) {
          initiateSearchServiceCall(getKeyword());
        } else if (isYourMedia) {
          initiateYourMediaServiceCall();
        } else {
          mCurrentPageNo++;
          initiateServiceCall();
        }
      }
    }
  }
}
