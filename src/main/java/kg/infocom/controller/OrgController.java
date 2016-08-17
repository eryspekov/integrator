package kg.infocom.controller;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by eryspekov on 17.08.16.
 */
@Controller
public class OrgController {

    @Autowired
    @Qualifier(value = "organizationDao")
    private AbstractDao organizationDao;

    @RequestMapping("/")
    public String foo() {
        return "redirect:/organizations";
    }

    @ModelAttribute("organizations")
    public List<Organization> getAllProjects() {
        return organizationDao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/organizations")
    public String get(Model model) {
        model.addAttribute("organization", new Organization());
        return "organizations";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/organizations/{action}/{id}")
    public String handleAction(@PathVariable Integer id, @PathVariable String action, Model model) {
        Organization organization = (Organization) organizationDao.getById(id);
        if (action.equalsIgnoreCase("edit")) {
            model.addAttribute("organization", organization);
            return "organizations";
        } else if (action.equalsIgnoreCase("delete")) {
            organizationDao.delete(organization);
        }
        return "redirect:/organizations";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute("project") Organization organization, BindingResult result) {
        //projectValidator.validate(organization, result);
        //if (result.hasErrors())
        //    return "/projects";

        organizationDao.update(organization);
        return "redirect:/organizations";

    }

}
