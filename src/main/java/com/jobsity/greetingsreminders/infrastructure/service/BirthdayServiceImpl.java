package com.jobsity.greetingsreminders.infrastructure.service;

import com.jobsity.greetingsreminders.domain.model.ClassifiedPersons;
import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.shared.EmailSender;
import com.jobsity.greetingsreminders.infrastructure.shared.SmsSender;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BirthdayServiceImpl implements BirthdayService {

  private final EmailSender emailSender;
  private final SmsSender smsSender;
  private final Config config;

  public BirthdayServiceImpl(EmailSender emailSender, SmsSender smsSender, Config config) {
    this.emailSender = emailSender;
    this.smsSender = smsSender;
    this.config = config;
  }

  @Override
  public void birthdayGreetings(List<Person> personsToGreet) {
    String birthdayEmailMessage = config.getBirthdayMessage();
    String birthdayEmailSubject = config.getBirthdaySubject();

    for (Person person : personsToGreet) {
      String birthdayMessage = String.format(birthdayEmailMessage, person.getFirstname());
      emailSender.sendEmail(person.getEmail(), birthdayEmailSubject, birthdayMessage);
      smsSender.sendMessage(person.getPhoneNumber(), birthdayMessage);
    }
  }

  @Override
  public void birthdayReminders(ClassifiedPersons classifiedPersons) {
    List<Person> personsToReminders = classifiedPersons.getPersonsToReminders();
    List<Person> personsToGreet = classifiedPersons.getPersonToGreet();
    String reminderEmailMessage = config.getReminderMessage();
    String reminderEmailSubject = config.getReminderSubject();

    for (Person personToGreet : personsToGreet) {
      for (Person personToReminder : personsToReminders) {
        String reminderMessage = String.format(reminderEmailMessage, personToReminder.getFirstname(), personToGreet.getFirstname(), personToGreet.getLastName());
        emailSender.sendEmail(personToReminder.getEmail(), reminderEmailSubject, reminderMessage);
        smsSender.sendMessage(personToReminder.getPhoneNumber(), reminderMessage);
      }
    }
  }

}
