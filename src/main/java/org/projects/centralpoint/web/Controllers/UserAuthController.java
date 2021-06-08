package org.projects.centralpoint.web.Controllers;

import org.projects.centralpoint.middleware.Models.Person;
import org.projects.centralpoint.middleware.Models.User;
import org.projects.centralpoint.middleware.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserAuthController
{
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String LoginGetHandler()
    {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String LoginPostHandler(String username, String password)
    {
        if(username != null && !username.isEmpty() &&
           password != null && !password.isEmpty())
        {
            User user = userService.GetUserWithCredentials(username, password);
            if(user != null)
            {
                return "central";
            }
        }

        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String RegisterGetHandler()
    {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String RegisterPostHandler(String name, String username,
                                      String email, String password)
    {
        if(username != null && !username.isEmpty() &&
           password != null && !password.isEmpty())
        {
            User user = userService.GetUserWithCredentials(username, password);
            if(user == null)
            {
                Person newPerson = new Person();
                User newUser = new User();

                newPerson.setEmail(email);
                newPerson.setName(name);
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setPerson(newPerson);

                userService.SaveUser(newUser);
                return "login";
            }
        }
        return "register";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String LogoutGetHandler()
    {
        return "welcome";
    }
}

