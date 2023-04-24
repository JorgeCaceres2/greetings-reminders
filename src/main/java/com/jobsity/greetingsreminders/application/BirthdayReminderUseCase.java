package com.jobsity.greetingsreminders.application;


import com.jobsity.greetingsreminders.domain.factory.PersonRepositoryFactory;
import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BirthdayReminderUseCase {

  private final BirthdayService birthdayService;

  private final PersonRepositoryFactory personRepositoryFactory;


  public BirthdayReminderUseCase(BirthdayService birthdayService, PersonRepositoryFactory personRepositoryFactory) {
    this.birthdayService = birthdayService;
    this.personRepositoryFactory = personRepositoryFactory;
  }

  public void sendBirthdayReminders () {
    log.info("Starting BirthdayReminderUseCase");
    List<Person> friendList = personRepositoryFactory.getRepository().getAllPersons();
    List<Person> friendsToGreet = personRepositoryFactory.getRepository().getPersonsToGreet();
    birthdayService.birthdayReminders(friendList, friendsToGreet);
    log.info("End of BirthdayReminderUseCase");
  }
}
