package com.cnbit.nimmasarkara.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.ui.fragment.DashBoardFragment1;
import com.cnbit.nimmasarkara.utils.AppUtils;

public class DashboardActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout mDrawerLayout;
  private NavigationView mNavigationView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setTitle("");
    }
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });
    TextView marqueetext = (TextView) findViewById(R.id.marquee_text);
    marqueetext.setSelected(true);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    mDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();
    mNavigationView = (NavigationView) findViewById(R.id.nav_view);
    mNavigationView.setNavigationItemSelectedListener(this);
    View drawerHeader = mNavigationView.getHeaderView(0);
    TextView name = (TextView) drawerHeader.findViewById(R.id.username);
    TextView mobileNo = (TextView) drawerHeader.findViewById(R.id.mobile_no);
    AppUtils appUtils = new AppUtils(this);
    OtpResponse response = appUtils.getObject("OTP_RESPONSE", OtpResponse.class);
    if (response != null) {
      name.setText(response.getName());
      mobileNo.setText(response.getMobile());
    }
    replaceFragment(new DashBoardFragment1(), false);
  }

  @Override public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  public void replaceFragment(Fragment fragment, boolean addToBackStack) {
    String backStateName = fragment.getClass().getName();
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.content_dashboard, fragment);

    if (addToBackStack) mFragmentTransaction.addToBackStack(backStateName);
    mFragmentTransaction.commitAllowingStateLoss();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.dashboard, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.

    if (item.getItemId() == R.id.nav_share) {
      shareApp();
    } else if (item.getItemId() == R.id.nav_your_photos) {
      // TODO: 10/12/2016  call your photos service
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void shareApp() {
    int applicationNameId = getApplicationInfo().labelRes;
    final String appPackageName = getPackageName();
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("text/plain");
    i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
    String text = "Install this cool application: ";
    String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
    i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
    startActivity(Intent.createChooser(i, "Share link:"));
  }
}
