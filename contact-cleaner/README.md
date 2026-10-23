# Contact List Cleaner

A small Java project for SWDV-110. It reads the messy contacts in `contacts.txt`, cleans names and phone numbers, checks emails, and prints a report. After the report, you can search for a name and see a masked email.

## How to run

From inside the `contact-cleaner` folder:

```bash
mvn test
mvn -q compile exec:java
```

Type a name (or part of a name) to search, or type `quit` to finish.

## Part 6: Reflection

**1. Where could forgetting that strings are immutable cause a bug?**

When I lowercase an email, I have to keep the returned string from `toLowerCase()`. If I just called the method without using its result, the email would stay the same because strings don't change in place.

**2. Why is `split("|")` different from `split("\|")`?**

`split()` uses regular expressions, and `|` means OR in a regex, not a normal pipe character. The extra backslash escapes the pipe, so Java splits at the actual `|` between the name, email, and phone number.

**3. Where did I use `StringBuilder` instead of `+`, and why?**

I used it to collect phone digits, change capital letters in names, and build the final report. It makes sense when I'm adding or changing lots of characters, instead of creating a new string every time.

**4. What did `mvn test` output?**

The code passed 23 local test checks, including all 20 provided tests and 3 extra tests. Those checks used a small Java assertion harness because Maven was not installed in the preparation environment; GitHub Actions runs the actual Maven tests.
