package domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassifiedPersons {

  List<Person> personToGreet;
  List<Person> personsToReminders;

}
