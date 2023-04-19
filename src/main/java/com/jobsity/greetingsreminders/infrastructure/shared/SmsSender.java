package com.jobsity.greetingsreminders.infrastructure.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsSender {

  public void sendMessage (String phoneNumber, String message) {
    //add the logic to send the sms...
    log.info("Text message sent to: {}, message: {}", phoneNumber, message);
  }

}
