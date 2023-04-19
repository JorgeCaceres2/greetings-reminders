package application;


import domain.model.ClassifiedPersons;
import domain.repository.PersonRepository;
import domain.service.BirthdayService;
import org.springframework.stereotype.Component;

@Component
public class BirthdayReminderUseCase {

  private final BirthdayService birthdayService;

  private final PersonRepository personRepository;


  public BirthdayReminderUseCase(BirthdayService birthdayService, PersonRepository personRepository) {
    this.birthdayService = birthdayService;
    this.personRepository = personRepository;
  }

  void sendBirthdayReminders () {
    ClassifiedPersons classifiedPersons = personRepository.getClassifiedPersons();
    birthdayService.birthdayReminders(classifiedPersons);
  }
}
