package kg.infocom.controller;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Organization;
import kg.infocom.model.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ProducerServiceController {

    @Autowired
    @Qualifier(value = "producerServiceDao")
    private AbstractDao producerServiceDao;

    @Autowired
    @Qualifier(value = "organizationDao")
    private AbstractDao organizationDao;

    @ModelAttribute("producer_services")
    public List<ProducerService> getAllServices() {
        return producerServiceDao.findAll();
    }

    @ModelAttribute("organizations")
    public List<Organization> getAllOrganizations() {
        return organizationDao.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/producer_service")
    public String get(Model model) {
        model.addAttribute("producer_service", new ProducerService());
        return "producer_service";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/producer_service/{action}/{id}")
    public String handleAction(@PathVariable Integer id, @PathVariable String action, Model model) {
        ProducerService producerService = (ProducerService) producerServiceDao.getById(id);
        if (action.equalsIgnoreCase("edit")) {
            model.addAttribute("producer_service", producerService);
            producerServiceDao.update(producerService);
            return "producer_service";
        }
        else if (action.equalsIgnoreCase("delete")) {
            producerServiceDao.delete(producerService);
        }
        return "redirect:/producer_service";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_producer_service")
    public String add(@ModelAttribute("producer_service") ProducerService producerService, BindingResult result) {
        producerServiceDao.update(producerService);
        return "redirect:/producer_service";
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
