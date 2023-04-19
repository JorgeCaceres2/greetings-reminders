package com.jobsity.greetingsreminders.infrastructure.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailSender {

  public void sendEmail (String email, String subject, String message) {
    //add the logic to send the email here...
    log.info("Email sent to: {}, subject: {}, message: {}", email, subject, message);
  }

}
