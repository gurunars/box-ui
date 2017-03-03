package com.gurunars.leaflet_view.example;

import android.content.pm.ActivityInfo;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityMainTest {

    @Rule
    public ActivityTestRule<ActivityMain> mActivityRule = new ActivityTestRule<>(
            ActivityMain.class);

    private void createPage(String title) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Create page")).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).perform(replaceText(title));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void checkTitle(String text) {
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).check(matches(withText(text)));
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText(text)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void initialOpen_shouldShowEmptyView() {
        checkTitle("Empty");
    }

    @Test
    public void creatingBlankNewPage_shouldNavigateToIt() {
        createPage("Page title");
        checkTitle("Page title");
    }

    @Test
    public void creatingNewPage_shouldNotNavigateAnywhereToIt() {
        createPage("Page title");
        createPage("aaa");
        createPage("zzz");
        checkTitle("Page title");
    }

    @Test
    public void deletingTheOnlyPage_shouldShowEmptyView() {
        createPage("Page title");
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Delete page")).perform(click());
        checkTitle("Empty");
    }

    @Test
    public void editingThePage_shouldChangeIt() {
        createPage("Page title");
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Edit page")).perform(click());
        onView(withId(R.id.pageTitle)).perform(replaceText("Edited title"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.pageTitle)).check(matches(withText("Edited title")));
        checkTitle("Edited title");

    }

    private void goToPage(String title) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Go to page")).perform(click());
        onView(withText(title)).perform(click());
    }

    @Test
    public void goingToPage_shouldNavigateToIt() {
        createPage("Page title 1");
        createPage("Page title 2");
        createPage("Page title 3");
        goToPage("Page title 3");
        checkTitle("Page title 3");
    }

    private void restart() {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void enteredEditTextInPages_shouldBeRetainedAfterPageDestruction() {
        createPage("Page title 1");
        createPage("Page title 2");
        createPage("Page title 3");
        createPage("Page title 4");
        createPage("Page title 5");
        createPage("Page title 6");
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).perform(replaceText("Text to retain"));
        goToPage("Page title 6");
        // Should be empty on page 6
        onView(allOf(hasSibling(withText("Page title 6")), withId(R.id.textEdit))).check(matches(withText("")));
        goToPage("Page title 1");
        // Should have retained entered title
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).check(matches(withText("Text to retain")));
    }

    @Test
    public void enteredEditTextInPages_shouldBeRetainedAfterOrientationChange() {
        createPage("Page title 1");
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).perform(replaceText("Text to retain"));
        restart();
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).check(matches(withText("Text to retain")));
    }

    @Before
    public void clear() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Clear")).perform(click());
    }

}