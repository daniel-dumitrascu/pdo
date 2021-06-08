package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.middleware.Models.Person;
import org.projects.centralpoint.middleware.Models.Video;

import java.util.List;

public interface PersonService
{
    boolean personExists(Person p);

    Person getPerson(String personName);

    List<Person> getWaitingPersons();

    void updatePerson(Person person);
}