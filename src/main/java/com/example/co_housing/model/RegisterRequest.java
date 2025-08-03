package com.example.co_housing.model;

import java.time.LocalDate;

public record RegisterRequest(String email, String password, String name, LocalDate dob, String gender, String bio,
                              String city, String state, String country, String mobile, String profession) {
}
