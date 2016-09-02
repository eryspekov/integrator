package kg.infocom.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by eryspekov on 25.08.16.
 */
public class ChannelHandler {

    private static final String SERVICE_URL = "http://10.51.1.29:9091/ws/passport_pin";
    private static final String TOKEN = "fc31242d9c4d4a12fc516d1e806b7abb";

    public void getPersonDataByPin(String message) {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SERVICE_URL).queryParam("verbose", true);
        String response = target.queryParam("token", TOKEN)
                .queryParam("pin", message)
                .request().accept("application/json").get(String.class);

        client.close();

        System.out.println(response);

    }

}
