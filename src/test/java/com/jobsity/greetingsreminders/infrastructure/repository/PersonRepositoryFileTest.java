package com.jobsity.greetingsreminders.infrastructure.repository;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class PersonRepositoryFileTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final Config config = mock(Config.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private PersonRepositoryFile personRepositoryFile;

  @BeforeEach
  public void initMocks() {
    personRepositoryFile = new PersonRepositoryFile(dateUtils, customFileReader, config);
    when(config.getFileDirectory()).thenReturn("friend-list.txt");
  }

  @Test
  void getPersonsWithBirthday() {
    LocalDate defaultLocalDate = LocalDate.of(2023, 4, 17);
    when(dateUtils.getCurrentDate()).thenReturn(defaultLocalDate);
    Person expectedPerson = getExpectedPerson();
    List<Person> personsWithBirthday = personRepositoryFile.getPersonsToGreet();

    Assertions.assertEquals(expectedPerson, personsWithBirthday.get(0));
  }

  @Test
  void getPersonsWithBirthdayInFeb28() {
    LocalDate defaultLocalDate = LocalDate.of(2023, 2, 28);
    when(dateUtils.getCurrentDate()).thenReturn(defaultLocalDate);
    Person expectedPerson = getExpectedPersonForFeb29();
    List<Person> personsWithBirthday = personRepositoryFile.getPersonsToGreet();

    Assertions.assertEquals(expectedPerson, personsWithBirthday.get(0));
  }

  @Test
  void invalidFileShouldReturnEmpty () {
    when(config.getFileDirectory()).thenReturn("x.txt");
    List<Person> personsWithBirthday = personRepositoryFile.getPersonsToGreet();

    Assertions.assertTrue(personsWithBirthday.isEmpty());
  }


  private Person getExpectedPerson() {
    return Person.builder()
        .lastName("Doe")
        .firstname("John")
        .dateOfBirth(LocalDate.of(1982, 4, 17))
        .email("john.doe@foobar.com")
        .phoneNumber("+5901233")
        .build();
  }

  private Person getExpectedPersonForFeb29() {
    return Person.builder()
        .lastName("Ann")
        .firstname("Mary")
        .dateOfBirth(LocalDate.of(1960, 2, 29))
        .email("mary.ann@foobar.com")
        .phoneNumber("+594322")
        .build();
  }
}