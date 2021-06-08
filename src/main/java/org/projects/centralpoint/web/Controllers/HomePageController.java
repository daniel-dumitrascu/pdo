package org.projects.centralpoint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomePageController
{

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String GoToHomePageGetHandler()
    {
        //TODO to implement after seasons have been added

        // If we are logged in then this will return 'central'
        // Otherwise it will return 'welcome'
        return "central";
    }
}
