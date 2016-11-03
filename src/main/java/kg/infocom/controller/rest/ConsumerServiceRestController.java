package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by kbakytbekov on 20.10.2016.
 */
@RestController
@RequestMapping("/rest/consumers/")
public class ConsumerServiceRestController {
    @Autowired
    @Qualifier(value = "consumerServiceDao")
    private AbstractDao consumerServiceDao;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ConsumerService>> listAllConsumerServices() {
        List<ConsumerService> users = consumerServiceDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<ConsumerService>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<ConsumerService>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<ConsumerService> getById(@PathVariable("id") Integer id) {
        ConsumerService users = (ConsumerService) consumerServiceDao.getById(id);
        if(users==null){
            return new ResponseEntity<ConsumerService>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<ConsumerService>(users, HttpStatus.OK);
    }

    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody ConsumerService organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        consumerServiceDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/consumers/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<ConsumerService> updateUser(@PathVariable("id") int id, @RequestBody ConsumerService organization) {
        System.out.println("Updating User " + id);

        ConsumerService currentConsumerService = (ConsumerService) consumerServiceDao.getById(id);

        if (currentConsumerService==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<ConsumerService>(HttpStatus.NOT_FOUND);
        }

        currentConsumerService.setName(organization.getName());

        consumerServiceDao.update(currentConsumerService);
        return new ResponseEntity<ConsumerService>(currentConsumerService, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ConsumerService> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        ConsumerService organization= (ConsumerService) consumerServiceDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ConsumerService>(HttpStatus.NOT_FOUND);
        }

        consumerServiceDao.delete(id);
        return new ResponseEntity<ConsumerService>(HttpStatus.NO_CONTENT);
    }
}
