package edu.cwi.swdv110.calendar;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemesterTest {

private Semester sem;

@BeforeEach
void setUp() {
// A short 3-week summer session that meets Tuesday & Thursday, 9:00–11:15
sem = new Semester("Summer", LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 18),
Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
LocalTime.of(9, 0), LocalTime.of(11, 15));
}

@Test
void constructorValidates() {
assertThrows(IllegalArgumentException.class, () -> new Semester("Bad",
LocalDate.of(2027, 6, 18), LocalDate.of(2027, 6, 1),
Set.of(DayOfWeek.MONDAY), LocalTime.of(9, 0), LocalTime.of(10, 0)));
assertThrows(IllegalArgumentException.class, () -> new Semester("Bad",
LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 18),
Set.of(DayOfWeek.MONDAY), LocalTime.of(10, 0), LocalTime.of(9, 0)));
}

@Test
void meetingDatesWithoutDaysOff() {
assertEquals(List.of(
LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 3),
LocalDate.of(2027, 6, 8), LocalDate.of(2027, 6, 10),
LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 17)), sem.meetingDates());
}

@Test
void holidaysAndBreaksAreSkipped() {
sem.addHoliday(LocalDate.of(2027, 6, 3));
sem.addBreak(LocalDate.of(2027, 6, 7), LocalDate.of(2027, 6, 11));
assertEquals(6, sem.getDaysOff().size());
assertEquals(List.of(
LocalDate.of(2027, 6, 1),
LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 17)), sem.meetingDates());
}

@Test
void weekNumbers() {
assertEquals(1, sem.weekNumber(LocalDate.of(2027, 6, 1))); // Tue
assertEquals(1, sem.weekNumber(LocalDate.of(2027, 6, 6))); // Sun – same week
assertEquals(2, sem.weekNumber(LocalDate.of(2027, 6, 7))); // Mon – new week
assertEquals(3, sem.weekNumber(LocalDate.of(2027, 6, 17)));
}

@Test
void durationsAndPeriods() {
assertEquals(Duration.ofMinutes(135), sem.classLength());
assertEquals(Duration.ofMinutes(135 * 6), sem.totalClassTime());
assertEquals(Period.of(0, 0, 17), sem.length());
}

@Test
void nextMeetingAndDaysLeft() {
assertEquals(LocalDate.of(2027, 6, 1), sem.nextMeeting(LocalDate.of(2027, 5, 20)));
assertEquals(LocalDate.of(2027, 6, 10), sem.nextMeeting(LocalDate.of(2027, 6, 10)));
assertEquals(LocalDate.of(2027, 6, 15), sem.nextMeeting(LocalDate.of(2027, 6, 11)));
assertNull(sem.nextMeeting(LocalDate.of(2027, 6, 18)));

assertEquals(8, sem.daysLeft(LocalDate.of(2027, 6, 10)));
assertEquals(0, sem.daysLeft(LocalDate.of(2027, 7, 4)));
}
}
