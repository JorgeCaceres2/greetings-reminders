package com.jobsity.greetingsreminders.infrastructure.shared;

import com.jobsity.greetingsreminders.infrastructure.dto.SmsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsSender {

  public void sendMessage (SmsDTO smsDTO) {
    //add the logic to send the sms...
    log.info("Text message sent to: {}, message: {}", smsDTO.getPhoneNumber(), smsDTO.getMessage());
  }

}
