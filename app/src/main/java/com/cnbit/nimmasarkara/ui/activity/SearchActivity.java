package com.cnbit.nimmasarkara.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.ui.fragment.SearchFragment;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;

public class SearchActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
    }
    String type= getIntent().getStringExtra(AppConstantsUtils.TYPE);
    String keyword = getIntent().getStringExtra(AppConstantsUtils.KEYWORD);
    replaceFragment(SearchFragment.getInstance(keyword,type), false);
  }
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  public void replaceFragment(Fragment fragment, boolean addToBackStack) {
    String backStateName = fragment.getClass().getName();
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.search_container, fragment);

    if (addToBackStack) mFragmentTransaction.addToBackStack(backStateName);
    mFragmentTransaction.commitAllowingStateLoss();
  }
}
