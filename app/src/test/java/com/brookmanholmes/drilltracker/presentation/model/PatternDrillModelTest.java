package com.brookmanholmes.drilltracker.presentation.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PatternDrillModelTest {

    @Test
    public void testDecodePattern() {
        List<PatternEntry> expected = new ArrayList<>();
        expected.add(new PatternEntry(3));
        expected.add(new PatternEntry(2));
        expected.add(new PatternEntry(1));
        expected.add(new PatternEntry(10));
        expected.add(new PatternEntry(9));

        DrillModel.AttemptModel attemptModel = PatternDrillModel.createAttempt(3, expected);

        List<PatternEntry> actual = PatternDrillModel.createPatternEntryListFromExtras(attemptModel.extras);

        Assert.assertEquals(expected, actual);
    }
}