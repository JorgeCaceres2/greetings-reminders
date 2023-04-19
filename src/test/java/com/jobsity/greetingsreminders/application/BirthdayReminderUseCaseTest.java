package com.jobsity.greetingsreminders.application;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jobsity.greetingsreminders.domain.repository.PersonRepository;
import com.jobsity.greetingsreminders.domain.service.BirthdayService;
import com.jobsity.greetingsreminders.infrastructure.configuration.Config;
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositoryFile;
import com.jobsity.greetingsreminders.infrastructure.service.BirthdayServiceImpl;
import com.jobsity.greetingsreminders.infrastructure.shared.CustomFileReader;
import com.jobsity.greetingsreminders.infrastructure.shared.EmailSender;
import com.jobsity.greetingsreminders.infrastructure.shared.SmsSender;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class BirthdayReminderUseCaseTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private BirthdayReminderUseCase birthdayReminderUseCase;


  @BeforeEach
  void initTest () {
    PersonRepository personRepository = new PersonRepositoryFile(customFileReader, config);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayReminderUseCase = new BirthdayReminderUseCase(birthdayService, personRepository);
    when(config.getFileDirectory()).thenReturn("friend-list.txt");
  }

  @Test
  void shouldSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023,2,28);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      when(config.getReminderMessage()).thenReturn("Dear '%s', \\nToday is '%s' '%s''s birthday.\\n\\tDon't forget to sem him a message!");
      when(config.getReminderSubject()).thenReturn("Birthday Reminder");
      mockedLocalDate.when(LocalDate::now).thenReturn(mockedDate);
      birthdayReminderUseCase.sendBirthdayReminders();

      verify(emailSender, times(2)).sendEmail(anyString(), anyString(), anyString());
      verify(smsSender, times (2)).sendMessage(anyString(), anyString());
    }
  }

  @Test
  void shouldNotSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023,1,1);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      mockedLocalDate.when(LocalDate::now).thenReturn(mockedDate);
      birthdayReminderUseCase.sendBirthdayReminders();

      verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
      verify(smsSender, never()).sendMessage(anyString(), anyString());
    }
  }
}