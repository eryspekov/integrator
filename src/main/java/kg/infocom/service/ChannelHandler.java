package kg.infocom.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kg.infocom.dao.AbstractDao;
import kg.infocom.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.util.*;

@Service("channelHandler")
public class ChannelHandler {

    @Autowired
    @Qualifier(value = "consumerServiceDao")
    private AbstractDao consumerServiceDao;

    @Autowired
    @Qualifier(value = "producerServiceDao")
    private AbstractDao producerServiceDao;

    @Autowired
    @Qualifier(value = "usersDao")
    private AbstractDao usersDao;

    @Autowired
    @Qualifier(value = "servicelogDao")
    private AbstractDao servicelogDao;

    @Autowired
    @Qualifier(value = "consumer.requestChannel")
    private MessageChannel consumerRequestChannel;

    public Message<String> handleConsumerRequest(Message<?> inMessage) {
        ServiceLog serviceLog = new ServiceLog();
        Map<String, String> map = (LinkedHashMap) inMessage.getPayload();
        // Get a set of the entries
        String request = "";
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getKey().equals("method"))
                continue;
            request += entry.getKey() + ":" + entry.getValue() + ";";
        }
        String method = map.get("method");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        serviceLog.setRequest(request);
        serviceLog.setMethod(method);
        String q = "from Users u where u.username=:username";
        List<Users> user = usersDao.getByNamedParam(q, "username", name);

