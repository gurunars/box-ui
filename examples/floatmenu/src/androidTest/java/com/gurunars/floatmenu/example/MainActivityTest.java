package com.gurunars.floatmenu.example;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private void restart() {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private ViewInteraction fab() {
        return onView(withId(R.id.openFab));
    }

    private void checkAlert(String title) {
        onView(withId(R.id.alertTitle)).check(matches(withText(title)));
    }

    private void checkFab(String contentDescription, String menuContentDescription) {
        onView(withId(R.id.floatingMenu)).check(matches(
                withContentDescription(menuContentDescription)));
        fab().check(matches(withContentDescription(contentDescription)));
    }

    @Test
    public void clickingFab_shouldOpenAndCloseMenu() {
        restart();
        checkFab("|BG:-7667712|IC:-1|ACT:false", "LH:false");
        fab().perform(click());
        restart();
        checkFab("|BG:-1|IC:-16777216|ACT:true", "LH:false");
        fab().perform(click());
        restart();
        checkFab("|BG:-7667712|IC:-1|ACT:false", "LH:false");
    }

    @Test
    public void clickingText_shouldShowAlertDialog() {
        onView(withId(R.id.textView)).perform(click());
        checkAlert("Text Clicked");
    }

    @Test
    public void clickingButtonInMenu_shouldWorkAsExpected() {
        restart();
        fab().perform(click());
        restart();
        onView(withId(R.id.button)).perform(click());
        checkAlert("Button Clicked");
        onView(withId(R.id.textView)).check(doesNotExist());
    }

    @Test
    public void togglingMenuDecoration_shouldChangeBackgroundAndForeground() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Toggle button color")).perform(click());
        restart();
        checkFab("|BG:-4419697|IC:-16777216|ACT:false", "LH:false");
    }

    private void toggleBg() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Toggle background")).perform(click());
        fab().perform(click());
    }

    @Test
    public void togglingBackground_shouldMakeBackgroundTranslucent() {
        toggleBg();
        restart();
        onView(withId(R.id.textView)).perform(click());
        checkAlert("Text Clicked");
    }

    @Test
    public void togglingBackground_shouldMakeButtonClickable() {
        toggleBg();
        restart();
        onView(withId(R.id.button)).perform(click());
        checkAlert("Button Clicked");
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void togglingBackground_shouldLeaveButtonBgClickable() {
        toggleBg();
        restart();
        onView(withId(R.id.buttonFrame)).perform(click());
        checkAlert("Button Frame Clicked");
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void togglingLeftHand_shouldChangeTheProperties() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Toggle left/right hand")).perform(click());
        restart();
        checkFab("|BG:-7667712|IC:-1|ACT:false", "LH:true");
    }

}