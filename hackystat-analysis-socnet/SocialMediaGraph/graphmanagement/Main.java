package graphmanagement;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;

import nodes.NodeFactory;

public class Main {
	
	
	/*
	 * This is currently being used for testing. It will not be included in a final release.
	 */
	public static void main(String[] args)
	{
		
		NeoService neo = new EmbeddedNeo("/home/rachel/Desktop/neotestwithprivateneo");
		
		Transaction tx = neo.beginTx();
		try{
			NodeFactory nodeFactory = new NodeFactory(neo);
		
			nodeFactory.createNode(NodeFactory.IsARelationshipType.IS_CODER);
			
			for(RelationshipType relationship : neo.getRelationshipTypes())
			{
				System.out.println(relationship.name());
			}
		}
		finally
		{
			tx.finish();
			neo.shutdown();
		}
	}
}
