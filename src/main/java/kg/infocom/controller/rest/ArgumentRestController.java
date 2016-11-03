package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Argument;
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
@RequestMapping("/rest/arguments/")
public class ArgumentRestController {
    @Autowired
    @Qualifier(value = "argumentDao")
    private AbstractDao argumentDao;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Argument>> listAllArguments() {
        List<Argument> users = argumentDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<Argument>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Argument>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<Argument> getById(@PathVariable("id") Integer id) {
        Argument users = (Argument) argumentDao.getById(id);
        if(users==null){
            return new ResponseEntity<Argument>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Argument>(users, HttpStatus.OK);
    }
    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody Argument organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        argumentDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/arguments/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Argument> updateUser(@PathVariable("id") int id, @RequestBody Argument organization) {
        System.out.println("Updating User " + id);

        Argument currentArgument = (Argument) argumentDao.getById(id);

        if (currentArgument==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Argument>(HttpStatus.NOT_FOUND);
        }

        currentArgument.setName(organization.getName());

        argumentDao.update(currentArgument);
        return new ResponseEntity<Argument>(currentArgument, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Argument> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        Argument organization= (Argument) argumentDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<Argument>(HttpStatus.NOT_FOUND);
        }

        argumentDao.delete(id);
        return new ResponseEntity<Argument>(HttpStatus.NO_CONTENT);
    }
}
