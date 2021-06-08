package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.middleware.Models.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao implements UserDao
{
    public void SaveUser(User user)
    {
        persist(user);
    }

    public void DeleteUserByUsername(String username)
    {

    }

    public User GetUserWithCredentials(String username, String password)
    {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        return (User) criteria.uniqueResult();
    }

    public void UpdateUser(User user)
    {

    }
}
