package com.jobsity.greetingsreminders.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Data
public class Config {

  private String birthdayMessage;
  private String birthdaySubject;
  private String reminderMessage;
  private String reminderSubject;

}
