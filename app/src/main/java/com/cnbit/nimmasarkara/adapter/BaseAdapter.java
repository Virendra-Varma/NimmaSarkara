package com.cnbit.nimmasarkara.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.cnbit.nimmasarkara.R;
import android.view.ViewGroup;
import com.cnbit.nimmasarkara.utils.LogUtils;
import java.util.List;

/**
 * Created by DC on 10/16/2016.
 * Base adapter for recycler view supports load more and swipeRefresh calls
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int PROGRESS_VIEW = 1;
  private static final int DATA_VIEW = 2;
  protected List<T> mDataList;
  private int mLayoutResource;

  private int lastVisibleItem;
  private int totalItemCount;
  private boolean isLoading;
  private int visibleThreshold = 1;

  public BaseAdapter(List<T> mDataList, @LayoutRes int layoutResource) {
    this(mDataList, layoutResource, null);
  }

  public BaseAdapter(List<T> mDataList, @LayoutRes int layoutResource, RecyclerView recyclerView) {
    this.mDataList = mDataList;
    this.mLayoutResource = layoutResource;
    if (recyclerView != null) {
      addLoadMoreSetup(recyclerView);
    }
  }

  private void addLoadMoreSetup(RecyclerView recyclerView) {
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      final LinearLayoutManager linearLayoutManager =
          (LinearLayoutManager) recyclerView.getLayoutManager();
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          totalItemCount = linearLayoutManager.getItemCount();
          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            try {
              sendLoadMoreEvent();
              isLoading = true;
            } catch (RuntimeException ex) {
              ex.printStackTrace();
            }
          }
        }
      });
    } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      final GridLayoutManager linearLayoutManager =
          (GridLayoutManager) recyclerView.getLayoutManager();
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          totalItemCount = linearLayoutManager.getItemCount();
          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          LogUtils.debugOut(""+isLoading);
          if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            try {
              sendLoadMoreEvent();
              isLoading = true;
            } catch (RuntimeException ex) {
              ex.printStackTrace();
            }
          }
        }
      });
    }
  }

  public void setIsLoading(boolean flag) {
    this.isLoading = flag;
  }

  public void updateAdapter(List<T> list) {
    mDataList.addAll(list);
    notifyDataSetChanged();
  }

  public void refreshAdapter(List<T> list) {
    mDataList.clear();
    mDataList.addAll(list);
    notifyDataSetChanged();
  }

  abstract void bindData(RecyclerView.ViewHolder holder, T data);

  abstract RecyclerView.ViewHolder getCurrentViewHolder(View view);

  abstract void sendLoadMoreEvent();

  
  protected T getData(int position) {
    return mDataList.get(position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    RecyclerView.ViewHolder viewHolder;
    switch (viewType) {
      case PROGRESS_VIEW:
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_progress, parent, false);
        viewHolder = new ProgressViewHolder(view);
        break;

      case DATA_VIEW:
        view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResource, parent, false);
        viewHolder = getCurrentViewHolder(view);
        break;
      default:
        view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResource, parent, false);
        viewHolder = getCurrentViewHolder(view);
    }
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (getData(position) != null) {
      bindData(holder, getData(position));
    }
  }

  @Override public int getItemCount() {
    return mDataList != null ? mDataList.size() : 0;
  }

  @Override public int getItemViewType(int position) {
    if (mDataList.get(position) == null) {
      return PROGRESS_VIEW;
    }
    return DATA_VIEW;
  }

  private static class ProgressViewHolder extends RecyclerView.ViewHolder {
    ProgressViewHolder(View v) {
      super(v);
    }
  }
}
