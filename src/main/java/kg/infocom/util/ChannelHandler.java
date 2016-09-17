package kg.infocom.util;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eryspekov on 25.08.16.
 */
//@Component
public class ChannelHandler {

    public Message<String> getPersonDataByPin(Message<?> inMessage) {

        Object payload = inMessage.getPayload();
        Map<String, Object> responseHeaderMap = new HashMap<String, Object>();

        setReturnStatusAndMessage("0", "Success", responseHeaderMap);

        String text = "id is null";
        Message<String> message = new GenericMessage<String>(text, responseHeaderMap);


        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://10.51.1.29:9091/ws/passport_pin").queryParam("verbose", true);
        String response = target.queryParam("token", "fc31242d9c4d4a12fc516d1e806b7abb")
                .queryParam("pin", payload)
                .request().accept("application/json").get(String.class);

        client.close();

        return MessageBuilder.withPayload(response).copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("http_statusCode", HttpStatus.OK).build();

    }

    private void setReturnStatusAndMessage(String status,
                                           String message,
                                           //EmployeeList employeeList,
                                           Map<String, Object> responseHeaderMap){

        //employeeList.setReturnStatus(status);
        //employeeList.setReturnStatusMsg(message);
        responseHeaderMap.put("Return-Status", status);
        responseHeaderMap.put("Return-Status-Msg", message);
    }

}