//        String query = "from ConsumerService cs where cs.method = :method";
//        List<ConsumerService> csList = consumerServiceDao.getByNamedParam(query, "method", method);

        String query = "select cs from ConsumerService cs LEFT JOIN FETCH cs.users where cs.method = :method and :users MEMBER OF cs.users";
        String params[] = {"method", "users"};
        Object[] values = {method, user};
        List<ConsumerService> csList = consumerServiceDao.getByNamedParam(query, params, values);
        System.out.println(csList);
        JsonObject jsonObjectResult = new JsonObject();
        JsonParser parser = new JsonParser();
        if (csList.size() > 0) {
            for (int i = 0; i < csList.size(); i++) {
                ConsumerService cs = csList.get(i);
                Set<Element> elemList = cs.getElements();
                Set<ProducerService> producerServices = cs.getProducerServices();
                for (Iterator<ProducerService> psIterator = producerServices.iterator(); psIterator.hasNext(); ) {
                    ProducerService ps = psIterator.next();
                    String url = ps.getUrl();
                    Set<ProducerArguments> arguments = ps.getArguments();
                    Set<Element> elements = ps.getElements();
                    WebServiceType wsType = ps.getWebServiceType();
                    if (wsType.getName().equals("rest")) {
                        String jsonData = getDataJson(map, url, arguments, ps.getWith_param());
                        JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();
                        for (Element element : elements) {
                            if (elemList.contains(element)) {
                                jsonObjectResult.add(element.getName(), jsonObject.get(element.getName()));
                            }
                        }
                    } else {
                        try {
                            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
                            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(
                                    url,
                                    "ws",
                                    "token",
                                    "38c858bc765e78e394a2eef8e9701dce"),
                                    url);
                            printSOAPResponse(soapResponse);

                            soapConnection.close();
                        } catch (Exception e) {
                            System.err.println("Error occurred while sending SOAP Request to Server");
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            jsonObjectResult.addProperty("answer", "You don't have the permission to use the service");
        }
        System.out.println("jsonObjectResult.toString():" + jsonObjectResult.toString());
        serviceLog.setResponse(jsonObjectResult.toString());
        serviceLog.setUser(name);
        serviceLog.setLogdate(new Date());
        serviceLog.setIpaddress(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr());
        System.out.println("ServiceLog:" + serviceLog);
        servicelogDao.create(serviceLog);
        return MessageBuilder
                .withPayload(jsonObjectResult.toString())
                .copyHeadersIfAbsent(inMessage.getHeaders())
                .setHeader("HTTP_RESPONSE_HEADERS", HttpStatus.OK)
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .build();

    }

    public Message<String> getCatalog(Message<?> inMessage) {

        String method = (String) inMessage.getPayload();

        List<ConsumerService> csList = consumerServiceDao.getByNamedParam(
                "select cs from ConsumerService cs LEFT JOIN FETCH cs.users where cs.method = :method and :users MEMBER OF cs.users"
                , new String[]{"method", "users"}
                , new Object[]{method, getUsersList()});

        if (csList.size() == 0)
            return MessageBuilder.withPayload("Denied permission to use the service or the service not found")
                    .setHeader("http_statusCode", HttpStatus.NOT_IMPLEMENTED).build();

        Set<ProducerService> psList = csList.get(0).getProducerServices();
        if (psList.size() == 0)
            return MessageBuilder.withPayload("Producer service not found")
                    .setHeader("http_statusCode", HttpStatus.NOT_IMPLEMENTED).build();

        Iterator<ProducerService> iterator = psList.iterator();
        ProducerService ps = iterator.next();
        Map<String, String> map = null;
        String jsonData = getDataJson(map, ps.getUrl(), ps.getArguments(), ps.getWith_param());
        return MessageBuilder
                .withPayload(jsonData)
                .setHeader("HTTP_RESPONSE_HEADERS", HttpStatus.OK)
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .build();
    }

    public String getDataJson(Map<String, String> params, String url, Set<ProducerArguments> arguments, Boolean withParam) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url).queryParam("verbose", true);
        for (Iterator<ProducerArguments> it = arguments.iterator(); it.hasNext(); ) {
            ProducerArguments producerArguments = it.next();
            Argument arg = producerArguments.getArgument();
            Integer order_num = producerArguments.getOrder_num();
            String param = null;
            if (params != null) {
                param = params.get("var" + order_num);
            }
            if (withParam) {
                if (arg.getStatic()) {
                    target = target.queryParam(arg.getName(), arg.getValue());
                } else {
                    target = target.queryParam(arg.getName(), param);
                }
            } else {
                target = target.resolveTemplate(arg.getName(), param);
            }
        }
        String response = target.request().header("Authorization", "Basic " + DatatypeConverter.printBase64Binary("adm:adm".getBytes())).get(String.class);
        client.close();
        return response;
    }

    private SOAPMessage createSOAPRequest(String serverURL,
                                          String envelopeName,
                                          String header,
                                          String headerValue) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        //String serverURI = "http://address.infocom.kg/ws/";
        //envelopeName = "ws"
        //header = token
        //headerValue = "0834ffc7d3d4883934708b1b270747df"
        //body
        //  1 level
        //      level1.elem1="getAteName", "ws"
        //  2 level
        //      level2.elem1="id", 7410
        //      level2.elem2="currentDate", "2016-11-01"

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(envelopeName, serverURL);

        //SOAP Header
        SOAPHeader soapHeader = soapMessage.getSOAPHeader();
        SOAPElement token = soapHeader.addHeaderElement(new QName(header, header));
        token.addTextNode(headerValue);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getAteName", "ws");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("id");
        soapBodyElem1.addTextNode("7410");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("currentDate");
        soapBodyElem2.addTextNode("2016-11-01");

        soapMessage.saveChanges();

        return soapMessage;
    }

    private void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }

    public Message<String> handleProducerRequest(Message<?> inMessage) {

        //define subscribes consumers and than send them producer's payload
        MessageHeaders headers = inMessage.getHeaders();

        String method = headers.get("method", String.class);

        List<Users> userList = getUsersList();
        if (userList.size() == 0)
            return MessageBuilder.withPayload("User not found")
                    .setHeader("http_statusCode", HttpStatus.NOT_IMPLEMENTED).build();

        List<ProducerService> psList = producerServiceDao.getByNamedParam(
                "from ProducerService where method = :method and users = :users",
                new String[]{"method", "users"},
                new Object[]{method, userList.get(0)});

        if (psList.size() == 0)
            return MessageBuilder.withPayload("Denied permission to use the service or the service not found")
                    .setHeader("http_statusCode", HttpStatus.NOT_IMPLEMENTED).build();

        ProducerService ps = psList.get(0);
        sendToConsumerMessage(ps, inMessage.getPayload());

        return MessageBuilder.withPayload("Your request has been successfully submitted")
                .setHeader("http_statusCode", HttpStatus.OK).build();

    }

    public void commitReplyStatus(Message<?> inMessage) {
        MessageHeaders headers = inMessage.getHeaders();
        if (headers.containsKey("http_statusCode")) {
            HttpStatus http_statusCode = headers.get("http_statusCode", HttpStatus.class);
            if (http_statusCode == HttpStatus.OK) {
                //write to db success status
            }

        }
    }

    public void pollerGetProducerResponse(Message<?> inMessage) {

        List<ProducerService> psList = producerServiceDao.getByNamedParam("from ProducerService where autoStartup = :start", "start", true);

        for (ProducerService ps : psList) {

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(ps.getUrl()).queryParam("verbose", true);
            String response = target.request().get(String.class);
            client.close();

            sendToConsumerMessage(ps, response);
        }
    }

    private void sendToConsumerMessage(ProducerService ps, Object response) {
        Set<ConsumerService> csList = ps.getConsumerServices();
        for (ConsumerService consumerService : csList) {
            Message<?> message = MessageBuilder.withPayload(response)
                    .setHeader("HTTP_RESPONSE_HEADERS", HttpStatus.OK)
                    .setHeader("Content-Type", "application/json;charset=UTF-8")
                    .setHeader("consumerUrl", consumerService.getUrl())
                    .build();
            consumerRequestChannel.send(message);
        }
    }

    private List<Users> getUsersList() {
        List<Users> userList = usersDao.getByNamedParam(
                "from Users u where u.username=:username",
                "username",
                SecurityContextHolder.getContext().getAuthentication().getName());
        return userList;
    }

}
