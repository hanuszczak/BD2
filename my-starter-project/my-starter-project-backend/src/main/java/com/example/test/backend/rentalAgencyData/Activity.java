package com.example.test.backend.rentalAgencyData;

public enum Activity {
    ACTIVE("Active"), BLOCKED("Blocked");

    private final String name;

    private Activity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}