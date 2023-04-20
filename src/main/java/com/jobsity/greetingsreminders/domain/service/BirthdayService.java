package com.jobsity.greetingsreminders.domain.service;

import com.jobsity.greetingsreminders.domain.model.Person;
import java.util.List;

public interface BirthdayService {

  void birthdayGreetings(List<Person> personList);

  void birthdayReminders(List<Person> friendList, List<Person> friendsToGreet);

}
