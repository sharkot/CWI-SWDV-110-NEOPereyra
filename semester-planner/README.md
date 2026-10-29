# Semester Planner

SWDV-110 Dates & Times project, using Java 17 and Maven.

The program builds the practice Spring 2027 schedule for a Monday/Wednesday
class in Boise. It skips holidays and spring break, shows the first class in
four time zones, and finds the next class and lab due date from a date entered
by the user. These dates are from the assignment, not the official CWI calendar.

## Running it

From this folder, with Java 17 and Maven installed:

```sh
mvn test
mvn -q compile exec:java
```

Dates can be entered as `2027-03-15`, `3/15/2027`, or `Mar 15, 2027`.
Type `quit` to stop. Bad input prints an example of the accepted formats.

The schedule has 30 meetings, 7 days off, and 75 hours of class time.
A lab assigned March 15 is due March 29 because weekends and spring break
do not count toward the five school days. School days include weekdays
without class, as long as they are not days off.

## Reflection

### Why use java.time instead of Date and Calendar?

Date and Calendar objects can be changed after they are created, which can
cause mistakes when the same object is used in different places. The older
API also has confusing numbering: Calendar months start at zero, and older
Date constructors count years from 1900. java.time uses immutable objects
and clearer types like LocalDate and LocalTime. The old classes still exist
for older programs.

### When should Period and Duration be used?

Period is for calendar amounts, such as months and days. The semester uses
Period to show its length as 3 months and 26 days. Duration is for clock time.
Each class lasts 2 hours and 30 minutes, so the program multiplies that Duration
by the number of meetings to get 75 hours.

### Why do Boise and Phoenix match in January but differ in April?

In January, both cities use Mountain Standard Time, UTC-7. Boise moves to
daylight saving time in the spring and becomes UTC-6. Phoenix stays at UTC-7,
so the same class appears one hour earlier there in April. Using the city ZoneIds
lets Java handle the change based on the date.

### Why not use ZonedDateTime for everything?

A holiday or class date only needs a year, month, and day, so LocalDate is
enough. A time and zone would add information that is not needed for counting
school days. The online class times need ZonedDateTime because they describe
the same moment in different places. withZoneSameInstant keeps that moment
while changing the displayed time. withZoneSameLocal would keep the clock
time and change when the class actually happens.

## Tests

The two provided JUnit test files are included without changes. All 15 tests
pass. The sample interactions were also checked, including the March 15 and
March 22 lookups, a date after the semester, and invalid dates.
