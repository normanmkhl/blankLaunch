 

package com.test.norman.launcher.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Way to trigger "choose home screen" dialog without having to ask for that Permission;
 */
public class FakeLauncherActivity extends AppCompatActivity {

    //could have been everywhere but this looks like the logical place to put it;
    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final ComponentName componentName = new ComponentName(context, FakeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        context.startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }
}