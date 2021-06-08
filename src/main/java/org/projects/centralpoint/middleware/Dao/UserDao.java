package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.middleware.Models.User;


public interface UserDao
{
    void SaveUser(User user);

    void DeleteUserByUsername(String username);

    User GetUserWithCredentials(String username, String password);

    void UpdateUser(User user);
}
