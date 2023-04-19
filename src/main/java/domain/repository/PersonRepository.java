package domain.repository;


import domain.model.ClassifiedPersons;
import domain.model.Person;
import java.util.List;

public interface PersonRepository {

  List<Person> getPersonsWithBirthday();

  ClassifiedPersons getClassifiedPersons();

}
