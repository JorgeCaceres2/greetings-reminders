package com.jobsity.greetingsreminders.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.factory.PersonRepositoryFactory;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.configuration.TestConfig;
import com.jobsity.greetingsreminders.infrastructure.factory.PersonRepositoryFactoryImpl;
import com.jobsity.greetingsreminders.infrastructure.service.BirthdayServiceImpl;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.DateUtils;
import com.jobsity.greetingsreminders.infrastructure.shared.EmailSender;
import com.jobsity.greetingsreminders.infrastructure.shared.SmsSender;
import com.jobsity.greetingsreminders.infrastructure.transformer.PersonTransformer;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class BirthdayReminderUseCaseFileRepoTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final EntityManager entityManager = mock(EntityManager.class);
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private BirthdayReminderUseCase birthdayReminderUseCase;
  @Autowired
  private TestConfig testConfig;


  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactoryImpl(config, dateUtils, customFileReader, personTransformer,
        entityManager);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayReminderUseCase = new BirthdayReminderUseCase(birthdayService, personRepositoryFactory);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(config.getFileDirectory()).thenReturn(testConfig.getFileDirectory());
    when(config.getReminderMessage()).thenReturn(testConfig.getReminderMessage());
    when(config.getFileDelimiter()).thenReturn(testConfig.getFileDelimiter());
    when(config.getDateFormat()).thenReturn(testConfig.getDateFormat());
  }

  @Test
  void shouldSendBirthdayRemindersFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 4, 17);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(any());
    verify(smsSender, times(2)).sendMessage(any());
  }

  @Test
  void shouldSendBirthdayRemindersFileRepoFeb28Case() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(any());
    verify(smsSender, times(2)).sendMessage(any());
  }

  @Test
  void shouldNotSendBirthdayRemindersFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, never()).sendEmail(any());
    verify(smsSender, never()).sendMessage(any());
  }

  @Test
  void shouldNotSendBirthdayRemindersFileRepoInvalidFile() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    when(config.getFileDirectory()).thenReturn("x.txt");
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, never()).sendEmail(any());
    verify(smsSender, never()).sendMessage(any());
  }
}