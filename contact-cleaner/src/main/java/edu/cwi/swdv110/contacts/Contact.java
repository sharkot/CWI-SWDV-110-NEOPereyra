package edu.cwi.swdv110.contacts;

/** One cleaned-up contact. Records give us a constructor, getters, equals and toString for free. */
public record Contact(String name, String email, String phone) { }
