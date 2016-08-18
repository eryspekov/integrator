package kg.infocom.controller;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Organization;
import kg.infocom.model.Software;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;

/**
 * Created by eryspekov on 17.08.16.
 */
@Controller
public class SoftwareController {

    @Autowired
    @Qualifier(value = "softwareDao")
    private AbstractDao softwareDao;

    @Autowired
    @Qualifier(value = "organizationDao")
    private AbstractDao organizationDao;

    @ModelAttribute("softwares")
    public List<Software> getAllSoftwares() {
        return softwareDao.findAll();
    }

    @ModelAttribute("organizations")
    public List<Organization> getAllOrganizations() {
        return organizationDao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/softwares")
    public String get(Model model) {
        model.addAttribute("software", new Software());
        return "softwares";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/softwares/{action}/{id}")
    public String handleAction(@PathVariable Integer id, @PathVariable String action, Model model) {
        Software software = (Software) softwareDao.getById(id);
        if (action.equalsIgnoreCase("edit")) {
            model.addAttribute("software", software);
            softwareDao.update(software);
            return "softwares";
        }
        else if (action.equalsIgnoreCase("delete")) {
            softwareDao.delete(software);
        }
        return "redirect:/softwares";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addsoftware")
    public String add(@ModelAttribute("software") Software software, BindingResult result) {
        softwareDao.update(software);
        return "redirect:/softwares";
    }


    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Organization.class, "organization", new PropertyEditorSupport() {

            public void setAsText(String text) {
                if (text instanceof String) {
                    Integer orgId = Integer.parseInt(text);
                    Organization organization = (Organization) organizationDao.getById(orgId);
                    setValue(organization);

                }
            }

            public String getAsText() {
                Object value = getValue();
                if (value != null) {
                    Organization organization = (Organization) value;
                    return organization.getName();
                }
                return null;
            }
        });
    }



}
