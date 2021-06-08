package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.middleware.Models.User;

public interface UserService
{
    void SaveUser(User user);

    void DeleteUserByUsername(String username);

    User GetUserWithCredentials(String username, String password);

    void UpdateUser(User user);
}
