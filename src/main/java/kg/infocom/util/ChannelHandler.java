package kg.infocom.util;

import com.google.gson.*;
import kg.infocom.dao.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier(value = "consumerService")
    private AbstractDao consumerServiceDao;

    public Message<String> handleConsumerRequest(Message<?> inMessage) {

        String method = (String) inMessage.getPayload();
        String var = (String) inMessage.getHeaders().get("var");

        //поиск записи в таблице consumer_service по method
        //затем вытаскиваем все producer_services
        String url = env.getProperty("passport.url");

        //передаем аргументы, в том числе токены
        String token = env.getProperty("passport.token");
        String param = var;

        //по полученным параметрам получаем данные от продюсеров/поставщиков
        String passportJson = getDataJson(param, url, token);

        //
        String zagsJson = getDataJson(param, env.getProperty("zags.url"), env.getProperty("zags.token"));
        String asbJson = getAddress(env.getProperty("asb.url") + param);

        //выбираем нужные поля и создаем новый объект/игнорируем ненужные поля
        JsonParser parser = new JsonParser();

        JsonObject passportObject = parser.parse(passportJson).getAsJsonObject();
        JsonObject zagsObject = parser.parse(zagsJson).getAsJsonObject();
        JsonObject asbObject = parser.parse(asbJson).getAsJsonObject();

        //сведения какие поля включать, берутся из таблицы element
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pin", zagsObject.get("pin").getAsString());
        jsonObject.addProperty("name", zagsObject.get("name").getAsString());
        jsonObject.addProperty("surname", zagsObject.get("surname").getAsString());
        jsonObject.addProperty("patronymic", zagsObject.get("patronymic").getAsString());
        jsonObject.addProperty("docSeries", passportObject.get("passport_series").getAsString());
        jsonObject.addProperty("docNumber", passportObject.get("passport_number").getAsString());
        jsonObject.addProperty("address", asbObject.get("message").getAsString());

        return MessageBuilder.withPayload(jsonObject.toString()).copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("http_statusCode", HttpStatus.OK).build();

    }


    public String getDataJson(String param, String url, String token) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);
        String response = target.queryParam("token", token)
                .queryParam("pin", param)
                .request().get(String.class);
        client.close();
        return response;
    }

    public String getAddress(String url) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);
        String response = target.request().get(String.class);
        client.close();
        return response;
    }

}
