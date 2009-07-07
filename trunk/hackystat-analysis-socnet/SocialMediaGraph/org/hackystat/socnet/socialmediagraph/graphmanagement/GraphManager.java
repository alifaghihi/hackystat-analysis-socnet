package org.hackystat.socnet.socialmediagraph.graphmanagement;

import java.util.ArrayList;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory;
import org.hackystat.socnet.socialmediagraph.nodes.NodeFactory.IsARelationshipType;
import org.hackystat.socnet.socialmediagraph.nodes.SocialMediaNode;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;

public class GraphManager {

	private NodeFactory nodeBuilder;
	
	private NeoService neo;
	/**
         * Creates an instance of neo that will store information to the location
         * passed as a parameter, and initializes a NodeFactory to build nodes.
         * @param databaseLocation
         */
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
        /**
         * Returns the node at the end of the given path from the start node.
         *
         * @param startNode
         * @param relationshipPath
         * @param relationshipPathDirections
         * @param nodeNamesOnPath
         * @return
         */
	public SocialMediaNode getNode(SocialMediaNode startNode,
                ArrayList<RelationshipType> relationshipPath,
                ArrayList<Direction> relationshipPathDirections,
                ArrayList<String> nodeNamesOnPath)
        {
            SocialMediaNode node = null;
            Transaction tx = neo.beginTx();
            try
            {
                for(int i = 0; i<relationshipPath.size(); i++)
                {
                    node = nodeBuilder.getNode(startNode, relationshipPath.get(i),
                            relationshipPathDirections.get(i), nodeNamesOnPath.get(i));
                    startNode = node;
                }
                
                tx.success();
            }
            finally
            {
                tx.finish();
                return node;
            }

            
        }
        /**
         * Adds a node to the graph
         * @param nodeType - the kind of node being added
         * @param name - the value of the name property of the node
         */
	public void addNode(IsARelationshipType nodeType, String name)
	{
                //start a transaction
                Transaction tx = neo.beginTx();
                
                try
                {
                    //create the node
                    SocialMediaNode node = nodeBuilder.createNode(nodeType);
                    //set the name property
                    node.setName(name);

                    //mark the transaction as successful. If you don't do this,
                    //the transaction will fail on tx.finish() and the changes
                    //won't be committed to the database
                    tx.success();
                }
                finally
                {
                    //close the transaction
                    tx.finish();
                }
	}

        /**
         * This method retrieves all of the nodes of a certain type from the
         * database.
         *
         * @param nodeType
         * @return
         */
        public Iterable<SocialMediaNode> getNodes(IsARelationshipType nodeType)
        {
            //start a new transaction
            Transaction tx = neo.beginTx();
            Iterable<Node> nodes = null;
            ArrayList<SocialMediaNode> socNodes = new ArrayList<SocialMediaNode>();
            try
            {
                //get the nodes of the appropriate type from the database
                nodes = nodeBuilder.getNodes(nodeType);
                for(Node node : nodes)
                {
                    //wrap each node in a SocialMediaNode wrapper
                    socNodes.add(new SocialMediaNode(node));
                }
                //make the transaction as a success (This is important!)
                tx.success();
            }
            finally
            {
                //close the transaction
                tx.finish();
                return socNodes;
            }
        }

        /**
         * This method is ONLY for testing purposes. All interactions with the
         * database must be wrapped in transations, and in order to test certain
         * functions outside of GraphManager, it's necessary to be able to
         * create a transaction without necessarily having direct access to the
         * instance of EmbeddedNeo. However, I suspect that it would have
         * potentially very strange and unpredictable effects if used other than
         * in very specific circumstances.
         * @return a Transaction
         */
        public Transaction getTransaction()
        {
            return neo.beginTx();
        }

        /**
         * This method cleanly exits the database.
         */
        public void exit()
        {
            neo.shutdown();
        }
}
