 

package com.test.norman.launcher.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast receiver to know when local changes.
 */
public class LocalChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            //caches to clear when local changes:
        }
    }
}
