package com.jobsity.greetingsreminders.infrastructure.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;


@Entity(name="Person")
@Data
@Builder
public class PersonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private String phoneNumber;


  public PersonEntity () {
    //required by jpa
  }

  public PersonEntity(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

}
