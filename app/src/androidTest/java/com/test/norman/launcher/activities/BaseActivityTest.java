 

package com.test.norman.launcher.activities;

import com.test.norman.launcher.utils.BPrefs;

import org.junit.After;
import org.junit.Before;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class BaseActivityTest {
    @Before
    public void setUp() {
        BPrefs
                .get(getInstrumentation().getTargetContext())
                .edit()
                .putBoolean(BPrefs.TEST_KEY, true)
                .putBoolean(BPrefs.VIBRATION_FEEDBACK_KEY, true)
                .putBoolean(BPrefs.LONG_PRESSES_KEY, true)
                .putBoolean(BPrefs.LONG_PRESSES_SHORTER_KEY, false)
                .putBoolean(BPrefs.TOUCH_NOT_HARD_KEY, false)
                .commit();
    }

    @After
    public void after() {
        BPrefs
                .get(getInstrumentation().getTargetContext())
                .edit()
                .clear()
                .commit();
    }

    public void sleep() {
        try {
            Thread.sleep(50);
            getInstrumentation().waitForIdleSync();
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
