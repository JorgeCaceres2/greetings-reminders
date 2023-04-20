package com.jobsity.greetingsreminders.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.entity.PersonEntity;
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositoryFactory;
import com.jobsity.greetingsreminders.infrastructure.service.BirthdayServiceImpl;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.shared.EmailSender;
import com.jobsity.greetingsreminders.infrastructure.shared.SmsSender;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BirthdayGreetingUseCaseSqlRepoTest {

  @Autowired
  private BirthdayGreetingUseCase birthdayGreetingUseCase;

  @Autowired
  private TestEntityManager entityManager;

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);

  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactory(config, dateUtils, customFileReader, personTransformer);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
  }

  /*
  * (1,'Doe', 'John', '1982/04/17', 'john.doe@foobar.com', '+5901233');
INSERT INTO person(id, last_name,first_name, date_of_birth, email, phone_number) VALUES(2, 'Ann', 'Mary', '1960/02/29', 'mary.ann@foobar.com', '+594322');
INSERT INTO person(id,last_name,first_name, date_of_birth, email, phone_number) VALUES(3, 'Tire', 'Mike', '1986/04/19', 'mike.tire@foobar.com', '+532344');*/


  @Test
  void shouldSendBirthdayGreetingsFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);

    PersonEntity p1 = PersonEntity.builder()
        .id(1L)
        .lastName("Doe")
        .firstName("John")
        .dateOfBirth(LocalDate.of(1982, 4, 17))
        .email("john.doe@foobar.com")
        .phoneNumber("+5901233")
        .build();

    PersonEntity p2 = PersonEntity.builder()
        .id(2L)
        .lastName("Ann")
        .firstName("Mary")
        .dateOfBirth(LocalDate.of(1960, 2, 29))
        .email("mary.ann@foobar.com")
        .phoneNumber("+594322")
        .build();

    entityManager.persist(p1);
    entityManager.persist(p2);
    entityManager.flush();

    String msg = "Happy birthday, dear %s!";
    String subject = "Happy birthday!";
    when(config.getPersonRepositorySource()).thenReturn("SQLite");
    when(config.getBirthdayMessage()).thenReturn(msg);
    when(config.getBirthdaySubject()).thenReturn(subject);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();

    verify(emailSender, times(1)).sendEmail("mary.ann@foobar.com", "Happy birthday!", "Happy birthday, dear Mary!");
    verify(smsSender, times(1)).sendMessage("+594322", "Happy birthday, dear Mary!");
  }

}
