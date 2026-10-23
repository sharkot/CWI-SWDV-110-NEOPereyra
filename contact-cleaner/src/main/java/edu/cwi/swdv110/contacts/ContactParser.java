package edu.cwi.swdv110.contacts;

public final class ContactParser {
    private ContactParser() { }

    public static String titleCase(String word) {
        StringBuilder name = new StringBuilder(word.toLowerCase());
        boolean capitalize = true;

        for (int i = 0; i < name.length(); i++) {
            if (capitalize) {
                name.setCharAt(i, Character.toUpperCase(name.charAt(i)));
            }
            capitalize = name.charAt(i) == '-';
        }
        return name.toString();
    }

    public static String normalizeName(String raw) {
        String name = raw.trim();
        if (name.isEmpty()) {
            return "";
        }

        int comma = name.indexOf(',');
        if (comma != -1) {
            // Last, First needs to become First Last.
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

    public static boolean isValidEmail(String email) {
        String e = email.trim();
        for (char c : e.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            }
        }

        int at = e.indexOf('@');
        if (at < 1 || at != e.lastIndexOf('@')) {
            return false;
        }

        String domain = e.substring(at + 1);
        int dot = domain.indexOf('.');
        if (dot < 1 || dot == domain.length() - 1) {
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
        for (char c : raw.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.append(c);
            }
        }

        // Remove the country code if it's just a 1.
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
        if (fullName.trim().isEmpty()) {
            return "";
        }

        String[] words = fullName.trim().split("\\s+");
        String name = words[0];
        if (words.length > 1) {
            name = words[0].substring(0, 1) + words[words.length - 1];
        }

        StringBuilder username = new StringBuilder();
        for (char c : name.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                username.append(c);
            }
        }

        if (username.length() > 8) {
            return username.substring(0, 8);
        }
        return username.toString();
    }

    public static Contact parseLine(String line) {
        // -1 keeps the empty pieces too.
        String[] parts = line.split("\\|", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("expected 3 fields but found " + parts.length);
        }

        String name = normalizeName(parts[0]);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is blank");
        }

        return new Contact(name, normalizeEmail(parts[1]), formatPhone(parts[2]));
    }
}
