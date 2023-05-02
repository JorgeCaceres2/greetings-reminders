package com.jobsity.greetingsreminders.infrastructure.service;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.dto.EmailDTO;
import com.jobsity.greetingsreminders.infrastructure.dto.SmsDTO;
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
      emailSender.sendEmail(new EmailDTO(person.getEmail(), birthdayEmailSubject, birthdayMessage));
      smsSender.sendMessage(new SmsDTO(person.getPhoneNumber(), birthdayMessage));
    }
  }

  @Override
  public void birthdayReminders(List<Person> friendList, List<Person> friendsToGreet) {
    String reminderEmailMessage = config.getReminderMessage();
    String reminderEmailSubject = config.getReminderSubject();

    for (Person personToGreet : friendsToGreet) {
      for (Person personToReminder : friendList) {
        if (!personToGreet.equals(personToReminder)) {
          String reminderMessage = String.format(reminderEmailMessage, personToReminder.getFirstname(), personToGreet.getFirstname(),
              personToGreet.getLastName());
          emailSender.sendEmail(new EmailDTO(personToReminder.getEmail(), reminderEmailSubject, reminderMessage));
          smsSender.sendMessage(new SmsDTO(personToReminder.getPhoneNumber(), reminderMessage));
        }
      }
    }
  }
}
