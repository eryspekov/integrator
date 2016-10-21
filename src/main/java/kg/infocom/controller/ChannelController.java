package kg.infocom.controller;

import kg.infocom.dao.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by eryspekov on 25.08.16.
 */
@Controller
@RequestMapping(value = "services")
public class ChannelController {

    @Autowired
    @Qualifier(value = "requestChannel")
    private MessageChannel requestChannel;

    @Autowired
    @Qualifier(value = "consumerServiceDao")
    private AbstractDao consumerServiceDao;

    @RequestMapping(method = RequestMethod.GET,
            value = "/getCatalogs/{name}",
            params = {"pin", "name"})//,
            //consumes = "application/json")
    //headers = "Accept=application/json")
    @ResponseBody
    public String getPersonDataByPin(
            @PathVariable("name") String name,
            @RequestParam("pin") String pin,
            @RequestParam(value = "surname", required = false) String surname) {
        //http://localhost:8080/integration/services/getPersonByParam?pin=214121987&name=edil
        return surname+"-----"+name+":ok:"+pin;
    }


}
