package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cnbit.nimmasarkara.R;
import android.widget.TextView;
import com.cnbit.nimmasarkara.events.BusProvider;
import com.cnbit.nimmasarkara.events.SaiEvent;
import com.cnbit.nimmasarkara.model.response.events.Event;
import com.cnbit.nimmasarkara.touchHelper.ItemTouchHelperAdapter;
import com.cnbit.nimmasarkara.touchHelper.ItemTouchHelperViewHolder;
import java.util.Collections;
import java.util.List;

/**
 * Created by DC on 10/16/2016.
 */

public class EventsAdapter extends BaseAdapter<Event> implements ItemTouchHelperAdapter {
  private Context context;

  public EventsAdapter(Context context, List<Event> mDataList, @LayoutRes int layoutResource,
      RecyclerView recyclerView) {
    super(mDataList, layoutResource, recyclerView);
    this.context = context;
  }

  @Override void bindData(RecyclerView.ViewHolder holder, Event data) {
    EventHolder eventHolder = (EventHolder) holder;
    eventHolder.mEventTitle.setText(data.getTitle());
    eventHolder.mEventDate.setText(data.getDatetime());
    eventHolder.mEventDescription.setText(data.getDescription());
  }

  @Override RecyclerView.ViewHolder getCurrentViewHolder(View view) {
    return new EventHolder(view);
  }

  @Override void sendLoadMoreEvent() {

  }

  @Override public boolean onItemMove(int fromPosition, int toPosition) {
    if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(mDataList, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(mDataList, i, i - 1);
      }
    }
    notifyItemMoved(fromPosition, toPosition);
    return true;
  }

  @Override public void onItemDismiss(final int position, final RecyclerView recyclerView) {
    final Event event = getData(position);
    Snackbar snackbar = Snackbar.make(recyclerView, "Event Deleted", Snackbar.LENGTH_LONG)
        .setAction("Undo", new View.OnClickListener() {
          @Override public void onClick(View view) {
            mDataList.add(position, event);
            notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
          }
        });
    snackbar.show();
    snackbar.setCallback(new Snackbar.Callback() {
      @Override public void onDismissed(Snackbar snackbar, int eventFlag) {
        super.onDismissed(snackbar, eventFlag);
        BusProvider.getInstance()
            .post(new SaiEvent.DeleteEvent(Integer.valueOf(event.getEventid())));
      }
    });
    mDataList.remove(position);
    notifyItemRemoved(position);
  }

  private class EventHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    private TextView mEventTitle;
    private TextView mEventDate;
    private TextView mEventDescription;

    EventHolder(View itemView) {
      super(itemView);
      mEventTitle = (TextView) itemView.findViewById(R.id.event_title);
      mEventDate = (TextView) itemView.findViewById(R.id.event_date);
      mEventDescription = (TextView) itemView.findViewById(R.id.event_description);
    }

    @Override public void onItemSelected() {
      itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    @Override public void onItemClear() {
      itemView.setBackgroundColor(0);
    }
  }
}
