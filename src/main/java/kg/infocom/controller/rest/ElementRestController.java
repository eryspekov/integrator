package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Element;
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
public class ElementRestController {
    @Autowired
    @Qualifier(value = "elementDao")
    private AbstractDao elementDao;


    @RequestMapping(value = "/rest/elements/", method = RequestMethod.GET)
    public ResponseEntity<List<Element>> listAllElements() {
        List<Element> users = elementDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<Element>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Element>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "/rest/elements/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody Element organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        elementDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/elements/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/rest/elements/{id}/", method = RequestMethod.PUT)
    public ResponseEntity<Element> updateUser(@PathVariable("id") int id, @RequestBody Element organization) {
        System.out.println("Updating User " + id);

        Element currentElement = (Element) elementDao.getById(id);

        if (currentElement==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Element>(HttpStatus.NOT_FOUND);
        }

        currentElement.setName(organization.getName());

        elementDao.update(currentElement);
        return new ResponseEntity<Element>(currentElement, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/rest/elements/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity<Element> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        Element organization= (Element) elementDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<Element>(HttpStatus.NOT_FOUND);
        }

        elementDao.delete(id);
        return new ResponseEntity<Element>(HttpStatus.NO_CONTENT);
    }
}
