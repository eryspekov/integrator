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
            value = "/getPersonDataByPin/{method}",
            params = {"var1", "var2", "var3", "var4"},
            produces = "application/json")
    @ResponseBody
    public String getPersonDataByPin(
            @PathVariable("method") String method,
            @RequestParam(value = "var1", required = false) String var1,
            @RequestParam(value = "var2", required = false) String var2,
            @RequestParam(value = "var3", required = true) String var3,
            @RequestParam(value = "var4", required = false) String var4
            ) {
        //http://localhost:8080/integration/services/getPersonByParam?pin=214121987&name=edil
        //return surname+"-----"+name+":ok:"+pin;
        return "it's getPersonDataByPin";
    }


}
