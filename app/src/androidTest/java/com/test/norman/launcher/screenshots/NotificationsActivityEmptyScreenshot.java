 

package com.test.norman.launcher.screenshots;

import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.activities.NotificationsActivity;
import com.test.norman.launcher.views.ModularRecyclerView;

import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NotificationsActivityEmptyScreenshot extends BaseScreenshotTakerTest<NotificationsActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        mActivityTestRule.getActivity().runOnUiThread(() -> mActivityTestRule.getActivity().recyclerView.setAdapter(ModularRecyclerView.ModularAdapter.EMPTY_ADAPTER));
    }

    @Override
    protected Class<NotificationsActivity> activity() {
        return NotificationsActivity.class;
    }
}
