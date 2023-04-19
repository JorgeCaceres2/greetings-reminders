package com.jobsity.greetingsreminders.application;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BirthdayGreetingUseCase {

  private final BirthdayService birthdayService;
  private final PersonRepository personRepository;

  public BirthdayGreetingUseCase(BirthdayService birthdayService, PersonRepository personRepository) {
    this.birthdayService = birthdayService;
    this.personRepository = personRepository;
  }

  void sendBirthdayGreetings () {
    List<Person> personList = personRepository.getPersonsWithBirthday();
    birthdayService.birthdayGreetings(personList);
  }
}
