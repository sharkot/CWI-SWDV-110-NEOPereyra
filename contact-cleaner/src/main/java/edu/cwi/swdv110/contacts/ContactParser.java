package edu.cwi.swdv110.contacts;

/** Methods used to clean up each contact. */
public final class ContactParser {

    private ContactParser() { }

    // Make the first letter and letters after hyphens uppercase.
    public static String titleCase(String word) {
        StringBuilder result = new StringBuilder(word.toLowerCase());
        boolean capitalizeNext = true;

        for (int i = 0; i < result.length(); i++) {
            if (capitalizeNext) {
                result.setCharAt(i, Character.toUpperCase(result.charAt(i)));
            }
            capitalizeNext = result.charAt(i) == '-';
        }
        return result.toString();
    }

    public static String normalizeName(String raw) {
        String name = raw.trim();
        if (name.isEmpty()) {
            return "";
        }

        int comma = name.indexOf(',');
        if (comma != -1) {
            String last = name.substring(0, comma).trim();
            String first = name.substring(comma + 1).trim();
            name = first + " " + last;
        }

        String[] words = name.trim().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = titleCase(words[i]);
        }
        return String.join(" ", words);
    }

    // Check the email with string methods, not a regex.
    public static boolean isValidEmail(String email) {
        String value = email.trim();
        for (int i = 0; i < value.length(); i++) {
            if (Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        int at = value.indexOf('@');
        if (at <= 0 || at != value.lastIndexOf('@')) {
            return false;
        }

        String domain = value.substring(at + 1);
        int dot = domain.indexOf('.');
        if (dot <= 0 || dot == domain.length() - 1) {
            return false;
        }
        return !domain.contains("..");
    }

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public static String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at < 2) {
            return email;
        }
        return email.charAt(0) + "*".repeat(at - 1) + email.substring(at);
    }

    public static String formatPhone(String raw) {
        StringBuilder digits = new StringBuilder();
        for (char ch : raw.toCharArray()) {
            if (Character.isDigit(ch)) {
                digits.append(ch);
            }
        }

        if (digits.length() == 11 && digits.charAt(0) == '1') {
            digits.deleteCharAt(0);
        }
        if (digits.length() != 10) {
            return "INVALID";
        }

        String phone = digits.toString();
        return String.format("(%s) %s-%s", phone.substring(0, 3),
                phone.substring(3, 6), phone.substring(6));
    }

    public static String makeUsername(String fullName) {
        String[] words = fullName.trim().split("\\s+");
        if (fullName.trim().isEmpty()) {
            return "";
        }

        String name;
        if (words.length == 1) {
            name = words[0];
        } else {
            name = words[0].substring(0, 1) + words[words.length - 1];
        }

        StringBuilder letters = new StringBuilder();
        for (char ch : name.toLowerCase().toCharArray()) {
            if (Character.isLetter(ch)) {
                letters.append(ch);
            }
        }
        if (letters.length() > 8) {
            return letters.substring(0, 8);
        }
        return letters.toString();
    }

    public static Contact parseLine(String line) {
        // -1 keeps empty columns, including a missing name or phone.
        String[] parts = line.split("\\|", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("expected 3 fields but found " + parts.length);
        }

        String name = normalizeName(parts[0]);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is blank");
        }
        String email = normalizeEmail(parts[1]);
        String phone = formatPhone(parts[2]);
        return new Contact(name, email, phone);
    }
}
