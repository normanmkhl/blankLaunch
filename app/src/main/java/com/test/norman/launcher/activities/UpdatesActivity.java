 

package com.test.norman.launcher.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.BDB;
import com.test.norman.launcher.utils.BDialog;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.D;
import com.test.norman.launcher.utils.S;

import java.io.File;

import static com.test.norman.launcher.utils.UpdatingUtil.blankUpdateObject;
import static com.test.norman.launcher.utils.UpdatingUtil.FILENAME;
import static com.test.norman.launcher.utils.UpdatingUtil.getDownloadedFile;

public class UpdatesActivity extends blankActivity {
    public static final String EXTRA_blank_UPDATE_OBJECT = "EXTRA_blank_UPDATE_OBJECT";
    private static final int PROGRESS_DELAY = 200 * D.MILLISECOND;

    private blankUpdateObject blankUpdateObject;
    private DownloadManager manager;
    private long downloadId = -1;
    private Handler handler = new Handler();
    private boolean isProgressCheckerRunning = false;
    private blankToast notConnectedToast, couldNotStartDownloadToast, downloadFinishedToast, downloadingToast, downloadedFileCouldNotBeDeletedToast, tryNowToast, pleaseBePateint;
    private TextView tv_new_version, tv_current_version, tv_change_log, bt, tv_download_progress, bt_re;
    private ProgressBar pb;
    private final Runnable progressChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkProgress();
            } finally {
                handler.postDelayed(progressChecker, PROGRESS_DELAY);
            }
        }
    };
    private BroadcastReceiver downloadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (downloadId == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -2)) {
                handler.postDelayed(() -> {
                    downloadFinishedToast.show();
                    stopProgressChecker();
                    apply();
                }, 50 * D.MILLISECOND);
            }
        }
    };

    public static void removeUpdatesInfo(Context context) {
        BPrefs.get(context)
                .edit()
                .remove(BPrefs.LAST_APK_VERSION_KEY)
                .remove(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER)
                .remove(BPrefs.LAST_UPDATE_ASKED_VERSION_KEY)
                .remove(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID)
                .apply();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        if (!checkPermissions(this, requiredPermissions()))
            return;
        blankUpdateObject = getIntent().getParcelableExtra(EXTRA_blank_UPDATE_OBJECT);
        if (blankUpdateObject == null) {
            blankToast.error(this);
            finish();
            return;
        }

        manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        tv_new_version = findViewById(R.id.tv_new_version);
        tv_current_version = findViewById(R.id.tv_current_version);
        tv_change_log = findViewById(R.id.tv_change_log);
        tv_download_progress = findViewById(R.id.tv_download_progress);
        bt_re = findViewById(R.id.bt_re);
        pb = findViewById(R.id.pb);
        bt = findViewById(R.id.bt);

        notConnectedToast = blankToast.from(this).setType(blankToast.TYPE_ERROR).setText(R.string.could_not_connect_to_internet);
        couldNotStartDownloadToast = blankToast.from(this).setType(blankToast.TYPE_ERROR).setText(R.string.could_not_start_the_download);
        downloadFinishedToast = blankToast.from(UpdatesActivity.this).setText(R.string.download_finished).setLength(1);
        downloadingToast = blankToast.from(this).setText(R.string.downloading);
        downloadedFileCouldNotBeDeletedToast = blankToast.from(this).setType(blankToast.TYPE_ERROR).setText(R.string.downloaded_update_file_could_not_be_deleted);
        tryNowToast = blankToast.from(this).setLength(1).setText(R.string.try_now).setBig(true);

        this.registerReceiver(downloadFinishedReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        apply();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(downloadFinishedReceiver);
        } catch (Exception ignore) {
        }
        stopProgressChecker();
        super.onDestroy();
    }

    public void apply() {
        if (isDestroyed())
            return;
        tv_new_version.setText(String.format("%s%s", getString(R.string.new_version), blankUpdateObject.versionName));
        tv_current_version.setText(String.format("%s%s", getString(R.string.current_version), BuildConfig.VERSION_NAME));
        tv_change_log.setText(blankUpdateObject.changeLog);
        final SharedPreferences sharedPreferences = BPrefs.get(this);
        final int downloadedVersion = BPrefs.get(this).getInt(BPrefs.LAST_APK_VERSION_KEY, -1);
        final int newVersion = blankUpdateObject.versionCode;
        final boolean downloading = sharedPreferences.contains(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID) && sharedPreferences.contains(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER);
        final boolean downloaded = downloadedVersion == newVersion && getDownloadedFile().exists();
        assert true;
        if (downloaded) {
            pb.setVisibility(View.GONE);
            tv_download_progress.setVisibility(View.GONE);
            bt_re.setVisibility(View.VISIBLE);
            bt_re.setOnClickListener(v -> {
                removeUpdatesInfo(this);
                tryNowToast.show();
                bt_re.setVisibility(View.GONE);
                bt_re.setOnClickListener(null);
                apply();
            });
            bt.setOnClickListener(v -> {
                install();
            });
            bt.setText(R.string.install);
        } else if (downloading) {
            bt.setText(R.string.downloading);
            bt.setOnClickListener(D.EMPTY_CLICK_LISTENER);
            pb.setVisibility(View.VISIBLE);
            tv_download_progress.setVisibility(View.VISIBLE);
            startProgressChecker();
        } else {
            pb.setVisibility(View.GONE);
            tv_download_progress.setVisibility(View.GONE);
            bt.setText(R.string.download);
            bt.setOnClickListener(v -> {
                final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (networkInfo != null) {
                        if (networkInfo.isConnected()) {
                            onDownloadButtonClick(newVersion);
                        } else {
                            BDB.from(this)
                                    .setTitle(R.string.data_warning)
                                    .setSubText(R.string.data_warning_subtext)
                                    .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                                    .setPositiveButtonListener(params -> {
                                        onDownloadButtonClick(newVersion);
                                        return true;
                                    })
                                    .show();
                        }
                    } else {
                        notConnectedToast.show();
                    }
                } else {
                    notConnectedToast.show();
                }
            });
        }
    }

    private void onDownloadButtonClick(final int newVersion) {
        if (downloadApk(newVersion)) {
            apply();
        } else {
            couldNotStartDownloadToast.show();
        }
    }

    /**
     * @param versionNumber version number
     * @return true if download was started;
     */
    public boolean downloadApk(final int versionNumber) {
        if (manager == null)
            return false;

        downloadingToast.show();
        deleteCurrentUpdateFile();

        final Uri uri =
                Uri.parse(blankUpdateObject.apkUrl);
        final DownloadManager.Request request =
                new DownloadManager.Request(uri)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FILENAME)
                        .setAllowedOverRoaming(true)
                        .setAllowedOverMetered(true)
                        .setDescription(getText(R.string.downloading_updates));

        downloadId = manager.enqueue(request);

        BPrefs.get(this).edit().putLong(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID, downloadId).apply();
        BPrefs.get(this).edit().putInt(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_VERSION_NUMBER, versionNumber).apply();
        apply();
        return true;
    }

    /**
     * @return true if progress was checked successfully
     */

    private boolean checkProgress() {
        final Cursor cursor = manager.query(new DownloadManager.Query());
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        if (downloadId == -1)
            downloadId = BPrefs.get(this).getLong(BPrefs.LAST_DOWNLOAD_MANAGER_REQUEST_ID, -1);
        if (downloadId == -1)
            return false;
        do {
            final long reference = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            if (reference == downloadId) {
                final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL)
                    return true;
                final long progress = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                final long progressMax = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                final int intProgress = (int) ((progress * 100.0) / ((double) progressMax));
                pb.setProgress(intProgress);
                return true;
            }

        } while (cursor.moveToNext());
        cursor.close();

        return false;
    }

    private void startProgressChecker() {
        if (!isProgressCheckerRunning) {
            progressChecker.run();
            isProgressCheckerRunning = true;
        }
    }

    private void stopProgressChecker() {
        handler.removeCallbacks(progressChecker);
        isProgressCheckerRunning = false;
    }

    public void install() {
        final File downloadedFile = getDownloadedFile();
        final Uri apkUri = S.fileToUriCompat(downloadedFile, this);
        final Intent intent =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                        new Intent(Intent.ACTION_INSTALL_PACKAGE)
                                .setData(apkUri)
                                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) :
                        new Intent(Intent.ACTION_VIEW)
                                .setDataAndType(apkUri, "application/vnd.android.package-archive")
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void deleteCurrentUpdateFile() {
        final File bp = getDownloadedFile();
        if (bp.exists()) {
            if (bp.delete()) {
                MediaScannerConnection.scanFile(this, new String[]{bp.getAbsolutePath()}, null, null);
            } else
                downloadedFileCouldNotBeDeletedToast.show();

        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_REQUEST_INSTALL_PACKAGES | PERMISSION_WRITE_EXTERNAL_STORAGE;
    }
}