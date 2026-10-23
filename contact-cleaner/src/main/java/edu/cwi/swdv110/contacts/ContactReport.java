package edu.cwi.swdv110.contacts;

import java.util.List;

/** Makes a neatly aligned report of all the cleaned contacts. */
public final class ContactReport {

    private static final String ROW = "%-22s %-30s %-15s %-9s%n";

    private ContactReport() { }

    public static String buildReport(List<Contact> contacts) {
        StringBuilder report = new StringBuilder();
        int badEmails = 0;
        int badPhones = 0;
        String line = "-".repeat(78);

        report.append(String.format(ROW, "NAME", "EMAIL", "PHONE", "USERNAME"));
        report.append(line).append(System.lineSeparator());

        for (Contact contact : contacts) {
            String email = contact.email();
            if (!ContactParser.isValidEmail(email)) {
                email = "!! " + email;
                badEmails++;
            }
            if (contact.phone().equals("INVALID")) {
                badPhones++;
            }

            String username = ContactParser.makeUsername(contact.name());
            report.append(String.format(ROW, contact.name(), email, contact.phone(), username));
        }

        report.append(line).append(System.lineSeparator());
        report.append("Total contacts: ").append(contacts.size());
        report.append(" | Invalid emails: ").append(badEmails);
        report.append(" | Invalid phones: ").append(badPhones);
        return report.toString();
    }
}
