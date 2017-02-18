package com.gurunars.item_list.example;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    private void clickMenu(String text) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(text)).perform(click());
    }

    @Test
    public void clickingClear_shouldShowEmptyListView() {
        clickMenu("Clear");
        onView(withText("Empty")).check(matches(isDisplayed()));
    }

    private void assertList(String...expectedItems) {
        for (int i=0; i < expectedItems.length; i++) {
            nthChildOf(withId(R.id.itemList), i).matches(
                    hasDescendant(withText(expectedItems[i])));
        }
    }

    @Test
    public void deletingItems_shouldLeadToPartialRemoval() {
        clickMenu("Delete items");
        assertList(
            "0 @ 0 [tiger]",
            "2 @ 0 [monkey]"
        );
    }

    @Test
    public void createItems_shouldAppendItemsToTheEnd() {
        clickMenu("Create items");
        assertList(
            "0 @ 0 [tiger]",
            "1 @ 0 [wolf]",
            "2 @ 0 [monkey]",
            "3 @ 0 [lion]",
            "4 @ 0 [tiger]",
            "5 @ 0 [wolf]",
            "6 @ 0 [monkey]",
            "7 @ 0 [lion]"
        );
    }

    @Test
    public void updateItems_shouldChangeSomeOfItems() {
        clickMenu("Update items");
        assertList(
            "0 @ 0 [tiger]",
            "1 @ 1 [wolf]",
            "2 @ 0 [monkey]",
            "3 @ 1 [lion]"
        );
    }

    @Test
    public void moveUp_shouldPutItemFromBottomToTop() {
        clickMenu("Move up");
        assertList(
            "3 @ 0 [lion]",
            "0 @ 0 [tiger]",
            "1 @ 0 [wolf]",
            "2 @ 0 [monkey]"
        );
    }

    @Test
    public void moveDown_shouldPutItemFromTopToBottom() {
        clickMenu("Move down");
        assertList(
            "1 @ 0 [wolf]",
            "2 @ 0 [monkey]",
            "3 @ 0 [lion]",
            "0 @ 0 [tiger]"
        );
    }

    @Test
    public void resetItems_shouldSetItemsToInitialList() {
        clickMenu("Reset items");
        assertList(
            "0 @ 0 [tiger]",
            "1 @ 0 [wolf]",
            "2 @ 0 [monkey]",
            "3 @ 0 [lion]"
        );
    }

    @Before
    public void before() {
        clickMenu("Reset items");
    }

    @After
    public void after() {
        clickMenu("Reset items");
    }

}