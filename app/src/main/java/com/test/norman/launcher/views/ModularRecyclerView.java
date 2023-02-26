 

package com.test.norman.launcher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.D;

//most probably must be match parent! remember!
public class ModularRecyclerView extends RecyclerView implements Modular {
    public boolean touchEnabled;

    public ModularRecyclerView(Context context) {
        super(context);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    public ModularRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    public ModularRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchEnabled = context.getSharedPreferences(D.blank_PREFS, Context.MODE_PRIVATE).getBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchEnabled && super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof ModularAdapter) {
            super.setAdapter(adapter);
            final AdapterDataObserver emptyObserver = ((ScrollingHelper) getParent()).emptyObserver;
            adapter.registerAdapterDataObserver(emptyObserver);
            emptyObserver.onChanged();

        } else
            throw new IllegalArgumentException("Adapter must be Modular!, and remember to call super.onBindViewHolder!");
    }

    public static abstract class ModularAdapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {
        public static final ModularAdapter EMPTY_ADAPTER = new ModularAdapter() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        //For the future
        @Override
        public void onBindViewHolder(@NonNull T holder, int position) {

        }
    }
}
