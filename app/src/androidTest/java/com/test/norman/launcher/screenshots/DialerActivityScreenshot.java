 

package com.test.norman.launcher.screenshots;

import android.content.Intent;
import android.os.Handler;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.DialerActivity;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DialerActivityScreenshot extends BaseScreenshotTakerTest<DialerActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
        new Handler(mActivityTestRule.getActivity().getMainLooper())
                .post(() -> mActivityTestRule.getActivity().setNumber("0594508160"));
        getInstrumentation().waitForIdleSync();

    }

    @Override
    protected Class<DialerActivity> activity() {
        return DialerActivity.class;
    }
}
