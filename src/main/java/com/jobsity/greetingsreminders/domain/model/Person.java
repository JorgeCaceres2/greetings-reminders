package com.jobsity.greetingsreminders.domain.model;


import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Person {
  private String firstname;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private String phoneNumber;

  public boolean isTodayHisBirthday(LocalDate today) {
    if (today.getMonthValue() == 2 && today.getDayOfMonth() == 28 &&
        (dateOfBirth.getMonthValue() == 2 && dateOfBirth.getDayOfMonth() == 29)) {
        return !today.isLeapYear();
    }
    return (today.getMonthValue() == dateOfBirth.getMonthValue() && today.getDayOfMonth() == dateOfBirth.getDayOfMonth());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals(firstname, person.firstname) && Objects.equals(lastName, person.lastName) && Objects.equals(
        dateOfBirth, person.dateOfBirth) && Objects.equals(email, person.email) && Objects.equals(phoneNumber, person.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastName, dateOfBirth, email, phoneNumber);
  }
}
