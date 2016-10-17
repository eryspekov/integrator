package kg.infocom.util;

import com.google.gson.*;
import kg.infocom.dao.AbstractDao;
import kg.infocom.model.Argument;
import kg.infocom.model.ConsumerService;
import kg.infocom.model.Element;
import kg.infocom.model.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by eryspekov on 25.08.16.
 */
@Service
public class ChannelHandler {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier(value = "consumerServiceDao")
    private AbstractDao consumerServiceDao;

    public Message<String> handleConsumerRequest(Message<?> inMessage) {

        String method = (String) inMessage.getPayload();
        String param = (String) inMessage.getHeaders().get("var");

        String query = "from ConsumerService cs where cs.method = :method";
        List<ConsumerService> csList = consumerServiceDao.getByNamedParam(query, "method", method);

        JsonObject jsonObjectResult = new JsonObject();
        JsonParser parser = new JsonParser();

        for (int i = 0; i < csList.size(); i++) {

            ConsumerService cs = csList.get(i);

            Set<Element> elemList = cs.getElements();

            Set<ProducerService> producerServices = cs.getProducerServices();
            for (Iterator<ProducerService> psIterator = producerServices.iterator(); psIterator.hasNext(); ) {

                ProducerService ps = psIterator.next();
                String url = ps.getUrl();
                Set<Argument> arguments = ps.getArguments();
                Set<Element> elements = ps.getElements();

                String jsonData = getDataJson(param, url, arguments);
                JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();

                for (Iterator<Element> elementIterator = elements.iterator(); elementIterator.hasNext(); ) {

                    Element element = elementIterator.next();

                    if (elemList.contains(element)) {

                        jsonObjectResult.addProperty(element.getName(), jsonObject.get(element.getName()).getAsString());

                    }
                }
            }
        }

        return MessageBuilder.withPayload(jsonObjectResult.toString()).copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("http_statusCode", HttpStatus.OK).build();

    }

    public String getDataJson(String param, String url, Set<Argument> arguments) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);

        for (Iterator<Argument> it = arguments.iterator(); it.hasNext(); ) {
            Argument arg = it.next();
            if (arg.getStatic()) {
                target = target.queryParam(arg.getName(), arg.getValue());
            }else {
                target = target.queryParam(arg.getName(), param);
            }
        }
        String response = target.request().get(String.class);
        client.close();
        return response;

    }

}
