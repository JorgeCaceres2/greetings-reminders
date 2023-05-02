package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.model.Person;
import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.entity.PersonEntity;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Slf4j
//@ConditionalOnProperty(
//    value="person.repository.source",
//    havingValue = "SQLite")
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
    List<Person> personList = findAll().stream()
        .map(personTransformerToDomain::transformToPerson)
        .filter(person -> person.isTodayHisBirthday(dateUtils.getCurrentDate()))
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

}
