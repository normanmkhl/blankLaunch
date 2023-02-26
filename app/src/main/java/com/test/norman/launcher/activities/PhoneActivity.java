 

package com.test.norman.launcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.contacts.ContactsActivity;

public class PhoneActivity extends blankActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        findViewById(R.id.bt_contacts).
                setOnClickListener(v -> startActivity(new Intent(this, ContactsActivity.class)));

        findViewById(R.id.bt_dialer).
                setOnClickListener(v -> startActivity(new Intent(this, DialerActivity.class)));

        if (BuildConfig.FLAVOR.equals("gPlay")) {
            findViewById(R.id.bt_recent).setVisibility(View.GONE);
            findViewById(R.id.div_2).setVisibility(View.GONE);
        } else
            findViewById(R.id.bt_recent).
                    setOnClickListener(v -> startActivity(new Intent(this, RecentActivity.class)));

    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}
