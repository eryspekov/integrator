package kg.infocom.controller.rest;

import kg.infocom.dao.AbstractDao;
import kg.infocom.model.ProducerArguments;
import kg.infocom.model.ProducerArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by Admin on 11/3/2016.
 */
@RestController
@RequestMapping("/rest/producersarg/")
public class ProducerArgRestController {
    @Autowired
    @Qualifier(value = "producerArgDao")
    private AbstractDao producerArgDao;
    

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProducerArguments>> listAllProducerArgumentss() {
        List<ProducerArguments> users = producerArgDao.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<ProducerArguments>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<ProducerArguments>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<ProducerArguments> getById(@PathVariable("id") Integer id) {
        ProducerArguments users = (ProducerArguments)producerArgDao.getById(id);
        if(users==null){
            return new ResponseEntity<ProducerArguments>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<ProducerArguments>(users, HttpStatus.OK);
    }

    @RequestMapping( method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody ProducerArguments ProducerArguments, UriComponentsBuilder ucBuilder) {
        producerArgDao.update(ProducerArguments);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/ProducerArgumentss/{id}").buildAndExpand(ProducerArguments.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProducerArguments> updateUser(@PathVariable("id") int id, @RequestBody ProducerArguments producerArguments) {
        System.out.println("Updating User " + id);

        ProducerArguments currentProducerArguments = (ProducerArguments) producerArgDao.getById(id);

        if (currentProducerArguments==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<ProducerArguments>(HttpStatus.NOT_FOUND);
        }

        currentProducerArguments.setArgument(producerArguments.getArgument());
        currentProducerArguments.setOrder_num(producerArguments.getOrder_num());

        producerArgDao.update(currentProducerArguments);
        return new ResponseEntity<ProducerArguments>(currentProducerArguments, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProducerArguments> deleteUser(@PathVariable("id") int id) {
        System.out.println("Fetching & Deleting User with id " + id);

        ProducerArguments ProducerArguments= (ProducerArguments) producerArgDao.getById(id);
        if (ProducerArguments == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<ProducerArguments>(HttpStatus.NOT_FOUND);
        }

        producerArgDao.delete(id);
        return new ResponseEntity<ProducerArguments>(HttpStatus.NO_CONTENT);
    }
}
