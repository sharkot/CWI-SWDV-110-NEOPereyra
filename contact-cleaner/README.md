# Contact List Cleaner

The tutoring center's contact list was kinda a mess, so this fixes the names, emails, and phone numbers. It also makes a report and lets you search names without showing full emails.

## Running it
Open a terminal in this folder and use:

```bash
mvn test
mvn -q compile exec:java
```

## Reflection

**1. Strings are immutable. Where could that cause a bug?**

When I use `toLowerCase()` on emails, I have to save the result. Otherwise it still has capital letters since Java strings don't change themselves.

**2. Why `split("|")` vs. `split("\|")`?**

The plain pipe means OR in a regex, so it splits things wrong. Escaping it makes Java split at the actual `|` in the file.

**3. Why use `StringBuilder`?**

I used it for the phone digits and the report. It's easier than a ton of `+` signs everywhere lol.

**4. Test results**

I ran `mvn test` through GitHub Actions. All 23 tests passed, so we're good 😅

```text
Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
