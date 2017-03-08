package com.gurunars.crud_item_list.example;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewInteraction;
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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.gurunars.test_utils.Helpers.nthChildOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivitySortableTest {

    private void restart() {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Rule
    public ActivityTestRule<ActivityMain> mActivityRule = new ActivityTestRule<>(
            ActivityMain.class);

    private void validateEnabled(int id) {
        onView(withId(id)).check(matches(isEnabled()));
    }

    private void validateDisabled(int id) {
        onView(withId(id)).check(matches(not(isEnabled())));
    }

    private void validateDoesNotExist(int id) {
        onView(withId(id)).check(doesNotExist());
    }

    private ViewInteraction atIndex(int id) {
        return onView(nthChildOf(withId(R.id.recyclerView), id));
    }

    @Test
    public void selectingOne_shouldOpenContextualMenu() {
        validateDoesNotExist(R.id.moveUp);
        validateDoesNotExist(R.id.moveDown);
        validateDoesNotExist(R.id.edit);
        validateDoesNotExist(R.id.delete);
        validateDoesNotExist(R.id.selectAll);
        atIndex(1).perform(longClick());
        restart();
        validateEnabled(R.id.moveUp);
        validateEnabled(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void deselectingLast_shouldCloseContextualMenu() {
        atIndex(0).perform(longClick());
        restart();
        atIndex(0).perform(click());
        restart();
        validateDoesNotExist(R.id.moveUp);
        validateDoesNotExist(R.id.moveDown);
        validateDoesNotExist(R.id.edit);
        validateDoesNotExist(R.id.delete);
        validateDoesNotExist(R.id.selectAll);
    }

    @Test
    public void clickingCross_shouldCloseContextualMenu() {
        atIndex(0).perform(longClick());
        restart();
        onView(withId(R.id.openFab)).perform(click());
        restart();
        validateDoesNotExist(R.id.moveUp);
        validateDoesNotExist(R.id.moveDown);
        validateDoesNotExist(R.id.edit);
        validateDoesNotExist(R.id.delete);
        validateDoesNotExist(R.id.selectAll);
    }

    @Test
    public void selectingTopItem_shouldDisableMoveUp() {
        atIndex(0).perform(longClick());
        restart();
        validateDisabled(R.id.moveUp);
        validateEnabled(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void selectingBottomItem_shouldDisableMoveDown() {
        atIndex(3).perform(longClick());
        restart();
        validateEnabled(R.id.moveUp);
        validateDisabled(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void selectingUnsolidChunk_shouldDisableMoveAndEdit() {
        atIndex(0).perform(longClick());
        restart();
        atIndex(3).perform(click());
        restart();
        validateDisabled(R.id.moveUp);
        validateDisabled(R.id.moveDown);
        validateDisabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void selectingSolidChunk_shouldEnableMoveButDisableEdit() {
        atIndex(1).perform(longClick());
        restart();
        atIndex(2).perform(click());
        restart();
        validateEnabled(R.id.moveUp);
        validateEnabled(R.id.moveDown);
        validateDisabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void movingSelectionToTop_shouldDisableMoveUp() {
        atIndex(1).perform(longClick());
        restart();
        onView(withId(R.id.moveUp)).perform(click());
        restart();
        validateDisabled(R.id.moveUp);
        validateEnabled(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void movingSelectionToBottom_shouldDisableMoveDown() {
        atIndex(2).perform(longClick());
        restart();
        onView(withId(R.id.moveDown)).perform(click());
        restart();
        validateEnabled(R.id.moveUp);
        validateDisabled(R.id.moveDown);
        validateEnabled(R.id.edit);
        validateEnabled(R.id.delete);
        validateEnabled(R.id.selectAll);
    }

    @Test
    public void editingItem_shouldIncrementVersion() {
        atIndex(3).perform(longClick());
        restart();
        onView(withId(R.id.edit)).perform(click());
        restart();
        atIndex(3).check(matches(withText("#4{WOLF @ 2|true}")));
    }

    @Test
    public void creatingItem_shouldIncrementItemListSize() {
        onView(withId(R.id.openFab)).perform(click());
        restart();
        onView(withId(R.id.lion)).perform(click());
        restart();
        atIndex(4).check(matches(withText("#5{LION @ 1|false}")));
    }

    @Test
    public void removingAllItems_shouldShowSpecialDefaultLayout() {
        atIndex(1).perform(longClick());
        restart();
        onView(withId(R.id.selectAll)).perform(click());
        onView(withId(R.id.delete)).perform(click());
        onView(withId(R.id.noItemsLabel)).check(matches(withText("No items at all")));
    }

    @Test
    public void leftAndRightHandSwitch() {
        atIndex(3).perform(longClick());
        restart();
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("RIGHT HANDED")));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Left handed")).perform(click());
        restart();
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("LEFT HANDED")));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Right handed")).perform(click());
        restart();
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("RIGHT HANDED")));
    }

    @Test
    public void testSelectMoveUpAndReset() {
        atIndex(1).perform(longClick());
        onView(withId(R.id.moveUp)).perform(click());
        onView(withId(R.id.reset)).perform(click());
        atIndex(0).check(matches(withText("#1{LION @ 1|false}")));
        atIndex(1).check(matches(withText("#2{TIGER @ 1|true}")));
    }

    @Test
    public void testSelectMoveDownAndReset() {
        atIndex(2).perform(longClick());
        onView(withId(R.id.moveDown)).perform(click());
        onView(withId(R.id.reset)).perform(click());
        atIndex(2).check(matches(withText("#3{MONKEY @ 1|true}")));
        atIndex(3).check(matches(withText("#4{WOLF @ 1|false}")));
    }

    @Before
    public void before() {
        onView(withId(R.id.reset)).perform(click());
        onView(withId(R.id.unlock)).perform(click());
    }

    @After
    public void after() {
        onView(withId(R.id.reset)).perform(click());
    }

}