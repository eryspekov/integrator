package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by kbakytbekov on 20.10.2016.
 */
public class ProducerServiceRestController {
    @Autowired
    @Qualifier(value = "producerServiceDao")
    private AbstractDao producerServiceDao;


    @RequestMapping(value = "/rest/producers/", method = RequestMethod.GET)
    public ResponseEntity<List<ProducerService>> listAllProducerServices() {
        List<ProducerService> users = producerServiceDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<ProducerService>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<ProducerService>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "/rest/producers/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody ProducerService organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        producerServiceDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/producers/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/rest/producers/{id}/", method = RequestMethod.PUT)
    public ResponseEntity<ProducerService> updateUser(@PathVariable("id") int id, @RequestBody ProducerService organization) {
        System.out.println("Updating User " + id);

        ProducerService currentProducerService = (ProducerService) producerServiceDao.getById(id);

        if (currentProducerService==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<ProducerService>(HttpStatus.NOT_FOUND);
        }

        currentProducerService.setName(organization.getName());

        producerServiceDao.update(currentProducerService);
        return new ResponseEntity<ProducerService>(currentProducerService, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/rest/producers/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity<ProducerService> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        ProducerService organization= (ProducerService) producerServiceDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ProducerService>(HttpStatus.NOT_FOUND);
        }

        producerServiceDao.delete(id);
        return new ResponseEntity<ProducerService>(HttpStatus.NO_CONTENT);
    }
}
