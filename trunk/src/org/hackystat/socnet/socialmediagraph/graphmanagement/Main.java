package org.hackystat.socnet.socialmediagraph.graphmanagement;

import java.io.File;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;

import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;

public class Main
{

    /*
     * This is currently being used for testing. It will not be included in a final release.
     */
    public static void main(String[] args)
    {

        File f = new File("/home/cody/Desktop/getNodesTest");
        try
        { 
            f.delete();
        }
        catch(Exception e)
        {
            
        }
        
        GraphManager gm = new GraphManager(f.getAbsolutePath());

      
        for (SocialMediaNode node : gm.getNodes("IS_USER"))
        {
            Transaction tx = gm.getTransaction();
            try
            {
                System.out.println(node);
                System.out.println(node.getName());
                tx.success();
            } finally
            {
                tx.finish();
            }
        }
        
        gm.exit();
    }
}


