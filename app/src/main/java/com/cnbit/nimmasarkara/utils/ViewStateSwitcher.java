package com.cnbit.nimmasarkara.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DC on 7/21/2016.
 */
public final class ViewStateSwitcher {
  private Set<View> contentStates = new HashSet<>();
  private Set<View> errorStates = new HashSet<>();
  private Set<View> emptyStates = new HashSet<>();
  private Set<View> progressStates = new HashSet<>();
  private TextView progressLabel;
  private TextView errorLabel;
  private TextView emptyLabel;

  public ViewStateSwitcher() {
  }

  private static <T> T isNull(T view, Object errorMsg) {
    if (view == null) throw new NullPointerException(String.valueOf(errorMsg));
    return view;
  }

  private void addContentState(View content) {
    this.contentStates.add(content);
  }

  private void addErrorState(View error) {
    this.errorStates.add(error);
  }

  private void addEmptyState(View empty) {
    this.emptyStates.add(empty);
  }

  private void addProgressState(View progress) {
    this.progressStates.add(progress);
  }

  private void setProgressLabel(TextView progressLabel) {
    this.progressLabel = progressLabel;
  }

  public void setErrorLabel(TextView errorLabel) {
    this.errorLabel = errorLabel;
  }

  public void setEmptyLabel(TextView emptyLabel) {
    this.emptyLabel = emptyLabel;
  }

  public static class with {
    private ViewStateSwitcher viewStateSwitcher = new ViewStateSwitcher();
    private Context context;

    public with(Context context) {
      this.context = context;
    }

    public with addContentState(View content) {
      viewStateSwitcher.addContentState(isNull(content, "Content state is null"));
      return this;
    }

    public with addErrorState(View error) {
      viewStateSwitcher.addErrorState(isNull(error, "Error state is null"));
      return this;
    }

    public with addEmptyState(View empty) {
      viewStateSwitcher.addEmptyState(isNull(empty, "Empty state is null"));
      return this;
    }

    public with addProgressState(View progress) {
      viewStateSwitcher.addProgressState(isNull(progress, "Progress state is null"));
      return this;
    }

    public ViewStateSwitcher build() {
      viewStateSwitcher.setupStates();
      return viewStateSwitcher;
    }
  }

  private void setupStates() {
    for (View contentState : contentStates) {
      contentState.setVisibility(View.VISIBLE);
    }

    for (View errorState : errorStates) {
      errorState.setVisibility(View.INVISIBLE);
    }

    for (View progressState : progressStates) {
      progressState.setVisibility(View.INVISIBLE);
    }

    for (View emptyState : emptyStates) {
      emptyState.setVisibility(View.INVISIBLE);
    }
  }

  private Set<View> excludedMembers(Set<View> exclude) {
    Set<View> members = new HashSet<>();
    members.addAll(contentStates);
    members.addAll(errorStates);
    members.addAll(emptyStates);
    members.addAll(progressStates);
    members.removeAll(exclude);
    return members;
  }

  private void showStates(Set<View> states) {
    for (View state : states) {
      state.setVisibility(View.VISIBLE);
    }
  }

  private void hideStates(Set<View> states) {
    for (View state : states) {
      state.setVisibility(View.INVISIBLE);
    }
  }

  public void showContentStates() {
    showStates(contentStates);
    hideStates(excludedMembers(contentStates));
  }

  public void showErrorStates() {
    showStates(errorStates);
    hideStates(excludedMembers(errorStates));
  }

  public void showEmptyStates() {
    showStates(emptyStates);
    hideStates(excludedMembers(emptyStates));
  }

  public void showProgressStates() {
    showStates(progressStates);
    hideStates(excludedMembers(progressStates));
  }
}
