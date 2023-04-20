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
import com.jobsity.greetingsreminders.infrastructure.repository.PersonRepositorySQLite;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class BirthdayGreetingUseCaseTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private BirthdayGreetingUseCase birthdayGreetingUseCase;

  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactory(config, dateUtils, customFileReader, personTransformer);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayGreetingUseCase = new BirthdayGreetingUseCase(birthdayService, personRepositoryFactory);
    when(config.getFileDirectory()).thenReturn("friend-list.txt");
  }

  @Test
  void shouldSendBirthdayGreetingsFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    String msg = "Happy birthday, dear %s!";
    String subject = "Happy birthday!";
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(config.getBirthdayMessage()).thenReturn(msg);
    when(config.getBirthdaySubject()).thenReturn(subject);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();

    verify(emailSender, times(1)).sendEmail("mary.ann@foobar.com", "Happy birthday!", "Happy birthday, dear Mary!");
    verify(smsSender, times(1)).sendMessage("+594322", "Happy birthday, dear Mary!");
  }

  @Test
  void shouldNotSendBirthdayGreetingsFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();

    verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    verify(smsSender, never()).sendMessage(anyString(), anyString());
  }

//  @Test
//  void shouldSendBirthdayGreetingsSQLiteRepo() {
//    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
//    String msg = "Happy birthday, dear %s!";
//    String subject = "Happy birthday!";
//    when(config.getPersonRepositorySource()).thenReturn("SQLite");
//    when(config.getBirthdayMessage()).thenReturn(msg);
//    when(config.getBirthdaySubject()).thenReturn(subject);
//    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
//    birthdayGreetingUseCase.sendBirthdayGreetings();
//
//    verify(emailSender, times(1)).sendEmail("mary.ann@foobar.com", "Happy birthday!", "Happy birthday, dear Mary!");
//    verify(smsSender, times(1)).sendMessage("+594322", "Happy birthday, dear Mary!");
//  }
//
//  @Test
//  void shouldNotSendBirthdayGreetingsSQLiteRepo() {
//    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
//    when(config.getPersonRepositorySource()).thenReturn("SQLite");
//    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
//    birthdayGreetingUseCase.sendBirthdayGreetings();
//
//    verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
//    verify(smsSender, never()).sendMessage(anyString(), anyString());
//  }

}