package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.middleware.Dao.PersonDao;
import org.projects.centralpoint.middleware.Models.Person;
import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("personService")
@Transactional
public class PersonServiceImpl implements PersonService
{
    @Autowired
    private PersonDao personDao;

    public boolean personExists(Person p)
    {
        return personDao.PersonExists(p);
    }

    public Person getPerson(String personName)
    {
        return personDao.getPerson(personName);
    }

    public List<Person> getWaitingPersons()
    {
        return personDao.getWaitingPersons();
    }

    public void updatePerson(Person person)
    {
        personDao.updatePerson(person);
    }
}
