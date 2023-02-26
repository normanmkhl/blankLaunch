 

package com.test.norman.launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.blankActivity;
import com.test.norman.launcher.activities.UpdatesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * On Different class than {@link UpdatesActivity} because may be exported to library in the future
 */
public class UpdatingUtil {
    public static final String FILENAME = "blankLaunchUpdate.apk";
    public static final String VOLLEY_TAG = "blankLaunch";

    @NonNull
    public static File getDownloadedFile() {
        final File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        downloads.mkdir();
        return new File(downloads.getAbsoluteFile() + "/" + FILENAME);
    }

    private static boolean isOnline(Context context) {
        final ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private static boolean updatePending(@NonNull blankUpdateObject blankUpdateObject) {
        return blankUpdateObject.versionCode > BuildConfig.VERSION_CODE;
    }



    public static class blankUpdateObject implements Parcelable {
        public static final Creator<blankUpdateObject> CREATOR = new Creator<blankUpdateObject>() {
            @Override
            public blankUpdateObject createFromParcel(Parcel in) {
                return new blankUpdateObject(in);
            }

            @Override
            public blankUpdateObject[] newArray(int size) {
                return new blankUpdateObject[size];
            }
        };
        public final int versionCode;
        public final String versionName;
        public final String changeLog;
        public final String apkUrl;

        public blankUpdateObject(int versionCode, String versionName, String changeLog, String apkUrl) {
            this.versionCode = versionCode;
            this.versionName = versionName;
            this.changeLog = changeLog;
            this.apkUrl = apkUrl;
        }

        protected blankUpdateObject(Parcel in) {
            versionCode = in.readInt();
            versionName = in.readString();
            changeLog = in.readString();
            apkUrl = in.readString();
        }

        public static blankUpdateObject parseMessage(String json) throws JSONException {
            final JSONObject root = new JSONObject(json);
            final int versionNumber = Integer.parseInt(root.getString("tag_name"));
            final String versionName = root.getString("name");

            final JSONArray assets = root.getJSONArray("assets");
            final JSONObject apkObject = assets.getJSONObject(0);
            if (!apkObject.getString("content_type").equals("application/vnd.android.package-archive"))
                throw new JSONException("first object in assets array is not an apk file!");
            final String apkDownloadUrl = apkObject.getString("browser_download_url");
            final String changeLog = root.getString("body");

            return new blankUpdateObject(
                    versionNumber,
                    versionName,
                    changeLog,
                    apkDownloadUrl
            );
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(versionCode);
            dest.writeString(versionName);
            dest.writeString(changeLog);
            dest.writeString(apkUrl);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}