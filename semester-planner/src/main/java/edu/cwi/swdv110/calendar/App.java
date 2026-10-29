package edu.cwi.swdv110.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Interactive planner for the assignment's practice semester. */
public class App {
    static final ZoneId CAMPUS = ZoneId.of("America/Boise");
    private static final int LAB_SCHOOL_DAYS = 5;

    public static void main(String[] args) {
        // Practice dates, not the official CWI academic calendar.
        Semester sem = new Semester("SWDV-110 – Spring 2027",
                LocalDate.of(2027, 1, 11), LocalDate.of(2027, 5, 7),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                LocalTime.of(18, 0), LocalTime.of(20, 30));
        sem.addHoliday(DateTools.nthWeekday(2027, Month.JANUARY, 3, DayOfWeek.MONDAY));
        sem.addHoliday(DateTools.nthWeekday(2027, Month.FEBRUARY, 3, DayOfWeek.MONDAY));
        sem.addBreak(LocalDate.of(2027, 3, 22), LocalDate.of(2027, 3, 26));

        System.out.println(sem.getName());
        System.out.println("=".repeat(sem.getName().length()));
        List<LocalDate> meetings = sem.meetingDates();
        int previousWeek = 0;
        for (LocalDate date : meetings) {
            int week = sem.weekNumber(date);
            if (week != previousWeek) {
                System.out.print("Week " + week + " ");
            }
            System.out.println(DateTools.formatShort(date));
            previousWeek = week;
        }

        Duration total = sem.totalClassTime();
        Period length = sem.length();
        System.out.println("\nDays off: " + sem.getDaysOff().size());
        System.out.println("Meetings: " + meetings.size());
        System.out.printf("Class time: %dh %dm%n", total.toHours(), total.toMinutesPart());
        System.out.printf("Length: %d months, %d days%n", length.toTotalMonths(), length.getDays());

        if (!meetings.isEmpty()) {
            ZonedDateTime first = ZonedDateTime.of(meetings.get(0), sem.getClassStart(), CAMPUS);
            System.out.println("\nFirst class, for online students:");
            for (String zone : List.of("America/Boise", "America/Los_Angeles",
                    "America/New_York", "Asia/Tokyo")) {
                System.out.println(zone + " " + DateTools.inZone(first, ZoneId.of(zone)));
            }
        }

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nEnter a date (or 'quit'): ");
                if (!sc.hasNextLine()) {
                    break;
                }
                String text = sc.nextLine().trim();
                if (text.equalsIgnoreCase("quit")) {
                    break;
                }
                try {
                    LocalDate from = DateTools.parseFlexible(text);
                    LocalDate next = sem.nextMeeting(from);
                    if (next == null) {
                        System.out.println("No more classes – enjoy your summer!");
                        continue;
                    }
                    System.out.printf("Next class: %s (week %d)%n",
                            DateTools.formatShort(next), sem.weekNumber(next));
                    System.out.println("Days until the semester ends: " + sem.daysLeft(from));
                    LocalDate due = DateTools.addSchoolDays(next, LAB_SCHOOL_DAYS, sem.getDaysOff());
                    System.out.println("A lab assigned that day is due " + DateTools.formatShort(due));
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage()
                            + " – try 2027-03-15, 3/15/2027 or Mar 15, 2027");
                }
            }
        }
    }
}
