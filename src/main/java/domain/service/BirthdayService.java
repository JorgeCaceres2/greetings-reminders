package domain.service;

import domain.model.ClassifiedPersons;
import domain.model.Person;
import java.util.List;

public interface BirthdayService {

  void birthdayGreetings(List<Person> personList);

  void birthdayReminders(ClassifiedPersons classifiedPersons);

}
