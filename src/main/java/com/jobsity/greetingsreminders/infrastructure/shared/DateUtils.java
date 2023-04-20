package com.jobsity.greetingsreminders.infrastructure.shared;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DateUtils {

  public LocalDate getCurrentDate () {
    return LocalDate.now();
  }

}
