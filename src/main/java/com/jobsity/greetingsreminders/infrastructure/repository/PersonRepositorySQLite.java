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
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
//@ConditionalOnProperty(
//    value="person.repository.source",
//    havingValue = "SQLite",
//matchIfMissing = false)
@Component
public class PersonRepositorySQLite implements PersonRepository {


  @PersistenceContext
  private final EntityManager entityManager;

  private final PersonTransformer personTransformerToDomain;
  private final DateUtils dateUtils;


  public PersonRepositorySQLite(EntityManager entityManager, PersonTransformer personTransformerToDomain,
      DateUtils dateUtils) {
    this.entityManager = entityManager;
    this.personTransformerToDomain = personTransformerToDomain;
    this.dateUtils = dateUtils;
  }


  public List<Person> getPersonsToGreet() {
    List<PersonEntity> personEntityList = findPersonsToGreet();
    List<Person> personList = personEntityList.stream()
        .map(personTransformerToDomain::transformToPerson)
        .collect(Collectors.toList());
    log.info("Returning persons to greet results: {}", personList);
    return personList;
  }

  public List<Person> getAllPersons() {
    List<PersonEntity> personEntityList = findAll();
    List<Person> allPersonList = personEntityList.stream()
        .map(personTransformerToDomain::transformToPerson)
        .collect(Collectors.toList());
    log.info("Returning all existing persons results: {}", allPersonList);
    return allPersonList;
  }

  private List<PersonEntity> findAll() {
    TypedQuery<PersonEntity> query = entityManager.createQuery("SELECT p FROM Person p", PersonEntity.class);
    return query.getResultList();
  }

  private List<PersonEntity> findPersonsToGreet() {
    LocalDate today = dateUtils.getCurrentDate();
    int day = today.getDayOfMonth();
    int month = today.getMonthValue();

//    If today is feb-29, yesterday we already sent greetings and reminders
//    If we want to avoid double notifications, uncomment this validation
//    if (month == 2 && day == 29) {
//      return List.of();
//    }

    TypedQuery<PersonEntity> query;
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT p FROM Person p ");
    sb.append("WHERE (cast(substr(p.dateOfBirth, 9, 2) as integer) = :day AND cast(substr(p.dateOfBirth, 6, 2) as integer) = :month) ");

    if (month == 2 && day == 28) {
      sb.append("OR (cast(substr(p.dateOfBirth, 9, 2) as integer) = :day + 1 AND cast(substr(p.dateOfBirth, 6, 2) as integer) = :month)");
    }

    query = entityManager.createQuery(sb.toString(), PersonEntity.class);
    query.setParameter("day", day);
    query.setParameter("month", month);

    return query.getResultList();
  }


  //out of scope - this is just for startup population  - Testing purposes
  @Transactional
  public void addPerson (PersonEntity personEntity) {
    entityManager.persist(personEntity);
    entityManager.flush();
  }

}
