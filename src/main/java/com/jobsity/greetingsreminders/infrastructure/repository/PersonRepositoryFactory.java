package com.jobsity.greetingsreminders.infrastructure.repository;

import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import org.springframework.stereotype.Component;

@Component
public class PersonRepositoryFactory {

  private final Config config;
  private final DateUtils dateUtils;
  private final CustomFileReader customFileReader;
  private final PersonTransformer personTransformerToDomain;

  public PersonRepositoryFactory(Config config, DateUtils dateUtils, CustomFileReader customFileReader, PersonTransformer personTransformer) {
    this.config = config;
    this.dateUtils = dateUtils;
    this.customFileReader = customFileReader;
    this.personTransformerToDomain = personTransformer;
  }


  public PersonRepository getRepository() {
    String sourceType = config.getPersonRepositorySource();
    if ("File".endsWith(sourceType)) {
      return new PersonRepositoryFile(dateUtils, customFileReader, config);
    } else if ("SQLite".equals(sourceType)) {
      return new PersonRepositorySQLite(personTransformerToDomain, dateUtils);
    } else {
      throw new IllegalArgumentException("Invalid source type in PersonRepositorySource property");
    }
  }
}
