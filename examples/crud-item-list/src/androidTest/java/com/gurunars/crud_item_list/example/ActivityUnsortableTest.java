package com.gurunars.crud_item_list.example;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.gurunars.crud_item_list.example.NthChildOf.nthChildOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityUnsortableTest {

    private void validateEnabled(int id) {
        onView(withId(id)).check(matches(isEnabled()));
    }

    private void validateInvisible(int id) {
        onView(withId(id)).check(matches(not(isDisplayed())));
    }

    @Rule
    public ActivityTestRule<ActivityMain> mActivityRule = new ActivityTestRule<>(
            ActivityMain.class);

    @Before
    public void before() {
        onView(withId(R.id.reset)).perform(click());
        onView(withId(R.id.lock)).perform(click());
    }

    @After
    public void after() {
        onView(withId(R.id.reset)).perform(click());
    }

    private ViewInteraction atIndex(int id) {
        return onView(nthChildOf(withId(R.id.recyclerView), id));
    }

    @Test
    public void whenUnselectable_contextualMenuShould() {
        atIndex(3).perform(longClick());
        validateInvisible(R.id.moveUp);
        validateInvisible(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void removingAllItems_shouldShowSpecialLayout() {
        atIndex(3).perform(longClick());
        onView(withId(R.id.selectAll)).perform(click());
        onView(withId(R.id.delete)).perform(click());
        onView(withId(R.id.noItemsLabel)).check(matches(withText("No items at all")));
    }

}