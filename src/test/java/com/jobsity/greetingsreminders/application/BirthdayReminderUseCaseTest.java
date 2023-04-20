package com.jobsity.greetingsreminders.application;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
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

class BirthdayReminderUseCaseTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private final String message = "Dear '%s', \\nToday is '%s' '%s''s birthday.\\n\\tDon't forget to sem him a message!";
  private final String subject = "Birthday Reminder";
  private BirthdayReminderUseCase birthdayReminderUseCase;

  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactory(config, dateUtils, customFileReader, personTransformer);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayReminderUseCase = new BirthdayReminderUseCase(birthdayService, personRepositoryFactory);
    when(config.getFileDirectory()).thenReturn("friend-list.txt");
  }

  @Test
  void shouldSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023, 4, 17);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(config.getReminderMessage()).thenReturn(message);
    when(config.getReminderSubject()).thenReturn(subject);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(anyString(), anyString(), anyString());
    verify(smsSender, times(2)).sendMessage(anyString(), anyString());
  }

  @Test
  void shouldSendBirthdayGreetingsFeb28Case() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(config.getReminderMessage()).thenReturn(message);
    when(config.getReminderSubject()).thenReturn(subject);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, times(2)).sendEmail(anyString(), anyString(), anyString());
    verify(smsSender, times(2)).sendMessage(anyString(), anyString());
  }

  @Test
  void shouldNotSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayReminderUseCase.sendBirthdayReminders();

    verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    verify(smsSender, never()).sendMessage(anyString(), anyString());
  }
}