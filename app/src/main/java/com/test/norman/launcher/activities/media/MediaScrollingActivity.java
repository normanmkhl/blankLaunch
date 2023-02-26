 

package com.test.norman.launcher.activities.media;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.blankActivity;
import com.test.norman.launcher.views.blankTitleBar;
import com.test.norman.launcher.views.ModularRecyclerView;

import static com.test.norman.launcher.activities.media.SingleMediaActivity.SHOULD_REFRESH;

/**
 * Parent activity for {@link VideosActivity} and {@link PhotosActivity}.
 * has all of their commons in here.
 * {@link #REVERSED} can be changed to reverse the Order.
 */
public abstract class MediaScrollingActivity extends blankActivity {
    public final static boolean REVERSED = false;

    protected boolean mediaChoose;
    protected blankTitleBar blankTitleBar;
    protected RecyclerView recyclerView;
    protected Cursor cursor;
    protected MediaRecyclerViewAdapter adapter;
    protected int width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions(this, requiredPermissions()))
            return;
        setContentView(R.layout.activity_media_scrolling);

        final Intent callingIntent = getIntent();
        mediaChoose =
                callingIntent != null &&
                        (Intent.ACTION_GET_CONTENT.equals(callingIntent.getAction()) || Intent.ACTION_PICK.equals(callingIntent.getAction()));

        blankTitleBar = findViewById(R.id.blank_title_bar);
        recyclerView = findViewById(R.id.child);

        blankTitleBar.setTitle(title());

        final Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        final int num = (point.x / point.y) > 0 ? 6 : 3;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, num, RecyclerView.VERTICAL, REVERSED);

        recyclerView.setLayoutManager(gridLayoutManager);
        width = (int) ((recyclerView.getWidth() / 3) - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        cursor = cursor(getContentResolver());

        setupBeforeAdapter();
        adapter = new MediaRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == SHOULD_REFRESH) {
            cursor = cursor(getContentResolver());
            adapter.notifyDataSetChanged();
        }

    }

    protected abstract Cursor cursor(ContentResolver contentResolver);

    protected abstract Class<? extends SingleMediaActivity> singleActivity();

    protected abstract void bindViewHolder(Cursor cursor, MediaRecyclerViewAdapter.ViewHolder viewHolder);

    protected abstract Uri getData(Cursor cursor);

    protected abstract void setupBeforeAdapter();

    protected abstract CharSequence title();

    public class MediaRecyclerViewAdapter extends ModularRecyclerView.ModularAdapter<MediaRecyclerViewAdapter.ViewHolder> {
        private final LayoutInflater layoutInflater;

        public MediaRecyclerViewAdapter() {
            this.layoutInflater = LayoutInflater.from(MediaScrollingActivity.this);

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(layoutInflater.inflate(R.layout.media_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            cursor.moveToPosition(position);
            MediaScrollingActivity.this.bindViewHolder(cursor, holder);
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final ImageView pic;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                pic = (ImageView) ((ViewGroup) itemView).getChildAt(0);//UNSAFE BUT FASTER
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }

            @Override
            public void onClick(View v) {
                if (mediaChoose) {
                    cursor.moveToPosition(getAdapterPosition());
                    final Uri regularUri = getData(cursor);
                    setResult(RESULT_OK, new Intent().setData(regularUri));
                    finish();
                } else {
                    final Intent intent =
                            new Intent(MediaScrollingActivity.this, MediaScrollingActivity.this.singleActivity())
                                    .putExtra(SingleMediaActivity.MEDIA_KEY, this.getAdapterPosition());
                    MediaScrollingActivity.this.startActivityForResult(intent, SHOULD_REFRESH);
                }
            }
        }

    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_WRITE_EXTERNAL_STORAGE;
    }
}
