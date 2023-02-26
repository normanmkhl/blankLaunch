 

package com.test.norman.launcher.activities;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.adapters.HelpRecyclerViewAdapter;
import com.test.norman.launcher.utils.S;

public class VideoTutorialsActivity extends blankActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        final RecyclerView recycler_view = findViewById(R.id.recycler_view);
        final DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.ll_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);
        final Resources resources = getResources();
        recycler_view.setAdapter(
                new HelpRecyclerViewAdapter(
                        this,
                        S.typedArrayToResArray(resources, R.array.yt_texts),
                        S.typedArrayToResArray(resources, R.array.yt_logos)));
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}
