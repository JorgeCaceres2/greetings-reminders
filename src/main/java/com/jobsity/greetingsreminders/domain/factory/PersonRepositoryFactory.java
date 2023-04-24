package com.jobsity.greetingsreminders.domain.factory;

import com.jobsity.greetingsreminders.domain.repository.PersonRepository;

public interface PersonRepositoryFactory {

  PersonRepository getRepository();

}
