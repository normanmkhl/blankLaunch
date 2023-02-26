 

package com.test.norman.launcher.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.R;
import com.test.norman.launcher.databases.apps.AppsDatabase;
import com.test.norman.launcher.utils.BDB;
import com.test.norman.launcher.utils.BDialog;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.S;

import java.io.File;

public class TechnicalInfoActivity extends blankActivity {
    public static void deleteCache(Context context) {
        try {
            deleteDir(context.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppsDatabase.getInstance(context).appsDatabaseDao().deleteAll();
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String getTechnicalInfo() {
        return
                String.format("Api Level: %s\nVersion Name: %s\nVersion Code: %s\nFlavor: %s\nManufacturer: %s\nBrand: %s\nDevice: %s\nModel: %s\n",
                        S.str(Build.VERSION.SDK_INT),
                        S.str(BuildConfig.VERSION_NAME),
                        S.str(BuildConfig.VERSION_CODE),
                        S.str(BuildConfig.FLAVOR),
                        S.str(Build.MANUFACTURER),
                        S.str(Build.BRAND),
                        S.str(Build.DEVICE),
                        S.str(Build.MODEL))
                ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions(this, requiredPermissions()))
            return;
        setContentView(R.layout.activity_tech_info);
        ((TextView) findViewById(R.id.tv_info)).setText(getTechnicalInfo());
        findViewById(R.id.bt_clear_cache).setOnClickListener(v -> {
            BDB.from(this)
                    .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                    .setTitle(R.string.clear_cache)
                    .setSubText(R.string.clear_cache_subtext)
                    .setPositiveButtonListener(params -> {
                        deleteCache(this);
                        BPrefs.get(this).edit().remove(BPrefs.CUSTOM_APP_KEY).apply();
                        if (BuildConfig.FLAVOR.equals("blankUpdates"))
                            UpdatesActivity.removeUpdatesInfo(this);
                        blankToast.simple(this, R.string.cache_cleared_successfully);
                        return true;
                    })
                    .show();

        });

        findViewById(R.id.bt_clear_data).

                setOnClickListener(v ->

                {
                    BDB.from(this)
                            .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                            .setTitle(R.string.clear_data)
                            .setSubText(R.string.clear_data_subtext)
                            .setPositiveButtonListener(params -> {
                                BDB.from(this)
                                        .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                                        .setTitle(R.string.clear_data)
                                        .setSubText(R.string.clear_data_subtext2)
                                        .setPositiveButtonListener(params2 -> {
                                            BDB.from(this)
                                                    .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                                                    .setTitle(R.string.clear_data)
                                                    .setSubText(R.string.clear_data_subtext3)
                                                    .setPositiveButtonListener(params3 -> {
                                                        ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                                                        return true;
                                                    }).show();
                                            return true;
                                        })
                                        .show();
                                return true;
                            })
                            .show();
                });
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_WRITE_SETTINGS | PERMISSION_WRITE_EXTERNAL_STORAGE;
    }
}
