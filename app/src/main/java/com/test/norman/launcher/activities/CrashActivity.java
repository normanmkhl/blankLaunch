 

package com.test.norman.launcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.D;

public class CrashActivity extends blankActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        new Handler().postDelayed(
                () -> {
                    startActivity(new Intent(this, HomeScreenActivity.class));
                    finish();
                },
                4 * D.SECOND);
    }

    @Override
    public void onBackPressed() {
        //Nope
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }

}