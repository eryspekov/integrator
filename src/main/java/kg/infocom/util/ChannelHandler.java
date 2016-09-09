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
@Component
public class ChannelHandler {

    private static final String SERVICE_URL = "http://10.51.1.29:9091/ws/passport_pin";
    private static final String TOKEN = "fc31242d9c4d4a12fc516d1e806b7abb";

    public Message<String> getPersonDataByPin(Message<?> inMessage) {

        MessageHeaders headers = inMessage.getHeaders();
        Object payload = inMessage.getPayload();
        Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
        String id = "test";//(String)headers.get("id");

        //boolean isFound;
/*        if (id.equals("1")) {
            employeeList.getEmployee().add(new Employee(1, "John", "Doe"));
            isFound = true;
        }
        else {
            isFound = false;
        }
        if (isFound) {*/
            setReturnStatusAndMessage("0", "Success", responseHeaderMap);
        /*}
        else {
            setReturnStatusAndMessage("2", "Employee Not Found", employeeList, responseHeaderMap);
        }*/

        String text = id == null ? "id is null" : id;
        Message<String> message = new GenericMessage<String>(text, responseHeaderMap);

        return MessageBuilder.withPayload(text).copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("http_statusCode", HttpStatus.OK).build();

        //return message;
/*
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SERVICE_URL).queryParam("verbose", true);
        String response = target.queryParam("token", TOKEN)
                .queryParam("pin", message)
                .request().accept("application/json").get(String.class);

        client.close();

        System.out.println(response);
*/
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

    public void printText(Message<String> text) {
        System.out.println(text);

    }

}
