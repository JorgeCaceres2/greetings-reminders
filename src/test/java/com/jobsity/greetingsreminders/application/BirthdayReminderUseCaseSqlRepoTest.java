package com.jobsity.greetingsreminders.application;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.configuration.TestConfig;
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositoryFactory;
import com.jobsity.greetingsreminders.infrastructure.service.BirthdayServiceImpl;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.shared.EmailSender;
import com.jobsity.greetingsreminders.infrastructure.shared.SmsSender;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class BirthdayReminderUseCaseSqlRepoTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private BirthdayReminderUseCase birthdayReminderUseCase;
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private TestConfig testConfig;
  private String expectedMessage;
  private String expectedSubject;

  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactory(config, dateUtils, customFileReader, personTransformer,
        entityManager);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayReminderUseCase = new BirthdayReminderUseCase(birthdayService,personRepositoryFactory);
    expectedMessage = "Don't forget to sem him a message!";
    expectedSubject = testConfig.getReminderSubject();
    when(config.getPersonRepositorySource()).thenReturn("SQLite");
    when(config.getReminderMessage()).thenReturn(testConfig.getReminderMessage());
    when(config.getReminderSubject()).thenReturn(testConfig.getReminderSubject());

  }

  @Test
  void shouldSendBirthdayRemindersSqlRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 4, 17);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(anyString(), eq(expectedSubject),contains(expectedMessage));
    verify(smsSender, times(2)).sendMessage(anyString(), contains(expectedMessage));
  }

  @Test
  void shouldSendBirthdayRemindersSqlRepoFeb28Case() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(anyString(), eq(expectedSubject),contains(expectedMessage));
    verify(smsSender, times(2)).sendMessage(anyString(), contains(expectedMessage));
  }

  @Test
  void shouldNotSendBirthdayRemindersSqlRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    verify(smsSender, never()).sendMessage(anyString(), anyString());
  }

}
