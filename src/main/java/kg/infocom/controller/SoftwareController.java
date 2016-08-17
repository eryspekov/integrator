package kg.infocom.controller;

import kg.infocom.dao.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by eryspekov on 17.08.16.
 */
@Controller
public class SoftwareController {

    @Autowired
    @Qualifier(value = "softwareDao")
    private AbstractDao softwareDao;


    @RequestMapping("/")
    public String foo() { return "redirect:/softwares"; }


}
