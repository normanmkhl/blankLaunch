 

package com.test.norman.launcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * with help from {@link "https://stackoverflow.com/questions/8881951/detect-home-button-press-in-android"}
 * but suited for blankLaunch...
 */
public class blankHomeWatcher {
    private static final String TAG = blankHomeWatcher.class.getSimpleName();
    private final Context context;
    private final IntentFilter filter;
    private final OnHomePressedListener listener;
    private final HomeClicksReceiver receiver;

    public blankHomeWatcher(Context context, OnHomePressedListener onHomePressedListener) {
        this.context = context;
        this.listener = onHomePressedListener;
        filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        receiver = new HomeClicksReceiver();
    }

    public void startWatch() {
        if (receiver != null)
            context.registerReceiver(receiver, filter);
    }

    public void stopWatch() {
        if (receiver != null)
            context.unregisterReceiver(receiver);
    }

    @FunctionalInterface
    public interface OnHomePressedListener {
        void onHomePressed();
    }

    class HomeClicksReceiver extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    Log.d(TAG, "action: " + action + " ,reason: " + reason);
                    if (listener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            listener.onHomePressed();
                        }
                    }
                }
            }
        }
    }
}