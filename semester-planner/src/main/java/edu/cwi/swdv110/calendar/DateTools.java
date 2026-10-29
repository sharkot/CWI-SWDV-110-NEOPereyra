package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Static date helpers used by Semester and App. */
public final class DateTools {
    private static final DateTimeFormatter SHORT =
            DateTimeFormatter.ofPattern("EEE, MMM d", Locale.US);
    private static final DateTimeFormatter ZONED =
            DateTimeFormatter.ofPattern("EEE, MMM d 'at' h:mm a z", Locale.US);
    private static final List<DateTimeFormatter> INPUTS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT));
    private static final int PENALTY_PER_DAY = 10;
    private static final int MAX_PENALTY = 50;

    private DateTools() { }

    public static LocalDate nthWeekday(int year, Month month, int n, DayOfWeek day) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.dayOfWeekInMonth(n, day));
    }

    public static LocalDate lastWeekday(int year, Month month, DayOfWeek day) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastInMonth(day));
    }

    public static String formatShort(LocalDate date) {
        return SHORT.format(date);
    }

    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static LocalDate parseFlexible(String text) {
        if (text != null) {
            for (DateTimeFormatter formatter : INPUTS) {
                try {
                    return LocalDate.parse(text.trim(), formatter);
                } catch (DateTimeParseException ex) {
                    // Try the next accepted input format.
                }
            }
        }
        throw new IllegalArgumentException("Unrecognized date: " + text);
    }

    /** Counts forward, excluding start and skipping weekends and holidays. */
    public static LocalDate addSchoolDays(LocalDate start, int days, Set<LocalDate> holidays) {
        if (days < 0) {
            throw new IllegalArgumentException("School days must be nonnegative");
        }
        LocalDate date = start;
        int counted = 0;
        while (counted < days) {
            date = date.plusDays(1);
            if (!isWeekend(date) && !holidays.contains(date)) {
                counted++;
            }
        }
        return date;
    }

    /** Charges for each started 24-hour interval, including fractional minutes. */
    public static int latePenaltyPercent(LocalDateTime due, LocalDateTime submitted) {
        Duration late = Duration.between(due, submitted);
        if (late.isNegative() || late.isZero()) {
            return 0;
        }
        long daysLate = late.toDays();
        // Any time left over counts as another late day.
        if (!late.equals(Duration.ofDays(daysLate))) {
            daysLate++;
        }
        return (int) Math.min(MAX_PENALTY, daysLate * PENALTY_PER_DAY);
    }

    public static String inZone(ZonedDateTime when, ZoneId zone) {
        return ZONED.format(when.withZoneSameInstant(zone));
    }
}
