package org.planning.model;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public enum DayShift {
    FIRST(0),
    SECOND(1),
    THIRD(2);

    public static final int SHIFT_DURATION = 8;
    private final int index;

    DayShift(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Instant startTimeOf(Instant date) {
        return date.truncatedTo(ChronoUnit.DAYS).plus(Duration.ofHours(index * SHIFT_DURATION));
    }
}
