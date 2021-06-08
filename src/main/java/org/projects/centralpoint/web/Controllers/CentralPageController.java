package org.projects.centralpoint.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CentralPageController
{
    @RequestMapping(value = "/central", method = RequestMethod.GET)
    public String CentralPageGetHandler()
    {
        return "central";
    }
}
