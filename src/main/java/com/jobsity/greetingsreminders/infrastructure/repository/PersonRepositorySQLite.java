package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.entity.PersonEntity;

import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;


@Slf4j
//@ConditionalOnProperty(
//    value="person.repository.source",
//    havingValue = "SQLite",
//matchIfMissing = false)
public class PersonRepositorySQLite implements PersonRepository {


  @PersistenceContext
  private EntityManager entityManager;

  private final PersonTransformer personTransformerToDomain;
  private final DateUtils dateUtils;


  public PersonRepositorySQLite(PersonTransformer personTransformerToDomain,
      DateUtils dateUtils) {
    this.personTransformerToDomain = personTransformerToDomain;
    this.dateUtils = dateUtils;
  }


  public List<Person> getPersonsToGreet() {
    List<PersonEntity> personEntityList = findPersonsToGreet();
    return personEntityList.stream()
        .map(personTransformerToDomain::transformToPerson)
        .collect(Collectors.toList());
  }

  public List<Person> getAllPersons() {
    List<PersonEntity> personEntityList = findAll();
    return personEntityList.stream()
        .map(personTransformerToDomain::transformToPerson)
        .collect(Collectors.toList());
  }

  private List<PersonEntity> findAll() {
    TypedQuery<PersonEntity> query = entityManager.createQuery("SELECT p FROM Person p", PersonEntity.class);
    return query.getResultList();
  }

  private List<PersonEntity> findPersonsToGreet() {
    LocalDate today = dateUtils.getCurrentDate();
    int day = today.getDayOfMonth();
    int month = today.getMonthValue();
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT p FROM Person p WHERE (DAY(p.dateOfBirth) = :day AND MONTH(p.dateOfBirth) = :month)");

    if (month == 2 && day == 28) {
      sb.append("OR (DAY(p.dateOfBirth) = :leapDay AND MONTH(p.dateOfBirth) = :month)");
    }

    TypedQuery<PersonEntity> query = entityManager.createQuery(sb.toString(), PersonEntity.class);
    query.setParameter("day", day);
    query.setParameter("month", month);
    query.setParameter("leapDay", day + 1);

    return query.getResultList();
  }

}
