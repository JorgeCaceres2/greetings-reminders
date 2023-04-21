package com.jobsity.greetingsreminders;

import com.jobsity.greetingsreminders.application.BirthdayGreetingUseCase;
import com.jobsity.greetingsreminders.application.BirthdayReminderUseCase;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyAppRunner implements ApplicationRunner {

  private final BirthdayGreetingUseCase birthdayGreetingUseCase;
  private final BirthdayReminderUseCase birthdayReminderUseCase;

  public MyAppRunner(BirthdayGreetingUseCase birthdayGreetingUseCase,
      BirthdayReminderUseCase birthdayReminderUseCase) {
    this.birthdayGreetingUseCase = birthdayGreetingUseCase;
    this.birthdayReminderUseCase = birthdayReminderUseCase;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
      birthdayGreetingUseCase.sendBirthdayGreetings();
      birthdayReminderUseCase.sendBirthdayReminders();
  }
}
