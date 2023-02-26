 

package com.test.norman.launcher.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.blankToast;

import org.acra.ACRA;

public class FeedbackActivity extends blankActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        final EditText et_feedback = findViewById(R.id.et_feedback);
        findViewById(R.id.bt_send).setOnClickListener(v -> {
            final CharSequence text = et_feedback.getText();
            if (text.length() == 0)
                blankToast.from(v.getContext()).setType(blankToast.TYPE_ERROR).setText(R.string.feedback_cannot_be_empty).show();
            else {
                ACRA.getErrorReporter().handleSilentException(new FeedbackException(String.valueOf(text)));
                blankToast.from(getApplicationContext()).setText(R.string.feedback_sent_successfully).show();
                finish();
            }
        });
    }

    private final static class FeedbackException extends Exception {
        FeedbackException(String message) {
            super(message);
        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}
