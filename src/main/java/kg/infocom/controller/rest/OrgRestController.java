package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Organization;
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
@RequestMapping("/rest/organizations")
public class OrgRestController implements Serializable {
    @Autowired
    @Qualifier(value = "organizationDao")
    private AbstractDao organizationDao;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Organization>> listAllOrganizations() {
        List<Organization> users = organizationDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<Organization>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Organization>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<Organization> getById(@PathVariable("id") Integer id) {
        Organization users = (Organization)organizationDao.getById(id);
        if(users==null){
            return new ResponseEntity<Organization>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Organization>(users, HttpStatus.OK);
    }

    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody Organization organization, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + organization.getName());

        organizationDao.update(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/organizations/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Organization> updateUser(@PathVariable("id") int id, @RequestBody Organization organization) {
        System.out.println("Updating User " + id);

        Organization currentOrganization = (Organization) organizationDao.getById(id);

        if (currentOrganization==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<Organization>(HttpStatus.NOT_FOUND);
        }

        currentOrganization.setName(organization.getName());

        organizationDao.update(currentOrganization);
        return new ResponseEntity<Organization>(currentOrganization, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Organization> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        Organization organization= (Organization) organizationDao.getById(id);
        if (organization == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<Organization>(HttpStatus.NOT_FOUND);
        }

        organizationDao.delete(id);
        return new ResponseEntity<Organization>(HttpStatus.NO_CONTENT);
    }
}
