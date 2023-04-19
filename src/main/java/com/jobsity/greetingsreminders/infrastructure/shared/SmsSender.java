package com.jobsity.greetingsreminders.infrastructure.shared;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class SmsSender {

  //private static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

  public void sendMessage (String phoneNumber, String message) {
    //add the logic to send the sms...
    //logger.info("Text message sent to: {}, message: {}", phoneNumber, message);
  }

}
