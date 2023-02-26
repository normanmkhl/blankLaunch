 

package com.test.norman.launcher.broadcast_receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.utils.BPrefs;

public class DownloadManagerReceiver extends BroadcastReceiver {

    public static void changeToDownloadedState(Context context) {
        final SharedPreferences sharedPreferences = BPrefs.get(context);
        final int LAST_APK_VERSION_KEY = sharedPreferences.getInt(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER, -1);
        sharedPreferences.edit()
                .putInt(BPrefs.LAST_APK_VERSION_KEY, LAST_APK_VERSION_KEY)
                .remove(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID)
                .remove(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER)
                .apply();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences sharedPreferences = BPrefs.get(context);
        if (BuildConfig.FLAVOR.equals("blankUpdates"))
            if (sharedPreferences.getLong(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID, -3) == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -2) && sharedPreferences.contains(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER)) {
                changeToDownloadedState(context);
            }
    }
}
