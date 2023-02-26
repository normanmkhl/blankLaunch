 

package com.test.norman.launcher.activities.media;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.Constants;
import com.test.norman.launcher.utils.Toggeler;

/**
 * Most of this class is defined at {@link SingleMediaActivity},
 * The Constants used are defined at {@link Constants.VideosConstants}
 */
public class SingleVideoActivity extends SingleMediaActivity implements Constants.VideosConstants {
    private VideoPagerAdapter videoPagerAdapter;

    @Override
    protected MediaPagerAdapter mediaPagerAdapter() {
        return (videoPagerAdapter = new VideoPagerAdapter(this));
    }

    @Override
    protected CharSequence title() {
        return getText(R.string.video);
    }

    @Override
    protected void genListeners() {
        super.genListeners();
        viewPagerHolder.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                videoPagerAdapter.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private static class VideoPagerAdapter extends MediaPagerAdapter {
        private static final Uri EXTERNAL = MediaStore.Files.getContentUri("external");
        SparseArray<VideoViewWrapper> availableViews = new SparseArray<>();

        public VideoPagerAdapter(SingleMediaActivity activity) {
            super(activity);
        }

        @Override
        protected void delete(Activity activity) {
            final int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri deleteUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            activity.getContentResolver().delete(
                    EXTERNAL,
                    MediaStore.MediaColumns.DATA + "=?",
                    new String[]{getPath(deleteUri)});

        }

        @Override
        protected void deletePost29(Activity activity) throws SecurityException {
            final int id =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri deleteUri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

            activity.getContentResolver().delete(
                    deleteUri,
                    null,
                    null);
        }

        private String getPath(Uri uri) {
            try (Cursor cursor = getContentResolver().query(uri, PROJECTION, null, null, null)) {
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final VideoViewWrapper videoViewWrapper = (VideoViewWrapper) super.instantiateItem(container, position);
            availableViews.append(position, videoViewWrapper);
            return videoViewWrapper;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            availableViews.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        protected Intent share(Activity activity, Cursor cursor) {
            final int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            return new Intent(Intent.ACTION_SEND)
                    .setTypeAndNormalize("image/*")
                    .putExtra(Intent.EXTRA_STREAM, uri);
        }

        @Override
        protected Cursor cursor(Context context) {
            return context.getContentResolver().query(VIDEOS_URI,
                    PROJECTION,
                    null,
                    null,
                    SORT_ORDER
            );
        }

        public void onPageSelected(int position) {
            for (int i = 0; i < availableViews.size(); i++) {
                availableViews.valueAt(i).setShown(availableViews.keyAt(i) == position);
            }
        }

        @Override
        protected void bindView(View v, Cursor cursor, Context context) {
            final VideoView videoView = v.findViewById(R.id.vid);
            final ImageView play_stop = v.findViewById(R.id.play_stop);

            videoView.setOnPreparedListener(mp -> {
                float videoProportion = (float) mp.getVideoWidth() / (float) mp.getVideoHeight();
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) videoView.getLayoutParams();
                lp.dimensionRatio = String.valueOf(videoProportion);
                videoView.setLayoutParams(lp);

            });

            final int fieldIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            final long id = cursor.getLong(fieldIndex);
            final Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.seekTo(1);
            videoView.setOnCompletionListener(mp -> {
                play_stop.setImageResource(R.drawable.replay_on_background);
                Toggeler.newImageToggeler(
                        play_stop,
                        play_stop,
                        new int[]{R.drawable.stop_on_background, R.drawable.play_on_background},
                        new View.OnClickListener[]{
                                view -> videoView.start(),
                                view -> videoView.pause()
                        });
            });

            Toggeler.newImageToggeler(
                    play_stop,
                    play_stop,
                    new int[]{R.drawable.stop_on_background, R.drawable.play_on_background},
                    new View.OnClickListener[]{
                            view -> videoView.start(),
                            view -> videoView.pause()
                    });

            ((VideoViewWrapper) v).setOnShowedChangedListener(shown -> {
                if (shown) {
                    videoView.seekTo(1);
                    Toggeler.newImageToggeler(
                            play_stop,
                            play_stop,
                            new int[]{R.drawable.stop_on_background, R.drawable.play_on_background},
                            new View.OnClickListener[]{
                                    view -> videoView.start(),
                                    view -> videoView.pause()
                            });
                    play_stop.setImageResource(R.drawable.play_on_background);
                    videoView.start();
                    videoView.pause();
                } else
                    videoView.pause();
            });

        }

        @Override
        protected View getView(Context context) {
            return new VideoViewWrapper(LayoutInflater.from(context).inflate(R.layout.fragment_video, null, false));
        }

        private static class VideoViewWrapper extends FrameLayout {

            private OnShowedChangedListener onShowedChangedListener;

            public VideoViewWrapper(@NonNull View view) {
                super(view.getContext());
                addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            public void setOnShowedChangedListener(OnShowedChangedListener onShowedChangedListener) {
                this.onShowedChangedListener = onShowedChangedListener;
            }

            public void setShown(boolean shown) {
                if (onShowedChangedListener != null)
                    onShowedChangedListener.shownChanged(shown);
            }

            private interface OnShowedChangedListener {
                void shownChanged(boolean shown);
            }

        }

    }
}

