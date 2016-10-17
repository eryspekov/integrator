package kg.infocom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by eryspekov on 25.08.16.
 */
@Controller
@RequestMapping(value = "services")
public class ChannelController {

    @Autowired
    @Qualifier(value = "requestChannel")
    private MessageChannel requestChannel;

    @RequestMapping(method = RequestMethod.GET, value = "/getPersonByParam?{value}")
    public String getPersonDataByPin(@PathVariable String value) {
        //boolean send = requestChannel.send(new GenericMessage<String>(value));
        return "ok:"+value;
        }


    //@ServiceActivator
}
