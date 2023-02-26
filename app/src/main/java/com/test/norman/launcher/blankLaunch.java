 

package com.test.norman.launcher;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.norman.launcher.activities.UpdatesActivity;
import com.test.norman.launcher.databases.alarms.AlarmScheduler;
import com.test.norman.launcher.databases.reminders.ReminderScheduler;
import com.test.norman.launcher.services.NotificationListenerService;
import com.test.norman.launcher.utils.blankUncaughtExceptionHandler;
import com.test.norman.launcher.utils.S;

import net.danlew.android.joda.JodaTimeAndroid;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

public class blankLaunch extends Application {
    private static final String TAG = blankLaunch.class.getSimpleName();
    // Application class should not have any fields, http://www.developerphil.com/dont-store-data-in-the-application-object/

    @Override
    public void onCreate() {
        S.logImportant("blankLaunch was started!");
        super.onCreate();
        JodaTimeAndroid.init(this);
        AlarmScheduler.reStartAlarms(this);
        ReminderScheduler.reStartReminders(this);
        if (BuildConfig.FLAVOR.equals("blankUpdates")) {
            UpdatesActivity.removeUpdatesInfo(this);
        }
        try {
            startService(new Intent(this, NotificationListenerService.class));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        final CoreConfigurationBuilder builder =
                new CoreConfigurationBuilder(this)
                        .setBuildConfigClass(BuildConfig.class)
                        .setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri(getString(R.string.tt_url))
                .setHttpMethod(HttpSender.Method.POST)
                .setEnabled(true);
        ACRA.init(this, builder);

        Thread.setDefaultUncaughtExceptionHandler(
                new blankUncaughtExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler())
        );
    }
}