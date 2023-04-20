package com.jobsity.greetingsreminders.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Data
@Component
public class Config {

  private String personRepositorySource;
  private String birthdayMessage;
  private String birthdaySubject;
  private String reminderMessage;
  private String reminderSubject;
  private String fileDirectory;

}
