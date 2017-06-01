package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.VideoAdapter;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.squareup.otto.Subscribe;

/**
 * Created by DC on 10/10/2016.
 */

public class VideoListFragment extends MediaBaseFragment {
    private VideoAdapter mAdapter;

    public static VideoListFragment getInstance() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstantsUtils.TYPE, AppConstantsUtils.VIDEO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = AppConstantsUtils.VIDEO;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils appUtils = new AppUtils(getActivity());
        if (appUtils.isLikedVideo()) {
            appUtils.setLikedVideo(false);
            isRefreshed = true;
            initiateServiceCall();
        }
    }

    @Override
    RecyclerView.Adapter createAdapter() {
        mAdapter = new VideoAdapter(mDataList, R.layout.item_rv_sai, mRecyclerView, getContext());
        return mAdapter;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Subscribe
    public void toRefresh(SaiEvent.RefreshVideoList refreshVideoList) {
        isRefreshed = true;
        mAdapter.setIsLoading(false);
        initiateServiceCall();
    }

    @Subscribe
    public void onVideoTitleClick(SaiEvent.VideoTitleClick onClick) {
        mediaItemClick(onClick.getPosition());
        photoclick(mDataList, onClick.getPosition(), onClick.getPosition());
    }

    @Subscribe
    public void onVideosFetched(SaiEvent.VideoFetched fetched) {
        mediaFetched(fetched.getResponseList());
    }

    @Subscribe
    public void loadMoreVideos(SaiEvent.LoadMoreVideos loadMoreVideos) {
        loadMoreMedia();
    }

    @Subscribe
    public void onVideosFetchedFailed(SaiEvent.VideoFetchFailed fetchFailed) {
        mediaFetchFailed(fetchFailed.getFailedResponse(), fetchFailed.getReason());
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }


}
