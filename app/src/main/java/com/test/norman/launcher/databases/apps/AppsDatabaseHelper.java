 

package com.test.norman.launcher.databases.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.AppsActivity;
import com.test.norman.launcher.activities.DialerActivity;
import com.test.norman.launcher.activities.Page1EditorActivity;
import com.test.norman.launcher.activities.RecentActivity;
import com.test.norman.launcher.activities.alarms.AlarmsActivity;
import com.test.norman.launcher.activities.contacts.ContactsActivity;
import com.test.norman.launcher.activities.media.PhotosActivity;
import com.test.norman.launcher.activities.media.VideosActivity;
import com.test.norman.launcher.activities.pills.PillsActivity;
import com.test.norman.launcher.utils.S;
import com.bumptech.glide.Glide;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * static class of useful methods when using the Apps Database
 */
public class AppsDatabaseHelper {
    private static final String TAG = AppsDatabaseHelper.class.getSimpleName();

    public static final String blankComponentNameBeginning = BuildConfig.APPLICATION_ID + "/";
    public static Map<String, Integer> blankComponentNames = new HashMap<>(9);

    static {
        if (!BuildConfig.FLAVOR.equals("gPlay"))
            blankComponentNames.put(blankComponentNameBeginning + RecentActivity.class.getName(), R.drawable.history_on_background);
        blankComponentNames.put(blankComponentNameBeginning + ContactsActivity.class.getName(), R.drawable.human_on_background);
        blankComponentNames.put(blankComponentNameBeginning + DialerActivity.class.getName(), R.drawable.phone_on_background);
        blankComponentNames.put(blankComponentNameBeginning + PhotosActivity.class.getName(), R.drawable.photo_on_background);
        blankComponentNames.put(blankComponentNameBeginning + VideosActivity.class.getName(), R.drawable.movie_on_background);
        blankComponentNames.put(blankComponentNameBeginning + PillsActivity.class.getName(), R.drawable.pill);
        blankComponentNames.put(blankComponentNameBeginning + AppsActivity.class.getName(), R.drawable.apps_on_background);
        blankComponentNames.put(blankComponentNameBeginning + AlarmsActivity.class.getName(), R.drawable.clock_on_background);
        blankComponentNames.put(blankComponentNameBeginning + Page1EditorActivity.class.getName(), R.drawable.edit_on_background);
    }

    private static List<String> getInstalledAppsFlattenComponentNames(Context context) {
        final PackageManager pm = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        final List<String> componentNames = new ArrayList<>(resolveInfos.size() + blankComponentNames.size());

        ActivityInfo activityInfo;
        ComponentName componentName;

        for (int i = 0; i < resolveInfos.size(); i++) {
            activityInfo = resolveInfos.get(i).activityInfo;
            componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
            if (componentName.getPackageName().equals(BuildConfig.APPLICATION_ID))
                continue;
            componentNames.add(componentName.flattenToString());
        }
        componentNames.addAll(blankComponentNames.keySet());
        return componentNames;
    }

    /**
     * Updates Apps Database - should never throw any exception
     */
    public static void updateDB(Context context) {

        final AppsDatabase appsDatabase = AppsDatabase.getInstance(context);
        final int dbAmountBefore = appsDatabase.appsDatabaseDao().getNumberOfRows();
        final List<String> addList = new ArrayList<>();
        final List<String> realApps = getInstalledAppsFlattenComponentNames(context);

        int realAppsInDbCount = 0;

        for (String componentName : realApps) {
            if (appsDatabase.appsDatabaseDao().findByFlattenComponentName(componentName) == null) {
                addList.add(componentName);
            } else {
                ++realAppsInDbCount;
            }
        }

        if (addList.size() > 0) {
            final PackageManager packageManager = context.getPackageManager();
            final App[] appsToAdd = new App[addList.size()];
            int counter = 0;
            for (String componentName : addList) {
                try {
                    final App app = new App();
                    appsToAdd[counter++] = app;
                    app.setFlattenComponentName(componentName);
                    final ActivityInfo activityInfo =
                            packageManager.getActivityInfo(ComponentName.unflattenFromString(componentName), PackageManager.MATCH_DEFAULT_ONLY);
                    app.setLabel(String.valueOf(activityInfo.loadLabel(packageManager)));
                    final Drawable drawable = activityInfo.loadIcon(packageManager);
                    if (drawable instanceof BitmapDrawable)
                        app.setIcon(S.bitmapToByteArray(((BitmapDrawable) drawable).getBitmap()));
                    else
                        app.setIcon(S.bitmapToByteArray(S.getBitmapFromDrawable(drawable)));

                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    ACRA.getErrorReporter().handleSilentException(new RuntimeException("cannot happen! new app is not found", e));
                }

            }
            appsDatabase.appsDatabaseDao().insertAll(appsToAdd);
        }

        if (dbAmountBefore > realAppsInDbCount) {
            final int howMuchToGo = dbAmountBefore - realAppsInDbCount;
            final int[] idsToDelete = new int[howMuchToGo];

            int idsToDeleteCounter = 0;
            boolean shouldDelete;

            for (App app : appsDatabase.appsDatabaseDao().getAll()) {
                shouldDelete = true;
                for (String componentName : realApps)
                    if (app.getFlattenComponentName().equals(componentName)) {
                        shouldDelete = false;
                        break;
                    }
                if (shouldDelete)
                    idsToDelete[idsToDeleteCounter++] = app.getId();

            }
            appsDatabase.appsDatabaseDao().deleteByIds(idsToDelete);
        }
    }

    public static void loadPic(App app, ImageView imageView) {
        if (app.getFlattenComponentName().startsWith(blankComponentNameBeginning))
            imageView.setImageResource(AppsDatabaseHelper.blankComponentNames.get(app.getFlattenComponentName()));
        else
            Glide.with(imageView).load(S.byteArrayToBitmap(app.getIcon())).into(imageView);
    }
}
