package org.hackystat.socnet.server.resource.helloping;

import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import static org.junit.Assert.assertTrue;

public class TestHelloPingRestApi extends SocNetRestApiHelper {

  /**
   * Test that GET {host}/ping returns the service name, and that GET
   * {host}/ping?user={user}&password={password} is OK.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testHelloPing() throws Exception {

    assertTrue("Checking ping", isHelloWorld(server.getHostName()));

  }

  public boolean isHelloWorld(String host) {
    try {
      String registerUri = host.endsWith("/") ? host + "ping" : host + "/ping";
      Request request = new Request();
      request.setResourceRef(registerUri);
      request.setMethod(Method.GET);
      Client client = new Client(Protocol.HTTP);
      Response response = client.handle(request);
      String pingText = response.getEntity().getText();
      boolean isHelloWorld = (response.getStatus().isSuccess() && "Hello, World".equals(pingText));

      return isHelloWorld;
    }
    catch (Exception e) {
      return false;
    }

  }
}
