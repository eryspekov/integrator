package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kbakytbekov on 20.10.2016.
 */
@RestController
@RequestMapping("/rest/producers/")
public class ProducerServiceRestController implements Serializable {
    @Autowired
    @Qualifier(value = "producerServiceDao")
    private AbstractDao producerServiceDao;


    @RequestMapping( method = RequestMethod.GET)
    public ResponseEntity<List<ProducerService>> listAllProducerServices() {
        List<ProducerService> users = producerServiceDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<ProducerService>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<ProducerService>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<ProducerService> getById(@PathVariable("id") Integer id) {
        ProducerService users = (ProducerService) producerServiceDao.getById(id);
        if(users==null){
            return new ResponseEntity<ProducerService>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<ProducerService>(users, HttpStatus.OK);
    }
    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody ProducerService organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        producerServiceDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/producers/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProducerService> updateUser(@PathVariable("id") int id, @RequestBody ProducerService organization) {
        System.out.println("Updating User " + id);

        ProducerService currentProducerService = (ProducerService) producerServiceDao.getById(id);

        if (currentProducerService==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<ProducerService>(HttpStatus.NOT_FOUND);
        }

        currentProducerService.setName(organization.getName());
        currentProducerService.setArguments(organization.getArguments());
        currentProducerService.setElements(organization.getElements());
        currentProducerService.setOrganization(organization.getOrganization());
        currentProducerService.setUrl(organization.getUrl());
        currentProducerService.setWebServiceType(organization.getWebServiceType());
        currentProducerService.setWith_param(organization.getWith_param());

        producerServiceDao.update(currentProducerService);
        return new ResponseEntity<ProducerService>(currentProducerService, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProducerService> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        ProducerService organization= (ProducerService) producerServiceDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ProducerService>(HttpStatus.NOT_FOUND);
        }

        producerServiceDao.delete(organization);
        return new ResponseEntity<ProducerService>(HttpStatus.NO_CONTENT);
    }
}
