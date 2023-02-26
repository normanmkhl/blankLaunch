 

package com.test.norman.launcher.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public abstract class blankViewAdapter extends PagerAdapter {
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View layout = getItem(position);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    public abstract View getItem(int position);
}