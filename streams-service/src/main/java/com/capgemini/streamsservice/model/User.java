package com.capgemini.streamsservice.model;

public record User(
        String id,
        String firstName,
        String lastName,
        int age
) {
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}