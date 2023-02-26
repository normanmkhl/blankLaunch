 

package com.test.norman.launcher.activities.media;

import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pools;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.blankActivity;
import com.test.norman.launcher.adapters.blankViewAdapter;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.S;
import com.test.norman.launcher.views.blankTitleBar;
import com.test.norman.launcher.views.ViewPagerHolder;

/**
 * Parent activity for {@link SinglePhotoActivity} and {@link SingleVideoActivity}.
 * has all of their commons in here.
 */
public abstract class SingleMediaActivity extends blankActivity {
    private static final String TAG = SingleMediaActivity.class.getSimpleName();
    public static final String MEDIA_KEY = "picKey";
    public static final int SHOULD_REFRESH = 0xDEAD;
    public static final int ALLOW_PHOTO_CODE = 347;
    protected ViewPagerHolder viewPagerHolder;
    private View delete, share, more;
    private LinearLayout optionsBar;
    private blankTitleBar blankTitleBar;
    private MediaPagerAdapter mediaPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions(this, requiredPermissions()))
            return;
        setContentView(R.layout.single_media_activity);
        attachXml();

        final CharSequence title = title();
        blankTitleBar.setTitle(title);
        viewPagerHolder.setItemType(title);

        mediaPagerAdapter = mediaPagerAdapter();
        viewPagerHolder.setViewPagerAdapter(mediaPagerAdapter);

        genListeners();

        final int mediaKey = getIntent().getIntExtra(MEDIA_KEY, -1);
        if (mediaKey == -1)
            throw new AssertionError(TAG + " must have a media key!");
        viewPagerHolder.setCurrentItem(mediaKey);

    }

    protected abstract MediaPagerAdapter mediaPagerAdapter();

    protected abstract CharSequence title();

    private void attachXml() {
        blankTitleBar = findViewById(R.id.blank_title_bar);
        viewPagerHolder = findViewById(R.id.view_pager_holder);
        more = findViewById(R.id.more);
        optionsBar = findViewById(R.id.options_bar);
        delete = optionsBar.findViewById(R.id.delete);
        share = optionsBar.findViewById(R.id.share);
    }

    protected void genListeners() {
        delete.setOnClickListener((v) ->
                mediaPagerAdapter.delete(viewPagerHolder.getPageIndex()));
        share.setOnClickListener((v) ->
                S.share(this, mediaPagerAdapter.share(viewPagerHolder.getPageIndex())));
        more.setOnClickListener((more) -> {
            more.setVisibility(View.GONE);
            optionsBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALLOW_PHOTO_CODE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mediaPagerAdapter.deletePost29(this);
                this.setResult(SHOULD_REFRESH);
                this.finish();
            }
        }
    }

    public abstract static class MediaPagerAdapter extends blankViewAdapter {
        private final SingleMediaActivity activity;
        protected final Cursor cursor;
        private final Pools.SimplePool<View> pool = new Pools.SimplePool<>(4);

        public MediaPagerAdapter(SingleMediaActivity activity) {
            this.activity = activity;
            this.cursor = cursor(activity);
        }

        protected abstract void delete(Activity activity);

        @RequiresApi(api = Build.VERSION_CODES.Q)
        protected abstract void deletePost29(Activity activity) throws SecurityException;

        private void delete(int position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cursor.moveToPosition(position);
                try {
                    deletePost29(activity);
                } catch (RecoverableSecurityException e) {
                    Log.e(TAG, S.str(e.getMessage()));
                    e.printStackTrace();
                    try {
                        activity.startIntentSenderForResult(
                                e.getUserAction().getActionIntent().getIntentSender(),
                                ALLOW_PHOTO_CODE,
                                null,
                                0,
                                0,
                                0);
                    } catch (IntentSender.SendIntentException ex) {
                        Log.e(TAG, S.str(ex.getMessage()));
                        ex.printStackTrace();
                        blankToast.error(activity);
                    }
                }
            } else {
                S.showAreYouSureYouWantToDelete(String.valueOf(activity.title()), activity, () -> {
                    cursor.moveToPosition(position);
                    delete(activity);
                    activity.setResult(SHOULD_REFRESH);
                    activity.finish();
                });

            }

        }

        protected abstract Intent share(Activity activity, Cursor cursor);

        private Intent share(int position) {
            cursor.moveToPosition(position);
            return share(activity, cursor);
        }

        protected abstract Cursor cursor(Context context);

        protected abstract void bindView(View view, Cursor cursor, Context context);

        protected abstract View getView(Context context);

        protected ContentResolver getContentResolver() {
            return activity.getContentResolver();
        }

        @Override
        public View getItem(int position) {
            View v = pool.acquire();
            if (v == null)
                v = getView(activity);
            cursor.moveToPosition(position);
            bindView(v, cursor, activity);
            return v;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_WRITE_EXTERNAL_STORAGE;
    }
}
