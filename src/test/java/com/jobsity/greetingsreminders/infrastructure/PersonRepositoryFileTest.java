package com.jobsity.greetingsreminders.infrastructure;


import domain.model.Person;
import infrastructure.repository.PersonRepositoryFile;
import infrastructure.shared.CustomFileReader;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


public class PersonRepositoryFileTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private PersonRepositoryFile personRepositoryFile;

  @Before
  public void initMocks () {
    personRepositoryFile = new PersonRepositoryFile(customFileReader);
  }

  @Test
  public void getPersonsWithBirthday() {
    LocalDate defaultLocalDate = LocalDate.of(2023,4,17);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      mockedLocalDate.when(LocalDate::now).thenReturn(defaultLocalDate);
      Person expectedPerson = getExpectedPerson();
      List<Person> personsWithBirthday = personRepositoryFile.getPersonsWithBirthday();
      Assertions.assertEquals(expectedPerson, personsWithBirthday.get(0));
    }
  }

  @Test
  public void getPersonsWithBirthdayInFeb28() {
    LocalDate defaultLocalDate = LocalDate.of(2023,2,28);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      mockedLocalDate.when(LocalDate::now).thenReturn(defaultLocalDate);
      Person expectedPerson = getExpectedPersonForFeb29();
      List<Person> personsWithBirthday = personRepositoryFile.getPersonsWithBirthday();
      Assertions.assertEquals(expectedPerson, personsWithBirthday.get(0));
    }
  }


  private Person getExpectedPerson () {
    return Person.builder()
        .lastName("Doe")
        .firstname("John")
        .dateOfBirth("1982/04/17")
        .email("john.doe@foobar.com")
        .phoneNumber("+5901233")
        .build();
  }

  private Person getExpectedPersonForFeb29 () {
    return Person.builder()
        .lastName("Ann")
        .firstname("Mary")
        .dateOfBirth("1975/02/29")
        .email("mary.ann@foobar.com")
        .phoneNumber("+594322")
        .build();
  }
}