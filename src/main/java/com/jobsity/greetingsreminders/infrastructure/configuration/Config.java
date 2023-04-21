package com.jobsity.greetingsreminders.infrastructure.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Data
@Component
public class Config {

  @Value( "${person.repository.source}")
  private String personRepositorySource;
  @Value( "${notification.birthdayMessage}")
  private String birthdayMessage;
  @Value( "${notification.birthdaySubject}")
  private String birthdaySubject;
  @Value( "${notification.reminderMessage}")
  private String reminderMessage;
  @Value( "${notification.reminderSubject}")
  private String reminderSubject;
  @Value( "${person.repository.file.directory}")
  private String fileDirectory;

}
