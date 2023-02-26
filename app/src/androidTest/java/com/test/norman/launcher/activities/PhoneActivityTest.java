 

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PhoneActivityTest extends BaseActivityTest {
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
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void phoneActivityTest() {
        mActivityTestRule.launchActivity(new Intent());
        ViewInteraction firstPageAppIcon = onView(allOf(withId(R.id.bt_dialer), childAtPosition(allOf(withId(R.id.phone_container), childAtPosition(withId(R.id.page1), 2)), 2), isDisplayed()));
        sleep();
        firstPageAppIcon.perform(longClick());
        ViewInteraction blankButton = onView(allOf(withId(R.id.b_0), withText("0"), childAtPosition(allOf(withId(R.id.include), childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 2)), 10), isDisplayed()));
        sleep();
        blankButton.perform(longClick());
        ViewInteraction blankButton2 = onView(allOf(withId(R.id.b_5), withText("5"), childAtPosition(allOf(withId(R.id.include), childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 2)), 4), isDisplayed()));
        sleep();
        blankButton2.perform(longClick());
        ViewInteraction blankButton3 = onView(allOf(withId(R.id.b_4), withText("4"), childAtPosition(allOf(withId(R.id.include), childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 2)), 3), isDisplayed()));
        sleep();
        blankButton3.perform(longClick());
        ViewInteraction blankLinearLayoutButton = onView(allOf(withId(R.id.empty_view), childAtPosition(allOf(withId(R.id.scrolling_helper), childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")), 3)), 2), isDisplayed()));
        sleep();
        blankLinearLayoutButton.perform(longClick());
        ViewInteraction blankEditText = onView(allOf(withId(R.id.et_name), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1), isDisplayed()));
        sleep();
        blankEditText.perform(replaceText("v"), closeSoftKeyboard());
        ViewInteraction blankButton4 = onView(allOf(withId(R.id.save), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 8), isDisplayed()));
        sleep();
        blankButton4.perform(longClick());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction blankLinearLayoutButton2 = onView(allOf(withId(R.id.bt_edit), childAtPosition(allOf(withId(R.id.options_bar), childAtPosition(withId(R.id.ll_info), 0)), 0), isDisplayed()));
        sleep();
        blankLinearLayoutButton2.perform(longClick());
        ViewInteraction blankEditText2 = onView(allOf(withId(R.id.et_mail), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 7), isDisplayed()));
        sleep();
        blankEditText2.perform(replaceText("hh"), closeSoftKeyboard());
        ViewInteraction blankButton5 = onView(allOf(withId(R.id.save), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 8), isDisplayed()));
        sleep();
        blankButton5.perform(longClick());
        sleep();
        ViewInteraction blankLinearLayoutButton3 = onView(allOf(withId(R.id.bt_delete), childAtPosition(allOf(withId(R.id.options_bar), childAtPosition(withId(R.id.ll_info), 0)), 2), isDisplayed()));
        sleep();
        blankLinearLayoutButton3.perform(longClick());
        ViewInteraction blankButton6 = onView(allOf(withId(R.id.dialog_box_true), childAtPosition(childAtPosition(withId(R.id.container), 1), 0), isDisplayed()));
        sleep();
        blankButton6.perform(longClick());
        pressBack();
    }
}
