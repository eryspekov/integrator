package kg.infocom.service;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by eryspekov on 31.10.16.
 */
public class SoapClient {

    public static void main(String args[]) {

        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            String ar = "https://address.infocom.kg/ws/AddressApi?wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), ar);

            printSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://address.infocom.kg/ws/";

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ws", serverURI);

        //SOAP Header
        SOAPHeader soapHeader = soapMessage.getSOAPHeader();
        SOAPElement token = soapHeader.addHeaderElement(new QName("token", "token"));
        token.addTextNode("0834ffc7d3d4883934708b1b270747df");

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

    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }

}


