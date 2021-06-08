package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.middleware.Models.Person;

import java.util.List;

public interface PersonDao
{
    boolean PersonExists(Person p);

    Person getPerson(String personName);

    List<Person> getWaitingPersons();

    void updatePerson(Person person);
}
