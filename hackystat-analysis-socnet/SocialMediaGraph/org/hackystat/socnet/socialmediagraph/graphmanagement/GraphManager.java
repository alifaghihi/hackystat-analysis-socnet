package org.hackystat.socnet.socialmediagraph.graphmanagement;

import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;
import org.hackystat.socnet.socialmediagraph.nodes.interfaces.SocialMediaNodeInterface;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

public class GraphManager {

	private NodeFactory nodeBuilder;
	
	private NeoService neo;
	
	public GraphManager(String databaseLocation)
	{
		neo = new EmbeddedNeo(databaseLocation);
		
                Transaction tx = neo.beginTx();
                
                try{
                    
                    nodeBuilder = new NodeFactory(neo);
                    tx.success();
                }
                finally
                {
                    tx.finish();
                }
	}
	
	public void addNode(IsARelationshipType nodeType, String name)
	{
                Transaction tx = neo.beginTx();
                
                try
                {     
                    SocialMediaNode node = nodeBuilder.createNode(nodeType);
                    node.setName(name);
                    System.out.println("GM addNode. Node name is: " + node.getName());
                    tx.success();
                }
                finally
                {
                    tx.finish();
                }
	}
	
        public Iterable<SocialMediaNode> getNodes(IsARelationshipType nodeType)
        {
            Transaction tx = neo.beginTx();
            Iterable<SocialMediaNode> nodes = null;
            try
            {
                nodes = nodeBuilder.getNodes(nodeType);
                
                for(SocialMediaNode node : nodes)
                {
                    System.out.println("GM getNodes node has property: " + node.hasName());
                }
                
                tx.success();
            }
            finally
            {
                tx.finish();
                return nodes;
            }
        }
        
        public Transaction getTransaction()
        {
            return neo.beginTx();
        }
        
        public void exit()
        {
            neo.shutdown();
        }
}
