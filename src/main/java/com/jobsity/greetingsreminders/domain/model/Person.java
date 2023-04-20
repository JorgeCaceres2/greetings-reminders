package com.jobsity.greetingsreminders.domain.model;


import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
  private String firstname;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private String phoneNumber;
}
