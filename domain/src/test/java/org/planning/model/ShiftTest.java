package org.planning.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;

import static org.assertj.core.api.Assertions.assertThat;

class ShiftTest {

    @Test
    void test_Of() {
        Instant date = Instant.now();
        String workerId = "john.doe";

        for (DayShift dayShift : DayShift.values()) {
            Shift shift = Shift.of(date, dayShift, workerId);
            assertThat(shift).isNotNull();
            assertThat(shift.getId()).isEqualTo(dayShift.startTimeOf(date).getEpochSecond());
            assertThat(shift.getStartTime()).isEqualTo(dayShift.startTimeOf(date));
            assertThat(shift.getWorkerId()).isEqualTo(workerId);
        }
    }

}