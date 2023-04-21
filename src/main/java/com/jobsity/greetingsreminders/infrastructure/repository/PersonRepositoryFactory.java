package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonRepositoryFactory {

  private final Config config;
  private final DateUtils dateUtils;
  private final CustomFileReader customFileReader;
  private final PersonTransformer personTransformerToDomain;
  private final EntityManager entityManager;

  public PersonRepositoryFactory(Config config, DateUtils dateUtils, CustomFileReader customFileReader, PersonTransformer personTransformer,
      EntityManager entityManager) {
    this.config = config;
    this.dateUtils = dateUtils;
    this.customFileReader = customFileReader;
    this.personTransformerToDomain = personTransformer;
    this.entityManager = entityManager;
  }


  public PersonRepository getRepository() {
    String sourceType = config.getPersonRepositorySource();
    log.info("Receiving source from config: {}", sourceType);
    if ("File".endsWith(sourceType)) {
      return new PersonRepositoryFile(dateUtils, customFileReader, config);
    } else if ("SQLite".equals(sourceType)) {
      return new PersonRepositorySQLite(entityManager, personTransformerToDomain, dateUtils);
    } else {
      throw new IllegalArgumentException("Invalid source type in PersonRepositorySource property");
    }
  }
}
