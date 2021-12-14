package org.planning.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.planning.model.DayShift;
import org.planning.model.Shift;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcShiftRepositoryTest extends JdbcTestContext {

    private JdbcShiftRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("delete from shifts where id <> 1639324800");
        repository = new JdbcShiftRepository(jdbcTemplate);
    }

    @Test
    void testFindBy_givenPersistedShift() {
        Optional<Shift> shiftOp = repository.findBy("test.user", 1639324800L);
        assertThat(shiftOp).isNotEmpty();
        assertThat(shiftOp.get().getStartTime()).isNotNull();
    }

    @Test
    void testAddShift() {
        Shift shift = Shift.of(Instant.now(), DayShift.FIRST, "test.user");

        //given
        assertThat(repository.findBy(shift.getWorkerId(), shift.getId())).isEmpty();

        Shift persistedShift = repository.save(shift);
        assertThat(persistedShift).isEqualTo(shift);
        Optional<Shift> shiftOp = repository.findBy(shift.getWorkerId(), shift.getId());
        assertThat(shiftOp).isNotEmpty();
        assertThat(shiftOp.get()).isEqualTo(shift);
    }

    @Test
    void testFindShifts_given2Shifts_thenFindBoth() {
        Shift[]  persistedShifts = given2Shifts();
        Shift shift1 = persistedShifts[0];
        Shift shift2 = persistedShifts[1];

        Instant from = shift1.getStartTime().truncatedTo(ChronoUnit.DAYS);
        Instant to = shift2.getStartTime().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);

        Collection<Shift> shifts = repository.findShifts(from, to);
        assertThat(shifts).containsOnly(shift1, shift2);
    }


    @Test
    void testFindShifts_given2WorkerShifts_thenFindBoth() {
        Shift[]  persistedShifts = given2Shifts();
        Shift shift1 = persistedShifts[0];
        Shift shift2 = persistedShifts[1];

        Instant from = shift1.getStartTime().truncatedTo(ChronoUnit.DAYS);
        Instant to = shift2.getStartTime().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);

        Collection<Shift> shifts = repository.findShifts(shift1.getWorkerId(), from, to);
        assertThat(shifts).containsOnly(shift1, shift2);
    }

    @Test
    void testFindShifts_given2WorkerShifts_thenFindFirst() {
        Shift[]  persistedShifts = given2Shifts();
        Shift shift1 = persistedShifts[0];

        Instant from = shift1.getStartTime().truncatedTo(ChronoUnit.DAYS);
        Instant to = from.plus(1, ChronoUnit.DAYS);
        Collection<Shift> shifts = repository.findShifts(shift1.getWorkerId(), from, to);
        assertThat(shifts).containsOnly(shift1);
    }

    @Test
    void testFindShifts_given2WorkerShifts_thenFindSecond() {
        Shift[]  persistedShifts = given2Shifts();
        Shift shift2 = persistedShifts[1];

        Instant from = shift2.getStartTime().truncatedTo(ChronoUnit.DAYS);
        Instant to = from.plus(1, ChronoUnit.DAYS);
        Collection<Shift> shifts = repository.findShifts(shift2.getWorkerId(), from, to);
        assertThat(shifts).containsOnly(shift2);
    }

    @Test
    void testDeleteShift() {
        Instant date = Instant.now().plus(1, ChronoUnit.DAYS);
        Shift shift = Shift.of(Instant.now(), DayShift.FIRST, "test.user");
        repository.save(shift);

        //given
        assertThat(repository.findBy(shift.getWorkerId(), shift.getId())).isNotEmpty();

        repository.delete(shift.getWorkerId(), shift.getId());

        Optional<Shift> shiftOp = repository.findBy(shift.getWorkerId(), shift.getId());
        assertThat(shiftOp).isEmpty();
    }


    private Shift[] given2Shifts() {
        Instant date = Instant.now().plus(1, ChronoUnit.DAYS);
        Shift shift1 = Shift.of(date, DayShift.FIRST, "test.user");
        Shift shift2 = Shift.of(date.plus(1, ChronoUnit.DAYS), DayShift.FIRST, "test.user");

        //given
        assertThat(repository.findBy(shift1.getWorkerId(), shift2.getId())).isEmpty();
        repository.save(shift1);
        repository.save(shift2);

        return new Shift[] {shift1, shift2};
    }

}