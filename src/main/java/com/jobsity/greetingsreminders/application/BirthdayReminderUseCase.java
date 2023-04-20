package com.jobsity.greetingsreminders.application;


import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositoryFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BirthdayReminderUseCase {

  private final BirthdayService birthdayService;

  private final PersonRepositoryFactory personRepositoryFactory;


  public BirthdayReminderUseCase(BirthdayService birthdayService, PersonRepositoryFactory personRepositoryFactory) {
    this.birthdayService = birthdayService;
    this.personRepositoryFactory = personRepositoryFactory;
  }

  void sendBirthdayReminders () {
    List<Person> friendList = personRepositoryFactory.getRepository().getAllPersons();
    List<Person> friendsToGreet = personRepositoryFactory.getRepository().getPersonsToGreet();
    birthdayService.birthdayReminders(friendList, friendsToGreet);
  }
}
