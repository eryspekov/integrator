package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.WebServiceType;
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
public class WsTypeRestController {
    @Autowired
    @Qualifier(value = "wsDao")
    private AbstractDao wsDao;


    @RequestMapping(value = "/rest/wss/", method = RequestMethod.GET)
    public ResponseEntity<List<WebServiceType>> listAllWebServiceTypes() {
        List<WebServiceType> users = wsDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<WebServiceType>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<WebServiceType>>(users, HttpStatus.OK);
    }
    @RequestMapping(value = "/rest/wss/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody WebServiceType organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        wsDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/wss/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/rest/wss/{id}/", method = RequestMethod.PUT)
    public ResponseEntity<WebServiceType> updateUser(@PathVariable("id") int id, @RequestBody WebServiceType organization) {
        System.out.println("Updating User " + id);

        WebServiceType currentWebServiceType = (WebServiceType) wsDao.getById(id);

        if (currentWebServiceType==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<WebServiceType>(HttpStatus.NOT_FOUND);
        }

        currentWebServiceType.setName(organization.getName());

        wsDao.update(currentWebServiceType);
        return new ResponseEntity<WebServiceType>(currentWebServiceType, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/rest/wss/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity<WebServiceType> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        WebServiceType organization= (WebServiceType) wsDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<WebServiceType>(HttpStatus.NOT_FOUND);
        }

        wsDao.delete(id);
        return new ResponseEntity<WebServiceType>(HttpStatus.NO_CONTENT);
    }
}