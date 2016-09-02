package kg.infocom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by eryspekov on 25.08.16.
 */
@Controller
public class ChannelController {

    @Autowired
    @Qualifier(value = "getPersonDataByPin")
    private MessageChannel getPersonDataByPin;

    @RequestMapping(method = RequestMethod.GET, value = "/{method}/{value}")
    public void getPersonDataByPin(@PathVariable String method, @PathVariable String value) {

        if (method.equals("getPersonDataByPin")) {
            getPersonDataByPin.send(new GenericMessage<String>(value));
            //getPersonDataByPin.
        }
    }

}
