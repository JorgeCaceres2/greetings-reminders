package com.jobsity.greetingsreminders.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "notification")
public class Config {

  private String birthdayMessage;
  private String birthdaySubject;
  private String reminderMessage;
  private String reminderSubject;
  @Value( "${person.repository.source}")
  private String personRepositorySource;
  @Value( "${person.repository.file.directory}")
  private String fileDirectory;
  @Value( "${person.repository.file.delimiter}")
  private String fileDelimiter;
  @Value("${date-format}")
  private String dateFormat;

}
