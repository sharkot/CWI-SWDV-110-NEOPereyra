package edu.cwi.swdv110.contacts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ContactExtraTest {

    @Test
    void phoneWithoutEnoughDigitsIsInvalid() {
        assertEquals("INVALID", ContactParser.formatPhone("123"));
    }

    @Test
    void blankEmailIsInvalid() {
        assertFalse(ContactParser.isValidEmail("  "));
    }

    @Test
    void missingPhoneFieldIsKeptAsInvalid() {
        Contact contact = ContactParser.parseLine("jane doe | jane@school.edu | ");
        assertEquals("INVALID", contact.phone());
    }
}
