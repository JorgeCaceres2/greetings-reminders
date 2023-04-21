package com.jobsity.greetingsreminders.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PersonRepositorySQLiteTest {

  private PersonRepositorySQLite personRepositorySQLite;
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final DateUtils dateUtils = mock(DateUtils.class);
  @PersistenceContext
  private EntityManager entityManager;


  @BeforeEach
  void initTest () {
    personRepositorySQLite = new PersonRepositorySQLite(entityManager, personTransformer, dateUtils);
  }

  @Test
  void getAllPersonsTest () {
    List<Person> allPersons = personRepositorySQLite.getAllPersons();

    assertFalse(allPersons.isEmpty());
    assertEquals(3, allPersons.size());
  }

  @Test
  void test2 () {
    LocalDate mockedDate = LocalDate.of(2023, 4, 17);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    List<Person> personsToGreet = personRepositorySQLite.getPersonsToGreet();

    assertEquals(1, personsToGreet.size());
  }

  @Test
  void test3 () {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    List<Person> personsToGreet = personRepositorySQLite.getPersonsToGreet();

    assertEquals(1, personsToGreet.size());
  }

}