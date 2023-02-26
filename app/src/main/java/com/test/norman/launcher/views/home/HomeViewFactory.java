 

package com.test.norman.launcher.views.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.adapters.blankPagerAdapter;
import com.test.norman.launcher.views.blankLinearLayoutButton;
import com.test.norman.launcher.views.HomeScreenAppView;

public class HomeViewFactory extends HomeView {
    public static final String TAG = HomeViewFactory.class.getSimpleName();
    public static final int AMOUNT_PER_PAGE = 8;
    public ConstraintLayout child;

    public HomeViewFactory(@NonNull HomeScreenActivity homeScreen) {
        super(homeScreen, homeScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        final ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.home_factory, container, false);
        this.child = view;
        return view;
    }

    public void populate(int index) {
        @NonNull final blankPagerAdapter blankPagerAdapter = homeScreen.blankPagerAdapter;

        final int startIndex = AMOUNT_PER_PAGE * (index);
        int endIndex = ((AMOUNT_PER_PAGE * (index)) + AMOUNT_PER_PAGE);
        if (endIndex > blankPagerAdapter.pinnedList.size())
            endIndex = blankPagerAdapter.pinnedList.size();

        for (int i = 0; i < endIndex - startIndex; i++) {
            final HomeScreenAppView homeScreenAppView = new HomeScreenAppView(
                    (blankLinearLayoutButton) child.getChildAt(
                            i / 2
                                    +
                                    (((i % 2)) * AMOUNT_PER_PAGE / 2))
            );
            homeScreenAppView.setVisibility(VISIBLE);
            blankPagerAdapter.pinnedList.get(i + startIndex).applyToHomeScreenAppView(homeScreenAppView);
        }
    }

    public void recycle() {
        for (int i = 0; i < child.getChildCount(); i++) {
            final View view = child.getChildAt(i);
            view.setVisibility(View.INVISIBLE);
            view.setOnClickListener(null);
        }
    }
}

