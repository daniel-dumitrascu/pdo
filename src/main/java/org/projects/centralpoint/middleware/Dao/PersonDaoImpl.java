package org.projects.centralpoint.middleware.Dao;

import org.hibernate.Hibernate;
import org.projects.centralpoint.Defines.Types;
import org.projects.centralpoint.Extern.Fetching.FetchingStatus;
import org.projects.centralpoint.middleware.Models.Person;
import org.projects.centralpoint.middleware.Models.Video;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("personDao")
public class PersonDaoImpl extends AbstractDao implements PersonDao
{
    public boolean PersonExists(Person p)
    {
        boolean exists = false;
        List<Person> result = null;

        if(p != null)
        {
            CriteriaBuilder builder = getSession().getCriteriaBuilder();
            CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
            Root<Person> personRoot = criteria.from(Person.class);

            if(p.getTmdbId() > 0)
            {
                // Search by ID
                criteria.where(builder.equal(personRoot.get("tmdbId"), p.getTmdbId()));
                result = getSession().createQuery(criteria).getResultList();
            }
            else if(p.getName() != null && !p.getName().isEmpty())
            {
                // Search by name
                criteria.where(builder.equal(personRoot.get("name"), p.getName()));
                result = getSession().createQuery(criteria).getResultList();
            }

            if(result != null && result.size() > 0) exists = true;
        }

        return exists;
    }

    public Person getPerson(String personName)
    {
        Person p = null;

        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Root<Person> personRoot = criteria.from(Person.class);

        // Search by name
        criteria.where(builder.equal(personRoot.get("name"), personName));
        List<Person> result = getSession().createQuery(criteria).getResultList();

        if(result != null && result.size() > 0)
            p = result.get(0);

        return p;
    }

    public List<Person> getWaitingPersons()
    {
        List<Person> result = null;

        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Root<Person> personRoot = criteria.from(Person.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(personRoot.get("externFetchingStatus"), FetchingStatus.WAITING.getIntValue()));
        predicates.add(builder.and(builder.equal(personRoot.get("role"), Types.PeopleRole.ACTOR.getIntValue())));

        criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        result = getSession().createQuery(criteria).getResultList();

        return result;
    }

    public void updatePerson(Person person)
    {
        if(person != null)
        {
            update(person);
        }
    }
}
