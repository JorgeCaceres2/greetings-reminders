package com.jobsity.greetingsreminders.infrastructure.transformer;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.infrastructure.entity.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonTransformer {

  public Person transformToPerson(PersonEntity personEntity) {
    return Person.builder()
        .lastName(personEntity.getLastName())
        .firstname(personEntity.getFirstName())
        .dateOfBirth(personEntity.getDateOfBirth())
        .email(personEntity.getEmail())
        .phoneNumber(personEntity.getPhoneNumber())
        .build();
  }
}
