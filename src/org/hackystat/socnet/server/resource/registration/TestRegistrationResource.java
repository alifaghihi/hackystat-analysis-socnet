/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hackystat.socnet.server.resource.registration;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNode;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLNodes;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.XMLRelationship;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;
import org.junit.Test;
import org.hackystat.socnet.server.test.SocNetRestApiHelper;
import org.hackystat.socnet.socialmediagraph.graphmanagement.GraphManager;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.BetweenNodesRelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.utils.FileUtils;
import org.hackystat.socnet.utils.JAXBHelper;
import org.restlet.data.Response;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Rachel Shadoan
 */
public class TestRegistrationResource extends SocNetRestApiHelper
{

    @Test
    public void testPutAndGetUser() throws Exception
    {
        assertTrue("testPutAndGetUser failed", isSuccessfulPutAndGetUser());
    }

    public static boolean isSuccessfulPutAndGetUser() throws JAXBException,
            ParserConfigurationException, TransformerConfigurationException,
            TransformerException, IOException, Exception
    {
        boolean result = false;

        JAXBContext jaxbContext = null;

        getCleanGraphDB();

        try
        {
            jaxbContext =
                    JAXBContext.newInstance(
                    org.hackystat.socnet.server.resource.users.jaxb.ObjectFactory.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String msg = "Exception during SocialMediaGraphManager initialization processing";
            throw new RuntimeException(msg, e);
        }

        String email = "pen.named@gmail.com";
        XMLUser user = new XMLUser();
        user.setEmail(email);

        String xml = JAXBHelper.marshall(user, jaxbContext);
        System.out.println("The xml string is: " + xml);

        String uri = getURI("register/");

        System.out.println("URI for Registration Test: " + uri);

      /*  Response response = POST(uri, xml);

        if (!response.getStatus().isSuccess())
        {
            System.out.println(response.getStatus().getDescription());
            System.out.println("POST was not successful");
            return false;
        }
        else
        {
            System.out.println("POST was successful");
        }*/

        uri = getURI("users/" + email);

        String getResult = GET(uri, "pen.named@gmail.com", "gzbdtLQrEbSe");

        XMLUser user1 = (XMLUser) JAXBHelper.unmarshall(getResult, jaxbContext);
        System.out.print("The returned user is registered under: " + user1.getEmail());

        return user1.getEmail().equals(email);


    }

    public static void main(String[] args) throws Exception
    {
        SocNetRestApiHelper.setupServer();
        isSuccessfulPutAndGetUser();
        System.exit(0);
    }
}
