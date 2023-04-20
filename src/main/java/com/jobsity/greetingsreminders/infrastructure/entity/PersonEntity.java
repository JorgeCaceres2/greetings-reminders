package com.jobsity.greetingsreminders.infrastructure.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;


@Entity(name="Person")
@Data
@Builder
public class PersonEntity {

  @Id
  @SequenceGenerator(
      name="person_sequence",
      sequenceName = "person_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = SEQUENCE,
      generator = "person_sequence"
  )
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private String phoneNumber;

}
