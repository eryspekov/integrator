package kg.infocom.service;

import com.google.gson.*;
import kg.infocom.dao.AbstractDao;
import kg.infocom.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.*;

/**
 * Created by eryspekov on 25.08.16.
 */
@Service("channelHandler")
@Transactional
public class ChannelHandler {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier(value = "consumerServiceDao")
    private AbstractDao consumerServiceDao;

    //@Secured("ROLE_REST_HTTP_USER")
    @Transactional
    public Message<?> handleConsumerRequest(Message<?> inMessage) {

        Map<String, String> map = (LinkedHashMap) inMessage.getPayload();
        String method = map.get("method");

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
                Set<ProducerArguments> arguments = ps.getArguments();
                Set<Element> elements = ps.getElements();

                String jsonData = getDataJson(map, url, arguments, ps.getWith_param());
                JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();

                for (Iterator<Element> elementIterator = elements.iterator(); elementIterator.hasNext(); ) {

                    Element element = elementIterator.next();

                    if (elemList.contains(element)) {

                        jsonObjectResult.addProperty(element.getName(), jsonObject.get(element.getName()).getAsString());

                    }
                }
            }
        }

        Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
        setReturnStatusAndMessage("0", "Success", responseHeaderMap);
        return new GenericMessage<String>(jsonObjectResult.toString(), responseHeaderMap);

    }

    public String getCatalog(Message<?> inMessage) {

        String catalog = (String) inMessage.getPayload();

        String query = "from ConsumerService cs where cs.method = :method";
        List<ConsumerService> csList = consumerServiceDao.getByNamedParam(query, "method", catalog);

        String jsonData = null;
        JsonParser parser = new JsonParser();

        for (int i = 0; i < csList.size(); i++) {

            ConsumerService cs = csList.get(i);

            Set<Element> elemList = cs.getElements();

            Set<ProducerService> producerServices = cs.getProducerServices();
            for (Iterator<ProducerService> psIterator = producerServices.iterator(); psIterator.hasNext(); ) {

                ProducerService ps = psIterator.next();
                String url = ps.getUrl();
                //Set<Argument> arguments = ps.getArguments();

                //jsonData = getDataJson("", url, arguments, ps.getWith_param());

            }
        }

        return jsonData;
    }

    public String getDataJson(Map<String, String> params, String url, Set<ProducerArguments> arguments, Boolean withParam) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);

        for (Iterator<ProducerArguments> it = arguments.iterator(); it.hasNext(); ) {
            ProducerArguments producerArguments = it.next();
            Argument arg = producerArguments.getArgument();
            Integer order_num = producerArguments.getOrder_num();

            String param = params.get("var"+order_num);

            if (withParam) {
                if (arg.getStatic()) {
                    target = target.queryParam(arg.getName(), arg.getValue());
                }
                else {
                    target = target.queryParam(arg.getName(), param);
                }
            }else {
                target = target.resolveTemplate(arg.getName(), param);
            }
        }
        String response = target.request().get(String.class);
        client.close();
        return response;
    }

    private void setReturnStatusAndMessage(String status, String message, Map<String, Object> responseHeaderMap){
        responseHeaderMap.put("Return-Status", status);
        responseHeaderMap.put("Return-Status-Msg", message);
    }

    public String getString() {
        return "it's string";
    }

    public void printString(String s) {
        System.out.println(s);
    }


}
