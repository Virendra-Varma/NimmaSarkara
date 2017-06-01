package com.cnbit.nimmasarkara.touchHelper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by DC on 6/12/2016.
 */
public interface ItemTouchHelperAdapter {

  boolean onItemMove(int fromPosition, int toPosition);

  void onItemDismiss(int position, RecyclerView recyclerView);
}