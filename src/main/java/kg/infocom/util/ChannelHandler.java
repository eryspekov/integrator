package kg.infocom.util;

import com.google.gson.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by eryspekov on 25.08.16.
 */
@PropertySource(value = "classpath:wsPath.properties")
public class ChannelHandler {

    public Message<String> getPersonDataByPin(Message<?> inMessage) {

        Object payload = inMessage.getPayload();

        String passportJson = getPersonDataFromPassport((String) payload);
        String zagsJson = getPersonDataFromZags((String) payload);

        JsonParser parser = new JsonParser();

        JsonObject passportObject = parser.parse(passportJson).getAsJsonObject();
        JsonElement passport_number = passportObject.get("passport_number");

        JsonObject zagsObject = parser.parse(zagsJson).getAsJsonObject();
        zagsObject.addProperty("docSeries", passportObject.get("passport_series").getAsString());
        zagsObject.addProperty("docNumber", passportObject.get("passport_number").getAsString());
        JsonElement pinGenerationDate = zagsObject.remove("pinGenerationDate");

        return MessageBuilder.withPayload(zagsObject.toString()).copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("http_statusCode", HttpStatus.OK).build();

    }

    public String getPersonDataFromZags(String pin) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://zagstest.infocom.kg/ws/passport_pin").queryParam("verbose", true);

        String response = target.queryParam("token", "fb8ae3567cb8351e82f96615df005b9a")
                .queryParam("pin", pin)
                .request().get(String.class);

        client.close();

        return response;

    }

    public void getAddress() {
        String token = "0834ffc7d3d4883934708b1b270747df";
    }

    public String getPersonDataFromPassport(String pin) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://10.51.1.29:9091/ws/passport_pin").queryParam("verbose", true);

        String response = target.queryParam("token", "fc31242d9c4d4a12fc516d1e806b7abb")
                .queryParam("pin", pin)
                .request().get(String.class);

        client.close();

        return response;

    }

}
