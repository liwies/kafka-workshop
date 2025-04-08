package com.capgemini.producerservice.model;

public record User (
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