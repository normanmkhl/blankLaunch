 

package com.test.norman.launcher.screenshots;

import android.content.Intent;
import android.os.Handler;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.contacts.ContactsActivity;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;

import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactsActivityScreenshot extends BaseScreenshotTakerTest<ContactsActivity> {

    public void test() {
        mActivityTestRule.launchActivity(new Intent());
        getInstrumentation().waitForIdleSync();
        new Handler(mActivityTestRule.getActivity().getMainLooper())
                .post(() -> {
                    final ContactsActivity dis = mActivityTestRule.getActivity();
                    final String[] names = dis.getResources().getStringArray(R.array.names_for_screenshots);
                    for (final String name : names) {
                        new ContactsSaverBuilder(dis).saveContact(ContactDataFactory.createEmpty().setCompositeName(name));
                    }
                    dis.applyFilter();

                });
        getInstrumentation().waitForIdleSync();

    }

    @Override
    protected void cleanupAfterTest() {
        super.cleanupAfterTest();
        TestUtils.deleteAllContactsInEmulator(getInstrumentation().getTargetContext().getApplicationContext());
    }

    @Override
    protected Class<ContactsActivity> activity() {
        return ContactsActivity.class;
    }

}
