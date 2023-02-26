 

package com.test.norman.launcher.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.blankPrefsUtils;

public class Page1EditorActivity extends blankActivity {
    public blankPrefsUtils blankPrefsUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_1_editor);
        blankPrefsUtils = blankPrefsUtils.newInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (blankPrefsUtils.hasChanged(this)) {
            recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppsActivity.REQUEST_SELECT_CUSTOM_APP && resultCode == RESULT_OK && data != null && data.getComponent() != null) {
            BPrefs.get(this).edit().putString(data.getStringExtra(AppsActivity.CHOOSE_MODE), data.getComponent().flattenToString()).apply();
        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}
