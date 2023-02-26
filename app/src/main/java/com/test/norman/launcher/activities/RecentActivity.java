 

package com.test.norman.launcher.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.adapters.CallsRecyclerViewAdapter;
import com.test.norman.launcher.databases.calls.CallLogsHelper;

public class RecentActivity extends blankActivity {
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions(this, requiredPermissions()))
            return;

        setContentView(R.layout.activity_recent);

        recyclerView = findViewById(R.id.recycler_view);
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.ll_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        final CallsRecyclerViewAdapter callsRecyclerViewAdapter = new CallsRecyclerViewAdapter(CallLogsHelper.getAllCalls(getContentResolver()), this);
        CallLogsHelper.markAllAsRead(getContentResolver());
        recyclerView.setAdapter(callsRecyclerViewAdapter);

        setupYoutube(3);
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_WRITE_CALL_LOG | PERMISSION_READ_CONTACTS;
    }
}
