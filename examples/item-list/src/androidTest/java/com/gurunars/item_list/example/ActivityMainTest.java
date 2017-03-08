package com.gurunars.item_list.example;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.gurunars.test_utils.Helpers.nthChildOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityMainTest {

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
            onView(nthChildOf(withId(R.id.recyclerView), i)).check(matches(withText(expectedItems[i])));
        }
    }

    @Test
    public void deletingItems_shouldLeadToPartialRemoval() {
        clickMenu("Delete items");
        assertList(
            "#0{TIGER @ 0}",
            "#2{MONKEY @ 0}"
        );
    }

    @Test
    public void createItems_shouldAppendItemsToTheEnd() {
        clickMenu("Create items");
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}",
            "#4{TIGER @ 0}",
            "#5{WOLF @ 0}",
            "#6{MONKEY @ 0}",
            "#7{LION @ 0}"
        );
    }

    @Test
    public void updateItems_shouldChangeSomeOfItems() {
        clickMenu("Update items");
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 1}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 1}"
        );
    }

    @Test
    public void moveUp_shouldPutItemFromBottomToTop() {
        clickMenu("Move up");
        assertList(
            "#3{LION @ 0}",
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}"
        );
    }

    @Test
    public void moveDown_shouldPutItemFromTopToBottom() {
        clickMenu("Move down");
        assertList(
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}",
            "#0{TIGER @ 0}"
        );
    }

    @Test
    public void resetItems_shouldSetItemsToInitialList() {
        clickMenu("Reset items");
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}"
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