 

package com.test.norman.launcher.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;

/**
 * A simple holder to {@link com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments.TutorialFragment3}
 */
public class KeyboardChangerActivity extends blankActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_changer);
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_WRITE_SETTINGS;
    }
}
