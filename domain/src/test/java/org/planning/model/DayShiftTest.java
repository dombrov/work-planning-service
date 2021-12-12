package org.planning.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DayShiftTest {

    @Test
    void startTimeOf_FIRST() {
        Instant date = Instant.now();

        Instant startDateTime = DayShift.FIRST.startTimeOf(date);
        assertThat(startDateTime).isNotNull();
        assertThat(startDateTime).isEqualTo(date.truncatedTo(ChronoUnit.DAYS));
    }

    @Test
    void startTimeOf_SECOND() {
        Instant date = Instant.now();

        Instant startDateTime = DayShift.SECOND.startTimeOf(date);
        assertThat(startDateTime).isNotNull();
        assertThat(startDateTime).isEqualTo(date.truncatedTo(ChronoUnit.DAYS).plus(8, ChronoUnit.HOURS));
    }
    @Test
    void startTimeOf_THIRD() {
        Instant date = Instant.now();

        Instant startDateTime = DayShift.THIRD.startTimeOf(date);
        assertThat(startDateTime).isNotNull();
        assertThat(startDateTime).isEqualTo(date.truncatedTo(ChronoUnit.DAYS).plus(16, ChronoUnit.HOURS));
    }

}