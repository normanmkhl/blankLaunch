 

package com.test.norman.launcher.activities.media;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.Constants;
import com.test.norman.launcher.utils.S;
import com.bumptech.glide.Glide;

/**
 * Most of this class is defined at {@link SingleMediaActivity}
 * The Constants used are defined at {@link Constants.PhotosConstants}
 */
public class SinglePhotoActivity extends SingleMediaActivity implements Constants.PhotosConstants {
    private static final String TAG = SinglePhotoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupYoutube(8);
    }

    @Override
    protected MediaPagerAdapter mediaPagerAdapter() {
        return new PhotoPagerAdapter(this);
    }

    @Override
    protected CharSequence title() {
        return getText(R.string.photo);
    }

    private static class PhotoPagerAdapter extends MediaPagerAdapter {
        private static final Uri EXTERNAL = MediaStore.Files.getContentUri("external");

        public PhotoPagerAdapter(SingleMediaActivity activity) {
            super(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void deletePost29(Activity activity) throws SecurityException {
            final int id =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri deleteUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            activity.getContentResolver().delete(
                    deleteUri,
                    null,
                    null);
        }

        @Override
        protected void delete(Activity activity) {
            final int id =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri deleteUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            activity.getContentResolver().delete(
                    EXTERNAL,
                    MediaStore.MediaColumns.DATA + "=?",
                    new String[]{getPath(deleteUri)});
        }

        private String getPath(Uri uri) {
            try (Cursor cursor = getContentResolver().query(uri, PROJECTION, null, null, null)) {
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }

        @Override
        protected Intent share(Activity activity, Cursor cursor) {
            final int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            final Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            return new Intent(Intent.ACTION_SEND)
                    .setTypeAndNormalize("image/*")
                    .putExtra(Intent.EXTRA_STREAM, uri);
        }

        @Override
        protected Cursor cursor(Context context) {
            return context.getContentResolver().query(IMAGES_URI,
                    PROJECTION,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_MODIFIED + " DESC"        // Ordering
            );
        }

        @Override
        protected void bindView(View view, Cursor cursor, Context context) {
            final ImageView pic = view.findViewById(R.id.pic);
            final int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            final long id = cursor.getLong(fieldIndex);
            final Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            if (S.isValidContextForGlide(pic.getContext()))
                Glide.with(pic).load(imageUri).into(pic);
        }

        @Override
        protected View getView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.fragment_image, null, false);
        }
    }
}
