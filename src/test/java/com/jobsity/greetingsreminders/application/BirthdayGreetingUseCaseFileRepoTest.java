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
import com.jobsity.greetingsreminders.infrastructure.dto.EmailDTO;
import com.jobsity.greetingsreminders.infrastructure.dto.SmsDTO;
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
class BirthdayGreetingUseCaseFileRepoTest {

  private final CustomFileReader customFileReader = new CustomFileReader();
  private final PersonTransformer personTransformer = new PersonTransformer();
  private final EntityManager entityManager = mock(EntityManager.class);
  private final Config config = mock(Config.class);
  private final EmailSender emailSender = mock(EmailSender.class);
  private final SmsSender smsSender = mock(SmsSender.class);
  private final DateUtils dateUtils = mock(DateUtils.class);
  private BirthdayGreetingUseCase birthdayGreetingUseCase;
  @Autowired
  private TestConfig testConfig;

  @BeforeEach
  void initTest() {
    PersonRepositoryFactory personRepositoryFactory = new PersonRepositoryFactoryImpl(config, dateUtils, customFileReader, personTransformer,
        entityManager);
    BirthdayService birthdayService = new BirthdayServiceImpl(emailSender, smsSender, config);
    birthdayGreetingUseCase = new BirthdayGreetingUseCase(birthdayService, personRepositoryFactory);
    when(config.getPersonRepositorySource()).thenReturn("File");
    when(config.getFileDirectory()).thenReturn(testConfig.getFileDirectory());
    when(config.getBirthdayMessage()).thenReturn(testConfig.getBirthdayMessage());
    when(config.getBirthdaySubject()).thenReturn(testConfig.getBirthdaySubject());
    when(config.getFileDelimiter()).thenReturn(testConfig.getFileDelimiter());
    when(config.getDateFormat()).thenReturn(testConfig.getDateFormat());
  }

  @Test
  void shouldSendBirthdayGreetingsFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 4, 21);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();
    String expectedMail = "mike.tire@foobar.com";
    String expectedSubject = "Happy birthday!";
    String expectedMessage = "Happy birthday, dear Mike!";
    String expectedPhoneNumber = "+532344";

    verify(emailSender, times(1)).sendEmail(new EmailDTO(expectedMail, expectedSubject, expectedMessage));
    verify(smsSender, times(1)).sendMessage(new SmsDTO(expectedPhoneNumber, expectedMessage));
  }

  @Test
  void shouldSendBirthdayGreetingsFileRepoFeb28Case() {
    LocalDate mockedDate = LocalDate.of(2023, 2, 28);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();
    String expectedMail = "mary.ann@foobar.com";
    String expectedSubject = "Happy birthday!";
    String expectedMessage = "Happy birthday, dear Mary!";
    String expectedPhoneNumber = "+594322";

    verify(emailSender, times(1)).sendEmail(new EmailDTO(expectedMail, expectedSubject, expectedMessage));
    verify(smsSender, times(1)).sendMessage(new SmsDTO(expectedPhoneNumber, expectedMessage));
  }

  @Test
  void shouldNotSendBirthdayGreetingsFileRepo() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    birthdayGreetingUseCase.sendBirthdayGreetings();

    verify(emailSender, never()).sendEmail(any());
    verify(smsSender, never()).sendMessage(any());
  }

  @Test
  void shouldNotSendBirthdayGreetingsFileRepoInvalidFile() {
    LocalDate mockedDate = LocalDate.of(2023, 1, 1);
    when(dateUtils.getCurrentDate()).thenReturn(mockedDate);
    when(config.getFileDirectory()).thenReturn("x.txt");
    birthdayGreetingUseCase.sendBirthdayGreetings();

    verify(emailSender, never()).sendEmail(any());
    verify(smsSender, never()).sendMessage(any());
  }

}