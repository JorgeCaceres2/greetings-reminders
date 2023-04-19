package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.model.ClassifiedPersons;
import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Component
public class PersonRepositoryFile implements PersonRepository {

  //private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryFile.class);
  private final CustomFileReader customFileReader;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  public PersonRepositoryFile(CustomFileReader customFileReader) {
    this.customFileReader = customFileReader;
  }

  @Override
  public List<Person> getPersonsWithBirthday() {
    List<Person> friendList;
    try {
      friendList = getAllFriendsFromFile();
    } catch (IOException e) {
      //Assuming that the exception is not mandatory to handle
      //logger.error("Invalid File, returning an empty person list");
      return Collections.emptyList();
    }
    ClassifiedPersons classifiedPersons = getClassifiedPersons(friendList);
    return classifiedPersons.getPersonToGreet();
  }


  @Override
  public ClassifiedPersons getClassifiedPersons() {
    List<Person> personList;
    try {
       personList = getAllFriendsFromFile();
    } catch (IOException e) {
      //Assuming that the exception is not mandatory to handle
      //logger.error("Invalid File, returning an empty person list");
      return null;
    }

    ClassifiedPersons classifiedPersons = getClassifiedPersons(personList);
    return classifiedPersons;
  }

  private ClassifiedPersons getClassifiedPersons (List <Person> friendList) {
    List<Person> personsToGreet = new ArrayList<>();
    List<Person> personsToReminder = new ArrayList<>();

    LocalDate today = LocalDate.now();
    for (Person person : friendList) {
      LocalDate friendDateOfBirth = LocalDate.parse(person.getDateOfBirth(), formatter);
      if (friendDateOfBirth.getMonthValue() == 2 && friendDateOfBirth.getDayOfMonth() == 29) {
        friendDateOfBirth = friendDateOfBirth.minusDays(1);
      }

      if (friendDateOfBirth.getMonthValue() == today.getMonthValue()
          && friendDateOfBirth.getDayOfMonth() == today.getDayOfMonth()) {
        personsToGreet.add(person);
      } else {
        personsToReminder.add(person);
      }
    }

    ClassifiedPersons classifiedPersons = ClassifiedPersons.builder()
        .personsToReminders(personsToReminder)
        .personToGreet(personsToGreet)
        .build();

    return classifiedPersons;
  }


  private List<Person> getAllFriendsFromFile() throws IOException {
    List<String> lines = customFileReader.readFile("friend-list.txt");
    List<Person> friendList = new ArrayList<>();
    for (String line : lines) {
      String[] tokens = line.split(",");
      Person person = Person.builder()
          .lastName(StringUtils.capitalize(tokens[0].trim()))
          .firstname(StringUtils.capitalize(tokens[1].trim()))
          .dateOfBirth(tokens[2].trim())
          .email(tokens[3].trim())
          .phoneNumber(tokens[4].trim())
          .build();
      friendList.add(person);
    }

    //logger.info("Returning results from file: {}", friendList);
    return friendList;
  }
}
