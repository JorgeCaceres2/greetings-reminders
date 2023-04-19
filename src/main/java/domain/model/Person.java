package domain.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
  private String firstname;
  private String lastName;
  private String dateOfBirth;
  private String email;
  private String phoneNumber;
}
