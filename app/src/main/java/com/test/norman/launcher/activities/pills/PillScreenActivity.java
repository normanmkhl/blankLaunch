 

package com.test.norman.launcher.activities.pills;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.TimedblankActivity;
import com.test.norman.launcher.databases.reminders.Reminder;
import com.test.norman.launcher.databases.reminders.ReminderScheduler;
import com.test.norman.launcher.databases.reminders.RemindersDatabase;
import com.test.norman.launcher.utils.Animations;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.D;
import com.test.norman.launcher.utils.S;

public class PillScreenActivity extends TimedblankActivity {
    private static final String TAG = PillScreenActivity.class.getSimpleName();

    private static final int TIME_DELAYED_SCHEDULE = 100;

    private TextView tv_textual_content, snooze, took;
    private ImageView iv_pill;
    private Ringtone ringtone;
    private Reminder reminder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        S.logImportant("reminderScreen was called!");
        setContentView(R.layout.reminder_screen);

        attachXml();

        final Intent intent = getIntent();
        if (intent == null) throw new AssertionError();
        int key = intent.getIntExtra(Reminder.REMINDER_KEY_VIA_INTENTS, -1);
        if (key == -1) throw new AssertionError();
        reminder = RemindersDatabase.getInstance(this).remindersDatabaseDao().getById(key);
        if (reminder == null) {
            S.logImportant("reminder == null!, returning");
            return;
        }

        final String textual_content = reminder.getTextualContent();
        if (textual_content == null) tv_textual_content.setVisibility(View.GONE);
        else tv_textual_content.setText(textual_content);

        if (reminder.getBinaryContentType() == Reminder.BINARY_RGB) {
            final Drawable drawable = getDrawable(R.drawable.pill).mutate();
            drawable.setTint(Color.rgb(reminder.getBinaryContent()[0] & 0xFF, reminder.getBinaryContent()[1] & 0xFF, reminder.getBinaryContent()[2] & 0xFF));
            iv_pill.setImageDrawable(drawable);

        }

        took.setOnClickListener(v -> {
            if (vibrator != null)
                vibrator.vibrate(D.vibetime);
            finish();
        });
        took.setOnLongClickListener(v -> {
            if (vibrator != null)
                vibrator.vibrate(D.vibetime);
            finish();
            return true;
        });

        snooze.setOnClickListener((v) -> snooze());
        snooze.setOnLongClickListener((v) -> {
            snooze();
            return true;
        });

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            blankToast.error(this);
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        Animations.makeBiggerAndSmaller(this, iv_pill, null);
        scheduleNextReminder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ringtone != null)
            ringtone.play();
    }

    @Override
    protected void onStop() {
        if (ringtone != null)
            ringtone.stop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (ringtone != null)
            ringtone.stop();
        super.onDestroy();
    }

    private void attachXml() {
        tv_textual_content = findViewById(R.id.textual_content);
        took = findViewById(R.id.took);
        snooze = findViewById(R.id.snooze);
        iv_pill = findViewById(R.id.iv_pill);
    }

    private void snooze() {
        if (vibrator != null)
            vibrator.vibrate(D.vibetime);
        ReminderScheduler.scheduleSnooze(reminder, this);
        finish();
    }

    private void scheduleNextReminder() {
        new Handler().postDelayed(() -> ReminderScheduler.scheduleReminder(reminder, this), TIME_DELAYED_SCHEDULE);
    }

    @Override
    public void onBackPressed() {
        snooze();
        super.onBackPressed();
    }

    @Override
    protected int screenTimeout() {
        return D.MINUTE * 2;
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_SYSTEM_ALERT_WINDOW;
    }
}
