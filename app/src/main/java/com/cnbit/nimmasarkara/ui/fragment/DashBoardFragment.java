package com.cnbit.nimmasarkara.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.adapter.TabsPagerAdapter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

  private View mRootView;
  private ViewPager mMediaVp;
  private TabLayout mTabLayout;
  private TabsPagerAdapter mAdapter;
  String pos;
  public DashBoardFragment() {
    // Required empty public constructor
  }

  public static DashBoardFragment getInstance() {
    return new DashBoardFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    if (mRootView == null) {
      mRootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
      pos = getArguments().getString(AppConstantsUtils.TYPE);
     // Toast.makeText(getActivity(),pos,Toast.LENGTH_SHORT).show();
      setupUi(mRootView);
    }
    return mRootView;
  }

  private void setupUi(View view) {
    mMediaVp = (ViewPager) view.findViewById(R.id.media_view_pager);
    mMediaVp.setOffscreenPageLimit(3);
    mTabLayout = (TabLayout) view.findViewById(R.id.media_tab_layout);
    mTabLayout.setVisibility(View.GONE);
    ArrayList<Fragment> fragments = new ArrayList<>(3);
    if(pos.equals("1")) {
      fragments.add(new PhotoListFragment());
    }
    else if(pos.equals("2")) {
      fragments.add(new VideoListFragment());
    }
    else if(pos.equals("3")) {
      fragments.add(new AudioListFragment());
    }
    else{
      fragments.add(new VideoListFragment());
    }

    ArrayList<String> titles = new ArrayList<>(3);
    if(pos.equals("1")) {
      titles.add("Photos");
    }
      else if(pos.equals("2")) {

      titles.add("Videos");
    }
      else if(pos.equals("3")) {
        titles.add("Audios");
      }
      else {
        titles.add("Videos");
      }
    mAdapter = new TabsPagerAdapter(getFragmentManager(), titles, fragments);
    mMediaVp.setAdapter(mAdapter);
    mTabLayout.setupWithViewPager(mMediaVp);
  }

}
