package com.cnbit.nimmasarkara.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

/**
 * Created by DC on 22-Sep-15.
 * Viewpager adapter
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

  private ArrayList<String> titles = null;
  private ArrayList<Fragment> fragments = null;

  public TabsPagerAdapter(FragmentManager fm, ArrayList<String> titles,
      ArrayList<Fragment> fragments) {
    super(fm);
    this.titles = titles;
    this.fragments = fragments;
  }

  @Override public Fragment getItem(int arg0) {
    if (titles != null && titles.size() > 0) {
      return fragments.get(arg0);
    }
    return null;
  }

  @Override public int getCount() {
    if (titles != null) {
      return titles.size();
    }
    return 0;
  }

  @Override public CharSequence getPageTitle(int position) {
    if (titles != null && titles.size() > position) {
      return titles.get(position);
    }
    return "";
  }
}
