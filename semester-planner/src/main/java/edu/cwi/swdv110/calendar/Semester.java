package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** One course's inclusive date range, meeting schedule, and days off. */
public class Semester {
    private final String name;
    private final LocalDate start;
    private final LocalDate end;
    private final Set<DayOfWeek> meetingDays;
    private final LocalTime classStart;
    private final LocalTime classEnd;
    private final Set<LocalDate> daysOff = new TreeSet<>();

    public Semester(String name, LocalDate start, LocalDate end, Set<DayOfWeek> meetingDays,
                    LocalTime classStart, LocalTime classEnd) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Semester end must not be before start");
        }
        if (!classEnd.isAfter(classStart)) {
            throw new IllegalArgumentException("Class end must be after class start");
        }
        this.name = name;
        this.start = start;
        this.end = end;
        this.meetingDays = Set.copyOf(meetingDays);
        this.classStart = classStart;
        this.classEnd = classEnd;
    }

    public String getName() { return name; }
    public LocalDate getStart() { return start; }
    public LocalDate getEnd() { return end; }
    public LocalTime getClassStart() { return classStart; }
    public Set<LocalDate> getDaysOff() { return Collections.unmodifiableSet(daysOff); }

    public void addHoliday(LocalDate date) {
        daysOff.add(date);
    }

    public void addBreak(LocalDate first, LocalDate last) {
        if (last.isBefore(first)) {
            throw new IllegalArgumentException("Break end must not be before start");
        }
        for (LocalDate date = first; !date.isAfter(last); date = date.plusDays(1)) {
            daysOff.add(date);
        }
    }

    public List<LocalDate> meetingDates() {
        List<LocalDate> meetings = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (meetingDays.contains(date.getDayOfWeek()) && !daysOff.contains(date)) {
                meetings.add(date);
            }
        }
        return meetings;
    }

    public int weekNumber(LocalDate date) {
        LocalDate firstMonday = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return Math.toIntExact(ChronoUnit.WEEKS.between(firstMonday, monday) + 1);
    }

    public Duration classLength() { return Duration.between(classStart, classEnd); }
    public Duration totalClassTime() { return classLength().multipliedBy(meetingDates().size()); }

    public LocalDate nextMeeting(LocalDate from) {
        for (LocalDate date : meetingDates()) {
            if (!date.isBefore(from)) {
                return date;
            }
        }
        return null;
    }

    public Period length() { return Period.between(start, end); }
    public long daysLeft(LocalDate from) { return Math.max(0, ChronoUnit.DAYS.between(from, end)); }
}
