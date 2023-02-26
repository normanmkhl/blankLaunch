 

package com.test.norman.launcher.activities;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.test.norman.launcher.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SOSActivityTest extends BaseActivityTest {
    @Rule
    public ActivityTestRule<HomeScreenActivity> mActivityTestRule = new ActivityTestRule<>(HomeScreenActivity.class, true, false);

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void sOSActivityTest() {
        mActivityTestRule.launchActivity(new Intent());
        ViewInteraction blankImageButton2 = onView(allOf(withId(R.id.sos), childAtPosition(allOf(withId(R.id.top_bar), childAtPosition(withId(R.id.container), 0)), 0), isDisplayed()));
        sleep();
        blankImageButton2.perform(longClick());
        ViewInteraction blankLinearLayoutButton = onView(allOf(withId(R.id.ec1), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2), isDisplayed()));
        sleep();
        blankLinearLayoutButton.perform(longClick());
        pressBack();
        pressBack();
        sleep();
    }
}
