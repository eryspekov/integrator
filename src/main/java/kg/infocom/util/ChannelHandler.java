package kg.infocom.util;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericApplicationContext;
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
        String url = env.getProperty("zags.url");
        WebTarget target = client.target(url).queryParam("verbose", true);

        String token = env.getProperty("zags.token");
        String response = target.queryParam("token", token)
                .queryParam("pin", pin)
                .request().get(String.class);

        client.close();

        return response;

    }

    public String getPersonDataFromPassport(String pin) {

        Client client = ClientBuilder.newClient();
        String url = env.getProperty("passport.url");
        WebTarget target = client.target(url).queryParam("verbose", true);

        String token = env.getProperty("passport.token");
        String response = target.queryParam("token", token)
                .queryParam("pin", pin)
                .request().get(String.class);

        client.close();

        return response;

    }

}
