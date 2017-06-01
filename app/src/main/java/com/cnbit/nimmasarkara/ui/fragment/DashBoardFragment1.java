package com.cnbit.nimmasarkara.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.ImageVpAdapter;
import com.cnbit.nimmasarkara.ui.activity.AboutUsActivity;
import com.cnbit.nimmasarkara.ui.activity.AlbumsActivity;
import com.cnbit.nimmasarkara.ui.activity.EventsListActivity;
import com.cnbit.nimmasarkara.ui.activity.VisionActivity;
import com.cnbit.nimmasarkara.ui.activity.DonateIdolActivity;
import com.cnbit.nimmasarkara.ui.activity.LandingPageActivity;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment1 extends Fragment implements View.OnClickListener {

  private ViewPager mSliderVp;
  private View mRootView;
  private ImageVpAdapter mAdapter;
  private CirclePageIndicator mIndicator;

  public DashBoardFragment1() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (null == mRootView) {
      mRootView = inflater.inflate(R.layout.fragment_dash_board_fragment1, container, false);
      setupUi(mRootView);
    }
    return mRootView;
  }

  private void setupUi(View view) {
    //ImageView mLeftNav = (ImageView) view.findViewById(R.id.left_nav);
    //ImageView mRightNav = (ImageView) view.findViewById(R.id.right_nav);
    TextView mAboutUs = (TextView) view.findViewById(R.id.about_us);
    TextView mVision = (TextView) view.findViewById(R.id.vision);
    TextView mDonateIdol = (TextView) view.findViewById(R.id.donate_idol);
    TextView mGallery = (TextView) view.findViewById(R.id.gallery);
    TextView mEvents = (TextView) view.findViewById(R.id.event);
    TextView mAlbums = (TextView) view.findViewById(R.id.album);
    mSliderVp = (ViewPager) view.findViewById(R.id.slider_vp);
    mSliderVp.setPageTransformer(false, new FadePageTransformer());
    mIndicator = (CirclePageIndicator) view.findViewById(R.id.slider_page_indicator);
    List<Object> list = new ArrayList<>();
    list.add(R.drawable.slide_1);
    list.add(R.drawable.slide_2);
    list.add(R.drawable.slide_3);
    list.add(R.drawable.slide_4);

    mIndicator.setFillColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
    mIndicator.setRadius(10.0f);
    mAdapter = new ImageVpAdapter(list);
    mSliderVp.setAdapter(mAdapter);
    mIndicator.setViewPager(mSliderVp);
    //mLeftNav.setOnClickListener(this);
    //mRightNav.setOnClickListener(this);
    mAboutUs.setOnClickListener(this);
    mVision.setOnClickListener(this);
    mDonateIdol.setOnClickListener(this);
    mGallery.setOnClickListener(this);
    mEvents.setOnClickListener(this);
    mAlbums.setOnClickListener(this);

    final Handler handler = new Handler();

    final Runnable update = new Runnable() {
      public void run() {
        nextSlide();
      }
    };

    new Timer().schedule(new TimerTask() {

      @Override public void run() {
        handler.post(update);
      }
    }, 1000, 5000);

    TextView ticker = (TextView)getActivity().findViewById(R.id.marquee_text);
    if(ticker != null)
      ticker.setSelected(true);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
    /*  case R.id.left_nav:
        previousSlide();
        break;
      case R.id.right_nav:
        nextSlide();
        break;*/
      case R.id.about_us:
        getActivity().startActivity(new Intent(getActivity(), AboutUsActivity.class));
        break;
      case R.id.vision:
        getActivity().startActivity(new Intent(getActivity(), VisionActivity.class));
        break;
      case R.id.donate_idol:
        getActivity().startActivity(new Intent(getActivity(), DonateIdolActivity.class));
        break;
      case R.id.gallery:
        getActivity().startActivity(new Intent(getActivity(), LandingPageActivity.class));
        break;
      case R.id.event:
        getActivity().startActivity(new Intent(getActivity(), EventsListActivity.class));
        break;
      case R.id.album:
        getActivity().startActivity(new Intent(getActivity(), AlbumsActivity.class));
        break;
    }
  }

  private void nextSlide() {
    mSliderVp.setCurrentItem(mSliderVp.getCurrentItem() == 3 ? 0 : mSliderVp.getCurrentItem() + 1,
        true);
  }

  private void previousSlide() {
    mSliderVp.setCurrentItem(mSliderVp.getCurrentItem() == 0 ? 3 : mSliderVp.getCurrentItem() - 1,
        true);
  }

  private class FadePageTransformer implements ViewPager.PageTransformer {
    @Override public void transformPage(View view, float position) {
      if (position <= -1.0F || position >= 1.0F) {
        view.setTranslationX(view.getWidth() * position);
        view.setAlpha(0.0F);
      } else if (position == 0.0F) {
        view.setTranslationX(view.getWidth() * position);
        view.setAlpha(1.0F);
      } else {
        // position is between -1.0F & 0.0F OR 0.0F & 1.0F
        view.setTranslationX(view.getWidth() * -position);
        view.setAlpha(1.0F - Math.abs(position));
      }
    }
  }
}
