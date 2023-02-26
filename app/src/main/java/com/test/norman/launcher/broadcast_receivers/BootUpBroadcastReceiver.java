 

package com.test.norman.launcher.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.test.norman.launcher.blankLaunch;
import com.test.norman.launcher.utils.S;

/**
 * guarantees that {@link blankLaunch#onCreate()} will be called
 */
public class BootUpBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        S.logImportant("blankLaunch OnBoot called");
    }
}
