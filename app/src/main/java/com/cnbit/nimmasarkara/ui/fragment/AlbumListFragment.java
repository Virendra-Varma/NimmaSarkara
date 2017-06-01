package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.model.response.AlbumResponse;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.adapter.AlbumAdapter;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.ui.activity.AlbumsActivity;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.ViewStateSwitcher;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DC on 10/16/2016.
 */

public class AlbumListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
  private View mRootView;
  private TextView errorState;
  private RecyclerView mAlbumsRv;
  private List<AlbumResponse> mAlbumResponseList;
  private ViewStateSwitcher mViewStateSwitcher;
  private AlbumAdapter mAdapter;
  private SwipeRefreshLayout mSwipeRefresh;
  private boolean isLoading;
  private boolean isRefreshed;

  public static AlbumListFragment getInstance() {
    return new AlbumListFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (mRootView == null) {
      mRootView = inflater.inflate(R.layout.fragment_album_list, container, false);
      setupUi(mRootView);
    }
    return mRootView;
  }

  private void setupUi(View view) {
    errorState = (TextView) view.findViewById(R.id.error_state);
    TextView emptyState = (TextView) view.findViewById(R.id.empty_state);
    View progressState = view.findViewById(R.id.progress_state);
    TextView progressLbl = (TextView) view.findViewById(R.id.progress_lbl);
    mAlbumsRv = (RecyclerView) view.findViewById(R.id.albums_rv);
    mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
    emptyState.setText(getString(R.string.no_albums));
    progressLbl.setText(getString(R.string.loading_albums));
    mSwipeRefresh.setOnRefreshListener(this);
    mViewStateSwitcher = new ViewStateSwitcher.with(getActivity()).addContentState(mAlbumsRv)
        .addEmptyState(emptyState)
        .addErrorState(errorState)
        .addProgressState(progressState)
        .build();

    mAlbumsRv.setHasFixedSize(true);
    mAlbumsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    mAlbumsRv.setItemAnimator(new DefaultItemAnimator());
    mAlbumResponseList = new ArrayList<>();
    mAdapter = new AlbumAdapter(getActivity(), mAlbumResponseList, R.layout.item_album, mAlbumsRv);
    mAlbumsRv.setAdapter(mAdapter);
    initiateServiceCall();
  }

  @Override public void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override public void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void onAlbumClick(SaiEvent.AlbumClick albumClick) {
   /* AlbumResponse ab = mAlbumResponseList.get(albumClick.getPosition());
    ((AlbumsActivity) getActivity()).replaceFragment(
        AlbumDetailFragment.getInstance(Integer.valueOf(ab.getAlbumid())), true);*/
  }

  @Subscribe public void onAlbumMediaItemClick(SaiEvent.AlbumMediaItemClick albumMediaItemClick) {
    ((AlbumsActivity) getActivity()).replaceFragment(
        AlbumDetailFragment.getInstance(albumMediaItemClick.getPosition(),
            albumMediaItemClick.getAlbumData()), true);
  }

  @Subscribe public void onAlbumsFetched(SaiEvent.AlbumsFetched fetched) {
    mViewStateSwitcher.showContentStates();
    if (!fetched.getResponseList().isEmpty()) {
      if (mAdapter != null) {
        if (isRefreshed) {
          setRefreshed();
          mAdapter.refreshAdapter(fetched.getResponseList());
        } else {
          mAdapter.updateAdapter(fetched.getResponseList());
        }
      }
    } else {
      ToastUtils.showToast(getActivity(), "Empty Response");
    }
  }

  @Subscribe public void onAlbumsNotFetched(SaiEvent.AlbumsNotFetched notFetched) {
    if (isRefreshed) {
      setRefreshed();
    }
    mViewStateSwitcher.showErrorStates();
    if (notFetched.getReason() != null) {
      ToastUtils.showSnackBar(getView(), notFetched.getReason());
    } else {
      ToastUtils.showSnackBar(getView(), notFetched.getFailedResponse().getMsg());
    }
  }

  private void initiateServiceCall() {
    if (NetworkUtils.haveNetworkConnection(getActivity())) {
      RestDataService service = new RestDataService();
      service.getAlbums();
      if (!isLoading) {
        mViewStateSwitcher.showProgressStates();
      }
    } else {
      ToastUtils.showNoInternetSnackBar(mAlbumsRv);
    }
  }

  private void setRefreshed() {
    isRefreshed = false;
    mSwipeRefresh.setRefreshing(false);
  }

  @Override public void onRefresh() {
    isRefreshed = true;
    mViewStateSwitcher.showProgressStates();
    initiateServiceCall();
  }


}
