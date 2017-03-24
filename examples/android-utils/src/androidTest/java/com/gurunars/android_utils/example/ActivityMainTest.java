package com.gurunars.android_utils.example;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityMainTest {

    @Rule
    public ActivityTestRule<ActivityMain> mActivityRule = new ActivityTestRule<ActivityMain>(
            ActivityMain.class);

    private void validateText(String text) {
        mActivityRule.getActivity().finish();
        mActivityRule.launchActivity(null);
        onView(withId(R.id.item)).check(matches(withText(text)));
    }

    @Test
    public void clickingButtons_shouldNotProduceExceptions() {
        onView(withId(R.id.clear)).perform(click());
        onView(withId(R.id.disabled)).perform(click());
        validateText("Empty");
        onView(withId(R.id.set)).perform(click());
        validateText("Configured");
        onView(withId(R.id.clear)).perform(click());
        validateText("Empty");
    }

}