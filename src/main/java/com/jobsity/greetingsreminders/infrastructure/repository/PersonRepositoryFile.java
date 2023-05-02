package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class PersonRepositoryFile implements PersonRepository {

  private final DateUtils dateUtils;
  private final CustomFileReader customFileReader;
  private final Config config;

  public PersonRepositoryFile(DateUtils dateUtils, CustomFileReader customFileReader,
      Config config) {
    this.dateUtils = dateUtils;
    this.customFileReader = customFileReader;
    this.config = config;
  }

  public List<Person> getPersonsToGreet() {
    List<Person> personList = getAllPersons().stream()
        .filter(person -> person.isTodayHisBirthday(dateUtils.getCurrentDate()))
        .collect(Collectors.toList());

    log.info("Returning persons to greet: {}", personList);
    return personList;
  }

  public List<Person> getAllPersons() {
    try {
      List<Person> personList = getAllFriendsFromFile();
      log.info("Returning all existing friends in file: {}", personList);
      return personList;
    } catch (IOException e) {
      //Assuming that the exception is not mandatory to handle
      log.error("Invalid File, returning an empty person list", e);
      return List.of();
    }
  }

  private List<Person> getAllFriendsFromFile() throws IOException {
    String fileDirectory = config.getFileDirectory();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(config.getDateFormat());
    List<String> lines = customFileReader.readFile(fileDirectory);
    List<Person> friendList = new ArrayList<>();
    for (String line : lines) {
      String[] tokens = line.split(config.getFileDelimiter());
      Person person = Person.builder()
          .lastName(StringUtils.capitalize(tokens[0].trim()))
          .firstname(StringUtils.capitalize(tokens[1].trim()))
          .dateOfBirth(LocalDate.parse(tokens[2].trim(), formatter))
          .email(tokens[3].trim())
          .phoneNumber(tokens[4].trim())
          .build();
      friendList.add(person);
    }
    log.info("Returning results from file: {}", List.of(friendList));
    return friendList;
  }
}
