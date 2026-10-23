package edu.cwi.swdv110.contacts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ContactParserTest {

// ---------- Part 1: names ----------
@Test
void titleCaseSimpleWord() {
assertEquals("John", ContactParser.titleCase("jOHN"));
}

@Test
void titleCaseHyphenatedWord() {
assertEquals("Garcia-Lopez", ContactParser.titleCase("garcia-LOPEZ"));
}

@Test
void titleCaseEmpty() {
assertEquals("", ContactParser.titleCase(""));
}

@Test
void normalizeNameReordersLastFirst() {
assertEquals("John Smith", ContactParser.normalizeName(" smith, JOHN "));
}

@Test
void normalizeNameMultiWordLastName() {
assertEquals("Ana De La Cruz", ContactParser.normalizeName("DE LA CRUZ, ana"));
}

@Test
void normalizeNameCollapsesSpaces() {
assertEquals("Maria Garcia-Lopez", ContactParser.normalizeName("maria garcia-lopez"));
}

@Test
void normalizeNameBlank() {
assertEquals("", ContactParser.normalizeName(" "));
}

// ---------- Part 2: emails ----------
@Test
void validEmails() {
assertTrue(ContactParser.isValidEmail("JSmith@Example.COM"));
assertTrue(ContactParser.isValidEmail(" maria.g@college.edu "));
assertTrue(ContactParser.isValidEmail("a@b.co"));
}

@Test
void invalidEmails() {
assertFalse(ContactParser.isValidEmail("bob@"), "nothing after @");
assertFalse(ContactParser.isValidEmail("@example.com"), "nothing before @");
assertFalse(ContactParser.isValidEmail("a@b"), "no dot in domain");
assertFalse(ContactParser.isValidEmail("minh tran@example.com"), "space");
assertFalse(ContactParser.isValidEmail("a@@b.com"), "two @");
assertFalse(ContactParser.isValidEmail("a@b..com"), "double dot");
assertFalse(ContactParser.isValidEmail("a@.com"), "dot first");
assertFalse(ContactParser.isValidEmail("a@com."), "dot last");
}

@Test
void normalizeEmail() {
assertEquals("jsmith@example.com", ContactParser.normalizeEmail(" JSmith@Example.COM "));
}

@Test
void maskEmail() {
assertEquals("j*****@example.com", ContactParser.maskEmail("jsmith@example.com"));
assertEquals("a@b.co", ContactParser.maskEmail("a@b.co"));
}

// ---------- Part 3: phones ----------
@Test
void formatPhoneVariousStyles() {
assertEquals("(555) 123-4567", ContactParser.formatPhone("555.123.4567"));
assertEquals("(208) 555-0199", ContactParser.formatPhone("(208) 555-0199"));
assertEquals("(208) 555-0175", ContactParser.formatPhone("208 555 0175"));
assertEquals("(208) 555-0111", ContactParser.formatPhone("2085550111"));
}

@Test
void formatPhoneDropsLeadingOne() {
assertEquals("(208) 555-0142", ContactParser.formatPhone("1-208-555-0142"));
assertEquals("(986) 555-0123", ContactParser.formatPhone("+1 (986) 555-0123"));
}

@Test
void formatPhoneInvalid() {
assertEquals("INVALID", ContactParser.formatPhone("555-0100"));
assertEquals("INVALID", ContactParser.formatPhone("call me"));
assertEquals("INVALID", ContactParser.formatPhone("22085550111"));
}

// ---------- Part 4: usernames & lines ----------
@Test
void makeUsername() {
assertEquals("jsmith", ContactParser.makeUsername("John Smith"));
assertEquals("mgarcial", ContactParser.makeUsername("Maria Garcia-Lopez"));
assertEquals("acruz", ContactParser.makeUsername("Ana De La Cruz"));
assertEquals("poneil", ContactParser.makeUsername("Pat O'neil"));
assertEquals("bob", ContactParser.makeUsername("Bob"));
}

@Test
void parseLineCleansEveryField() {
Contact c = ContactParser.parseLine(" smith, JOHN | JSmith@Example.COM | 555.123.4567");
assertEquals(new Contact("John Smith", "jsmith@example.com", "(555) 123-4567"), c);
}

@Test
void parseLineKeepsBadPhoneAsInvalid() {
Contact c = ContactParser.parseLine("bob | bob@ | 555-0100");
assertEquals("INVALID", c.phone());
assertEquals("bob@", c.email());
}

@Test
void parseLineRejectsWrongFieldCount() {
assertThrows(IllegalArgumentException.class,
() -> ContactParser.parseLine("this line is broken"));
}

@Test
void parseLineRejectsBlankName() {
assertThrows(IllegalArgumentException.class,
() -> ContactParser.parseLine(" | nobody@example.com | 208-555-0000"));
}

// ---------- Part 5: report ----------
@Test
void reportHasHeaderRowsAndTotals() {
List<Contact> list = List.of(
new Contact("John Smith", "jsmith@example.com", "(555) 123-4567"),
new Contact("Bob", "bob@", "INVALID"));
String report = ContactReport.buildReport(list);

assertTrue(report.startsWith("NAME"), "report should start with the header");
assertTrue(report.contains("-".repeat(78)));
assertTrue(report.contains(String.format("%-22s %-30s %-15s %-9s",
"John Smith", "jsmith@example.com", "(555) 123-4567", "jsmith")));
assertTrue(report.contains("!! bob@"), "invalid emails are flagged with !!");
assertTrue(report.contains("Total contacts: 2 | Invalid emails: 1 | Invalid phones: 1"));
}
}
