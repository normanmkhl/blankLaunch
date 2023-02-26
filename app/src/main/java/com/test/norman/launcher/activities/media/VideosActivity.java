 

package com.test.norman.launcher.activities.media;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.Constants;
import com.test.norman.launcher.utils.S;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

/**
 * Most of this class is defined at {@link MediaScrollingActivity},
 * The Constants used are defined at {@link Constants.VideosConstants}
 */
public class VideosActivity extends MediaScrollingActivity implements Constants.VideosConstants {
    private static final String TAG = VideosActivity.class.getSimpleName();
    private Size videoThumbnailSize;
    private RequestOptions requestOptions;
    private ContentResolver contentResolver;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    @Override
    protected void setupBeforeAdapter() {
        options.inSampleSize = 1;

        requestOptions = new RequestOptions()
                .override(width)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.error_on_background)
                .lock();
        videoThumbnailSize = new Size(width, width);
        contentResolver = getContentResolver();
    }

    @Override
    protected CharSequence title() {
        return getString(R.string.videos);
    }

    @Override
    protected Cursor cursor(ContentResolver contentResolver) {
        return contentResolver.query(VIDEOS_URI, PROJECTION,
                null,
                null,
                SORT_ORDER
        );
    }

    @Override
    protected Class<? extends SingleMediaActivity> singleActivity() {
        return SingleVideoActivity.class;
    }

    @Override
    protected void bindViewHolder(Cursor cursor, MediaRecyclerViewAdapter.ViewHolder holder) {
        if (!S.isValidContextForGlide(holder.itemView.getContext()))
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final int fieldIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            final long id = cursor.getLong(fieldIndex);
            final Uri imageUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            try {
                Bitmap thumbnailBitmap = getContentResolver().loadThumbnail(imageUri, videoThumbnailSize, null);
                Glide.with(holder.pic)
                        .load(thumbnailBitmap)
                        .apply(requestOptions)
                        .into(holder.pic);

            } catch (IOException e) {
                Log.e(TAG, S.str(e.getMessage()));
                e.printStackTrace();
            }
        } else {
            final long vidId = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            Glide.with(holder.pic)
                    .load(MediaStore.Video.Thumbnails.getThumbnail(contentResolver,
                            vidId,
                            MediaStore.Video.Thumbnails.MICRO_KIND,
                            options))
                    .apply(requestOptions)
                    .into(holder.pic);
        }
    }

    @Override
    protected Uri getData(Cursor cursor) {
        return Uri.parse("file://" + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
    }
}
