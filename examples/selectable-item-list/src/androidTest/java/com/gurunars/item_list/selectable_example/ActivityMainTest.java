package com.gurunars.item_list.selectable_example;

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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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

    private void assertList(String...expectedItems) {
        for (int i=0; i < expectedItems.length; i++) {
            onView(nthChildOf(withId(R.id.recyclerView), i)).check(matches(withText(expectedItems[i])));
        }
    }

    private void selectTwo() {
        onView(nthChildOf(withId(R.id.recyclerView), 0)).perform(longClick());
        onView(nthChildOf(withId(R.id.recyclerView), 1)).perform(click());
    }

    @Test
    public void longClickingOneAndClickingAnother_shouldSelectTwoItems() {
        selectTwo();
        assertList(
            "#0{TIGER @ 0|true}",
            "#1{WOLF @ 0|true}",
            "#2{MONKEY @ 0|false}",
            "#3{LION @ 0|false}"
        );
    }

    @Test
    public void deletingSelected_shouldRemoveTheItems() {
        selectTwo();
        clickMenu("Delete selected");
        assertList(
                "#2{MONKEY @ 0|false}",
                "#3{LION @ 0|false}"
        );
    }

    @Test
    public void updatingSelected_shouldIncrementCount() {
        selectTwo();
        clickMenu("Update selected");
        assertList(
                "#0{TIGER @ 1|true}",
                "#1{WOLF @ 1|true}",
                "#2{MONKEY @ 0|false}",
                "#3{LION @ 0|false}"
        );
    }

    @Test
    public void creatingItems_shouldNotUnselectItems() {
        selectTwo();
        clickMenu("Create items");
        assertList(
                "#0{TIGER @ 0|true}",
                "#1{WOLF @ 0|true}",
                "#2{MONKEY @ 0|false}",
                "#3{LION @ 0|false}",
                "#4{TIGER @ 0|false}",
                "#5{WOLF @ 0|false}",
                "#6{MONKEY @ 0|false}",
                "#7{LION @ 0|false}"
        );
    }

    @Test
    public void clickingClearSelection_shouldDropSelection() {
        selectTwo();
        clickMenu("Clear selection");
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