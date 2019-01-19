package com.example.test.backend.rentalAgencyData;

public enum UserType {
    ADMIN("admin"), USER("user"), WORKER("worker");

    private final String name;

    private UserType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}