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

class BirthdayGreetingUseCaseTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private BirthdayGreetingUseCase birthdayGreetingUseCase;


  @BeforeEach
  void initTest () {
    PersonRepository personRepository = new PersonRepositoryFile(customFileReader);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayGreetingUseCase = new BirthdayGreetingUseCase(birthdayService, personRepository);

  }

  @Test
  void shouldNotSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023,1,1);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      mockedLocalDate.when(LocalDate::now).thenReturn(mockedDate);
      birthdayGreetingUseCase.sendBirthdayGreetings();

      verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
      verify(smsSender, never()).sendMessage(anyString(), anyString());
    }
  }

  @Test
  void shouldSendBirthdayGreetings() {
    LocalDate mockedDate = LocalDate.of(2023,2,28);
    try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
      String msg = "Happy birthday, dear %s!";
      String subject = "Happy birthday!";
      when(config.getBirthdayMessage()).thenReturn(msg);
      when(config.getBirthdaySubject()).thenReturn(subject);
      mockedLocalDate.when(LocalDate::now).thenReturn(mockedDate);
      birthdayGreetingUseCase.sendBirthdayGreetings();

      verify(emailSender, times(1)).sendEmail("mary.ann@foobar.com", "Happy birthday!", "Happy birthday, dear Mary!");
      verify(smsSender, times (1)).sendMessage(anyString(), anyString());
    }
  }
}