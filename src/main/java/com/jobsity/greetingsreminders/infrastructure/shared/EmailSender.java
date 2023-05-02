package com.jobsity.greetingsreminders.infrastructure.shared;

import com.jobsity.greetingsreminders.infrastructure.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailSender {

  public void sendEmail (EmailDTO emailDto) {
    //add the logic to send the email here...
    log.info("Email sent to: {}, subject: {}, message: {}", emailDto.getEmail(), emailDto.getSubject(), emailDto.getMessage());
  }

}
