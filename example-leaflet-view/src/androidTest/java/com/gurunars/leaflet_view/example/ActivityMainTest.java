package com.gurunars.leaflet_view.example;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityMainTest {

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("position " + childPosition + " of parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) return false;
                ViewGroup parent = (ViewGroup) view.getParent();

                return parentMatcher.matches(parent)
                        && parent.getChildCount() > childPosition
                        && parent.getChildAt(childPosition).equals(view);
            }
        };
    }

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
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText(text)));
    }

    private void ensureEmpty() {
        onView(withId(R.id.noPageText)).check(matches(withText("Empty")));
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Empty")));
    }

    @Test
    public void initialOpen_shouldShowEmptyView() {
        ensureEmpty();
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
        ensureEmpty();
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

    @Test
    public void goingToPage_shouldNavigateToIt() {
        createPage("Page title 1");
        createPage("Page title 2");
        createPage("Page title 3");
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Go to page")).perform(click());
        onView(withText("Page title 1")).perform(click());
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Page title 1")));
    }

    @Before
    public void clear() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Clear")).perform(click());
    }

}