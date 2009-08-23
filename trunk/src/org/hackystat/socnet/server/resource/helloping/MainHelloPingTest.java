package org.hackystat.socnet.server.resource.helloping;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class MainHelloPingTest {

  public static void main(String[] args) {
    String host = "http://localhost:9876/socnet/";
    try {
      String registerUri = host.endsWith("/") ? host + "ping" : host + "/ping";
      Request request = new Request();
      request.setResourceRef(registerUri);
      request.setMethod(Method.GET);
      Client client = new Client(Protocol.HTTP);
      Response response = client.handle(request);
      String pingText = response.getEntity().getText();

      System.out.println(pingText);

    }
    catch (Exception e) {
      System.out.println("You have failed, Obi-Wan-Kenobi");
    }
  }
}
