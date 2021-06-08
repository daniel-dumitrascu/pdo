package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.middleware.Dao.UserDao;
import org.projects.centralpoint.middleware.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    public void SaveUser(User user)
    {
        userDao.SaveUser(user);
    }

    public void DeleteUserByUsername(String username)
    {
        userDao.DeleteUserByUsername(username);
    }

    public User GetUserWithCredentials(String username, String password)
    {
        return userDao.GetUserWithCredentials(username, password);
    }

    public void UpdateUser(User user)
    {
        userDao.UpdateUser(user);
    }
}
