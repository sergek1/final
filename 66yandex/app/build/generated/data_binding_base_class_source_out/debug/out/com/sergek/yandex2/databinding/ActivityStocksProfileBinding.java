// Generated by view binder compiler. Do not edit!
package com.sergek.yandex2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.sergek.yandex2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityStocksProfileBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final TabLayout tabsStocks;

  @NonNull
  public final ViewPager viewPagerStocks;

  private ActivityStocksProfileBinding(@NonNull CoordinatorLayout rootView,
      @NonNull TabLayout tabsStocks, @NonNull ViewPager viewPagerStocks) {
    this.rootView = rootView;
    this.tabsStocks = tabsStocks;
    this.viewPagerStocks = viewPagerStocks;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityStocksProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityStocksProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_stocks_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityStocksProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.tabsStocks;
      TabLayout tabsStocks = rootView.findViewById(id);
      if (tabsStocks == null) {
        break missingId;
      }

      id = R.id.viewPagerStocks;
      ViewPager viewPagerStocks = rootView.findViewById(id);
      if (viewPagerStocks == null) {
        break missingId;
      }

      return new ActivityStocksProfileBinding((CoordinatorLayout) rootView, tabsStocks,
          viewPagerStocks);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
