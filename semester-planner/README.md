# Semester Planner

SWDV-110 Dates & Times project, using Java 17 and Maven.

A calendar that remembers class dates so your brain can focus on more important
things, like remembering where you put your charger.

The program builds the practice Spring 2027 schedule for a Monday/Wednesday
class in Boise. It skips holidays and spring break, shows the first class in
four time zones, and finds the next class and lab due date from a date you enter.
These dates come from the assignment, not the official CWI calendar.
Please do not use this README as permission to miss class.

## Running it

From this folder, with Java 17 and Maven installed:

```sh
mvn test
mvn -q compile exec:java
```

Enter a date as `2027-03-15`, `3/15/2027`, or `Mar 15, 2027`.
Type `quit` to stop. Unfortunately, this only quits the program, not the semester.

Bad input prints the accepted formats. February 30 gets rejected.
Even Java has to draw the line somewhere.

## What the schedule looks like

- 30 meetings
- 7 days off
- 75 hours of class time
- 3 months and 26 days from the start date to the end date

A lab assigned March 15 is due March 29 because weekends and spring break
do not count toward the five school days. The assignment day does not count
either. School days include weekdays without class, as long as they are not
days off. Spring break gets to do its one job: be a break.

## Reflection

### Why use java.time instead of Date and Calendar?

Date and Calendar objects can be changed after they are created. If the same
object is used in different places, changing it in one place can cause a surprise
somewhere else. Surprise parties are fine. Surprise due dates are less popular.

The older API also has confusing numbering: Calendar months start at zero,
and older Date constructors count years from 1900. January being month zero
feels like the calendar is trying to win an argument.

java.time uses immutable objects and clearer types like LocalDate and LocalTime.
The older classes still exist for compatibility with older programs.

### When should Period and Duration be used?

Period is for calendar amounts, such as months and days. This project uses it
to show the semester length as 3 months and 26 days.

Duration is for clock time. Each class lasts 2 hours and 30 minutes.
Multiplying that by 30 meetings gives 75 hours of class time.
The program measures the scheduled hours; how long the last ten minutes feel
is outside the scope of this assignment.

### Why do Boise and Phoenix match in January but differ in April?

In January, both cities use Mountain Standard Time, UTC-7. In spring, Boise
switches to daylight saving time and becomes UTC-6. Phoenix stays at UTC-7,
so the same class appears one hour earlier there in April.

Boise changes its clocks. Phoenix declines the invitation.
Using the city ZoneIds lets Java handle the rules for the date.

### Why not use ZonedDateTime for everything?

A holiday or class date only needs a year, month, and day, so LocalDate is
enough. A time and zone would add information that is not needed for counting
school days. A holiday does not need a passport.

The online class times need ZonedDateTime because they describe the same
moment in different places. withZoneSameInstant keeps that moment while
changing the displayed time. withZoneSameLocal keeps the clock time and
changes the actual moment, which could make an online student arrive very
confidently at the wrong time.

## Tests

The two provided JUnit test files are included without changes. All 15 tests
pass. The sample interactions were also checked, including the March 15 and
March 22 lookups, a date after the semester, and invalid dates.

The tests confirm the date math. They make no promises about your study habits.
