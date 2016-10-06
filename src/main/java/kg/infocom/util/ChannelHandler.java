package kg.infocom.util;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by eryspekov on 25.08.16.
 */
@Configuration
@PropertySource("classpath:wsPath.properties")
public class ChannelHandler {

    @Autowired
    private Environment env;

    public Message<String> getPersonDataByPin(Message<?> inMessage) {

        Object payload = inMessage.getPayload();

        String passportJson = getDataJson((String) payload, env.getProperty("passport.url"), env.getProperty("passport.token"));
        String zagsJson = getDataJson((String) payload, env.getProperty("zags.url"), env.getProperty("zags.token"));

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

    public String getDataJson(String payload, String url, String token) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);
        String response = target.queryParam("token", token)
                .queryParam("pin", payload)
                .request().get(String.class);
        client.close();
        return response;
    }

}
