package com.jobsity.greetingsreminders.infrastructure.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailDTO {

  private final String email;
  private final String subject;
  private final String message;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailDTO emailDTO = (EmailDTO) o;
    return Objects.equals(email, emailDTO.email) &&
        Objects.equals(subject, emailDTO.subject) &&
        Objects.equals(message, emailDTO.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, subject, message);
  }
}
