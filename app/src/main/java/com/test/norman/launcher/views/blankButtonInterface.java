 

package com.test.norman.launcher.views;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.norman.launcher.utils.blankToast;

import java.util.ArrayList;
import java.util.List;

public interface blankButtonInterface {
    String TAG = blankButtonInterface.class.getSimpleName();
    int MEDIUM_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout() / 2;
    int MEDIUM_PRESS_DISTANCE = 200;

    void blankPerformClick();

    void vibrate();

    class blankButtonTouchListener implements View.OnTouchListener {
        private final blankButtonInterface v;
        private final Handler longPressHandler;
        private final List<View.OnTouchListener> otherListeners = new ArrayList<>();
        private boolean isLongPressHandlerActivated = false;
        private boolean isActionMoveEventStored = false;
        private float lastActionMoveEventBeforeUpX;
        private float lastActionMoveEventBeforeUpY;
        private Runnable longPressedRunnable = new Runnable() {
            public void run() {
                v.blankPerformClick();
                v.vibrate();
                isLongPressHandlerActivated = true;
            }
        };

        public blankButtonTouchListener(blankButtonInterface v) {
            this.v = v;
            longPressHandler = new Handler();
        }

        public void addListener(View.OnTouchListener listener) {
            otherListeners.add(listener);
        }

        boolean consumeOthers(View v, MotionEvent event) {
            boolean ret = false;
            for (View.OnTouchListener onTouchListener : otherListeners)
                ret |= onTouchListener.onTouch(v, event);

            return ret;

        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                longPressHandler.postDelayed(longPressedRunnable, MEDIUM_PRESS_TIMEOUT);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                if (!isActionMoveEventStored) {
                    isActionMoveEventStored = true;
                    lastActionMoveEventBeforeUpX = event.getX();
                    lastActionMoveEventBeforeUpY = event.getY();
                } else {
                    final float currentX = event.getX();
                    final float currentY = event.getY();
                    final float firstX = lastActionMoveEventBeforeUpX;
                    final float firstY = lastActionMoveEventBeforeUpY;
                    final double distance = Math.sqrt(
                            (currentY - firstY) * (currentY - firstY) + ((currentX - firstX) * (currentX - firstX)));
                    if (distance > MEDIUM_PRESS_DISTANCE) {
                        longPressHandler.removeCallbacks(longPressedRunnable);
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                isActionMoveEventStored = false;
                longPressHandler.removeCallbacks(longPressedRunnable);
                if (isLongPressHandlerActivated) {
                    Log.d(TAG, "Long Press detected; halting propagation of motion event");
                    isLongPressHandlerActivated = false;
                    return consumeOthers(v, event);
                } else {
                    blankToast.longer(v.getContext());
                    return consumeOthers(v, event);
                }

            }
            return consumeOthers(v, event);
        }
    }
}
