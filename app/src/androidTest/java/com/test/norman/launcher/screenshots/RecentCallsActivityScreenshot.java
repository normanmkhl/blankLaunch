 

package com.test.norman.launcher.screenshots;

import android.content.Intent;
import android.os.Handler;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.RecentActivity;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.test.norman.launcher.screenshots.FakeCallsRecyclerViewAdapter.FakeCall;
import static com.test.norman.launcher.screenshots.FakeCallsRecyclerViewAdapter.INCOMING_TYPE;
import static com.test.norman.launcher.screenshots.FakeCallsRecyclerViewAdapter.MISSED_TYPE;
import static com.test.norman.launcher.screenshots.FakeCallsRecyclerViewAdapter.OUTGOING_TYPE;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecentCallsActivityScreenshot extends BaseScreenshotTakerTest<RecentActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
        new Handler(mActivityTestRule.getActivity().getMainLooper())
                .post(() -> {
                    final RecentActivity dis = mActivityTestRule.getActivity();
                    final String[] names = dis.getResources().getStringArray(R.array.names_for_screenshots);
                    final String[] numbers = dis.getResources().getStringArray(R.array.phone_numbers_for_tests);
                    final List<FakeCall> fakeCallList = new ArrayList<>();
                    int[] types = new int[]
                            {INCOMING_TYPE, INCOMING_TYPE, INCOMING_TYPE, OUTGOING_TYPE, OUTGOING_TYPE, OUTGOING_TYPE, INCOMING_TYPE, MISSED_TYPE};
                    int[] actualNames = new int[]
                            {4, 0, 2, 1, 0, 1, 3, 0};
                    int[] minusDays = new int[]
                            {0, 4, 2, 0, 0, 1, 2, 0};
                    int[] minusHours = new int[]
                            {1, 2, 0, 0, 0, 1, 2, 0};
                    int[] minusMinutes = new int[]
                            {23, 12, 7, 34, 2, 2, 35, 7};
                    for (int i = 0; i < types.length; i++)
                        fakeCallList.add(
                                new FakeCall(
                                        types[i],
                                        null,
                                        names[actualNames[i]],
                                        numbers[0],
                                        DateTime.now()
                                                .minusDays(minusDays[i])
                                                .minusHours(minusHours[i])
                                                .minusMinutes(minusMinutes[i])
                                                .getMillis()
                                )
                        );
                    Collections.sort(fakeCallList, (o1, o2) -> Long.compare(o2.dateTime, o1.dateTime));
                    dis.recyclerView.setAdapter(new FakeCallsRecyclerViewAdapter(fakeCallList, dis));
                });
        getInstrumentation().waitForIdleSync();

    }

    @Override
    protected void cleanupAfterTest() {
        super.cleanupAfterTest();
        TestUtils.deleteAllContactsInEmulator(getInstrumentation().getTargetContext().getApplicationContext());
    }

    @Override
    protected Class<RecentActivity> activity() {
        return RecentActivity.class;
    }
}
