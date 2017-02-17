package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class MoverUpTest {

    private final MoverUp<String> moverUp = new MoverUp<>();

    private final List<String> all = Arrays.asList(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten"
    );
    private final Set<String> selected = Sets.newSet(
            "four",
            "five",
            "six",
            "seven"
    );
    private final List<String> expectedOutcome = Arrays.asList(
            "one",
            "two",
            "four",
            "five",
            "six",
            "seven",
            "three", // item moved down
            "eight",
            "nine",
            "ten"
    );

    @Test
    public void moveUp_isCorrect() throws Exception {
        assertEquals(expectedOutcome, moverUp.apply(all, selected));
    }

    @Test
    public void moveUpAll_noChanges() throws Exception {
        assertEquals(all, moverUp.apply(all, new HashSet<>(all)));
    }

}
