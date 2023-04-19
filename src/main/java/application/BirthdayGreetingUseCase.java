package application;

import domain.model.Person;
import domain.repository.PersonRepository;
import domain.service.BirthdayService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BirthdayGreetingUseCase {

  private final BirthdayService birthdayService;
  private final PersonRepository personRepository;

  public BirthdayGreetingUseCase(BirthdayService birthdayService, PersonRepository personRepository) {
    this.birthdayService = birthdayService;
    this.personRepository = personRepository;
  }

  void sendBirthdayGreetings () {
    List<Person> personList = personRepository.getPersonsWithBirthday();
    birthdayService.birthdayGreetings(personList);
  }
}
