package com.jobsity.greetingsreminders.application;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositoryFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BirthdayGreetingUseCase {

  private final BirthdayService birthdayService;
  private final PersonRepositoryFactory personRepositoryFactory;

  public BirthdayGreetingUseCase(BirthdayService birthdayService, PersonRepositoryFactory personRepositoryFactory) {
    this.birthdayService = birthdayService;
    this.personRepositoryFactory = personRepositoryFactory;
  }

  void sendBirthdayGreetings () {
    List<Person> personList = personRepositoryFactory.getRepository().getPersonsToGreet();
    birthdayService.birthdayGreetings(personList);
  }
}
