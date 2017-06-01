package com.cnbit.nimmasarkara.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.EventsAdapter;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.network.RestDataService;
import com.cnbit.nimmasarkara.utils.NetworkUtils;
import com.cnbit.nimmasarkara.utils.PickDateFragment;
import com.cnbit.nimmasarkara.utils.PickTimeFragment;
import com.cnbit.nimmasarkara.utils.ToastUtils;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.model.response.events.Event;
import com.cnbit.nimmasarkara.touchHelper.XEventItemTouchHelperCallback;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.BasicUtils;
import com.cnbit.nimmasarkara.utils.ViewStateSwitcher;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;

import static com.cnbit.nimmasarkara.utils.BasicUtils.getTextFromEt;

public class EventsListActivity extends AppCompatActivity {
  private TextView errorState;
  private RecyclerView mEventsRv;
  private ViewStateSwitcher mViewStateSwitcher;
  private EventsAdapter mAdapter;
  private List<Event> mEventsList;
  private AppUtils mAppUtils;
  private RestDataService service;
  private ProgressDialog mProgressDialog;
  private boolean isRefresh;
  // dialog things
  private TextInputLayout mTitleTil;
  private TextInputLayout mDescriptionTil;
  private TextInputEditText mTitle;
  private TextInputEditText mDescription;
  private TextView mDate;
  private TextView mTime;
  private Button mAddEvent;
  private Button mCancel;
  private AlertDialog mAddEventDialog;
  private String mDateStr;
  private String mTimeStr;
  private int day;
  private int month;
  private int year;
  private int hour;
  private int min;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_events_list);
    setupAdapter();
    setupUi();
    initiateServiceCall();
  }

  private void setupAdapter() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDefaultDisplayHomeAsUpEnabled(true);
      ab.setDisplayHomeAsUpEnabled(true);
    }
  }

  private void setupUi() {
    mAppUtils = new AppUtils(this);
    service = new RestDataService();
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setMessage(getString(R.string.adding_event));
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setCanceledOnTouchOutside(false);
    errorState = (TextView) findViewById(R.id.error_state);
    TextView emptyState = (TextView) findViewById(R.id.empty_state);
    View progressState = findViewById(R.id.progress_state);
    TextView progressLbl = (TextView) findViewById(R.id.progress_lbl);
    mEventsRv = (RecyclerView) findViewById(R.id.events_rv);
    emptyState.setText(getString(R.string.no_events));
    progressLbl.setText(getString(R.string.loading_events));
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        addEventDialog();
      }
    });

    if (mAppUtils.isUserIsAdmin()) {
      fab.setVisibility(View.VISIBLE);
    } else {
      fab.setVisibility(View.GONE);
    }
    mViewStateSwitcher = new ViewStateSwitcher.with(this).addContentState(mEventsRv)
        .addEmptyState(emptyState)
        .addErrorState(errorState)
        .addProgressState(progressState)
        .build();
    mEventsList = new ArrayList<>();
    mEventsRv.setLayoutManager(new LinearLayoutManager(this));
    mEventsRv.setHasFixedSize(true);
    mEventsRv.setItemAnimator(new DefaultItemAnimator());

    mAdapter = new EventsAdapter(this, mEventsList, R.layout.item_event, mEventsRv);
    ItemTouchHelper.Callback callback =
        new XEventItemTouchHelperCallback<>(mAdapter, mEventsRv);
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mEventsRv);
    mEventsRv.setAdapter(mAdapter);
  }

  private void initiateServiceCall() {
    if (NetworkUtils.haveNetworkConnection(this)) {
      service.getEvents();
      mViewStateSwitcher.showProgressStates();
    } else {
      ToastUtils.showNoInternetSnackBar(mEventsRv);
    }
  }

  private void initiateAddEventServiceCall() {
    if (NetworkUtils.haveNetworkConnection(this)) {
      RestDataService service = new RestDataService();
      service.addEvent(Integer.valueOf(mAppUtils.getUserId()), getTextFromEt(mTitle),
          getTextFromEt(mDescription), BasicUtils.parseDate(year, month, day, hour, min));
      mAddEventDialog.cancel();
      mProgressDialog.show();
    }
  }

  private boolean isValidTitle() {
    if (getTextFromEt(mTitle).isEmpty()) {
      mTitleTil.setError(getString(R.string.title_required));
      mTitle.requestFocus();
      return false;
    } else {
      mTitleTil.setError(null);
      return true;
    }
  }

  private boolean isValidDescription() {
    if (getTextFromEt(mDescription).isEmpty()) {
      mDescriptionTil.setError(getString(R.string.description_required));
      mDescription.requestFocus();
      return false;
    } else {
      mDescriptionTil.setError(null);
      return true;
    }
  }

  private boolean isValidDate() {
    if (mDateStr != null && !mDateStr.isEmpty()) {
      return true;
    }
    ToastUtils.showToast(this, "Invalid Date");
    return false;
  }

  private boolean isValidTime() {
    if (mTimeStr != null && !mTimeStr.isEmpty()) {
      return true;
    }
    ToastUtils.showToast(this, "Invalid Time");
    return false;
  }

  private void addEventDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    @SuppressLint("InflateParams") View view =
        LayoutInflater.from(this).inflate(R.layout.dialog_event, null, false);
    mTitle = (TextInputEditText) view.findViewById(R.id.event_title);
    mDescription = (TextInputEditText) view.findViewById(R.id.event_description);
    mTitleTil = (TextInputLayout) view.findViewById(R.id.title_til);
    mDescriptionTil = (TextInputLayout) view.findViewById(R.id.description_til);
    mDate = (TextView) view.findViewById(R.id.event_date);
    mTime = (TextView) view.findViewById(R.id.event_time);
    mAddEvent = (Button) view.findViewById(R.id.add_event_btn);
    mCancel = (Button) view.findViewById(R.id.cancel_btn);
    mAddEvent.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isValidTitle() && isValidDescription() && isValidDate() && isValidTime()) {
          initiateAddEventServiceCall();
        }
      }
    });
    mCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mAddEventDialog.dismiss();
      }
    });

    mDate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        PickDateFragment fragment = PickDateFragment.newInstance("YYYY-MM-dd");
        fragment.show(getSupportFragmentManager(), PickDateFragment.TAG);
      }
    });
    mTime.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        PickTimeFragment fragment = PickTimeFragment.getInstance();
        fragment.show(getSupportFragmentManager(), PickTimeFragment.TAG);
      }
    });
    builder.setView(view);
    mAddEventDialog = builder.create();
    mAddEventDialog.show();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onStart() {
    super.onStart();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void deleteEvent(SaiEvent.DeleteEvent deleteEvent) {
    if (NetworkUtils.haveNetworkConnection(this)) {
      RestDataService service = new RestDataService();
      service.deleteEvent(deleteEvent.getEventId());
    } else {
      ToastUtils.showNoInternetSnackBar(mEventsRv);
    }
  }

  @Subscribe public void onEventDeleted(SaiEvent.EventDeleted eventDeleted) {
    ToastUtils.showToast(this, eventDeleted.getResponse().getStatus());
  }

  @Subscribe public void onEventNotDeleted(SaiEvent.EventNotDeleted eventNotDeleted) {
    if (eventNotDeleted.getReason() != null) {
      ToastUtils.showToast(this, eventNotDeleted.getReason());
    } else {
      ToastUtils.showToast(this, eventNotDeleted.getFailedResponse().getMsg());
    }
  }

  @Subscribe public void onEventsFetched(SaiEvent.EventsFetched eventsFetched) {
    mViewStateSwitcher.showContentStates();
    if (eventsFetched.getEvents() != null && mEventsList.isEmpty() && eventsFetched.getEvents()
        .isEmpty()) {
      mViewStateSwitcher.showEmptyStates();
    } else {
      if (mAdapter != null) {
        if (isRefresh) {
          isRefresh = false;
          mAdapter.refreshAdapter(eventsFetched.getEvents());
        } else {
          mAdapter.updateAdapter(eventsFetched.getEvents());
        }
        mViewStateSwitcher.showContentStates();
      }
    }
  }

  @Subscribe public void onEventsFetchFailed(SaiEvent.EventsFetchFailed fetchFailed) {
    String errorText;
    if (fetchFailed.getReason() != null) {
      errorText = fetchFailed.getReason();
    } else {
      errorText = fetchFailed.getFailedResponse().getMsg();
    }
    if (mEventsList != null && mEventsList.isEmpty()) {
      errorState.setText(errorText);
      mViewStateSwitcher.showErrorStates();
    } else {
      mViewStateSwitcher.showContentStates();
      ToastUtils.showSnackBar(mEventsRv, fetchFailed.getReason());
    }
  }

  @Subscribe public void onEventAdded(SaiEvent.EventAdded eventAdded) {
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }
    isRefresh = true;
    initiateServiceCall();
    ToastUtils.showToast(this, eventAdded.getResponse().getStatus());
  }

  @Subscribe public void onEventNotAdded(SaiEvent.EventNotAdded notAdded) {
    if (mProgressDialog.isShowing()) {
      mProgressDialog.cancel();
    }
    if (notAdded.getReason() != null) {
      ToastUtils.showSnackBar(mEventsRv, notAdded.getReason());
    } else {
      ToastUtils.showSnackBar(mEventsRv, notAdded.getFailedResponse().getMsg());
    }
  }

  @Subscribe public void onDateSelect(PickDateFragment.DatePickEvent pickEvent) {
    if (mDate != null) {
      mDateStr = pickEvent.getDate();
      year = pickEvent.getYear();
      month = pickEvent.getMonth();
      day = pickEvent.getDay();
      mDate.setText(pickEvent.getDate());
    }
  }

  @Subscribe public void onTimeSelect(PickTimeFragment.TimePickEvent pickEvent) {
    if (mTime != null) {
      mTimeStr = pickEvent.getTime();
      hour = pickEvent.getHour();
      min = pickEvent.getMin();
      mTime.setText(pickEvent.getTime());
    }
  }
}
