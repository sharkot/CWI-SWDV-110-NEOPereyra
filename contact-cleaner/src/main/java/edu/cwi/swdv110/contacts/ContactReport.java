package edu.cwi.swdv110.contacts;

import java.util.List;

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

        for (Contact c : contacts) {
            String email = c.email();

            if (!ContactParser.isValidEmail(email)) {
                email = "!! " + email;
                badEmails++;
            }
            if (c.phone().equals("INVALID")) {
                badPhones++;
            }

            report.append(String.format(ROW, c.name(), email, c.phone(),
                    ContactParser.makeUsername(c.name())));
        }

        report.append(line).append(System.lineSeparator());
        report.append("Total contacts: ").append(contacts.size());
        report.append(" | Invalid emails: ").append(badEmails);
        report.append(" | Invalid phones: ").append(badPhones);
        return report.toString();
    }
}
