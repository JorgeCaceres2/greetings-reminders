package com.jobsity.greetingsreminders.infrastructure.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsDTO {

  private final String phoneNumber;
  private final String message;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmsDTO smsDTO = (SmsDTO) o;
    return Objects.equals(phoneNumber, smsDTO.phoneNumber) && Objects.equals(message, smsDTO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phoneNumber, message);
  }
}
