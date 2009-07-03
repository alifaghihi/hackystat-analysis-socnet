package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.ArrayList;

import org.hackystat.socnet.socialmediagraph.nodes.interfaces.SocialMediaNodeInterface;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class NodeFactory {

	private final NeoService neo;
	
	public enum ReferenceNodeRelationshipTypes implements RelationshipType {
		BOOKS, CITIES, CODERS, CONCENTRATIONS, COUNTRIES, EMPLOYERS, FACEBOOK_ACCOUNTS, 
		GROUPS, HACKYSTAT_ACCOUNTS, INTERESTS, MOVIES,	MUGGLES, MUSIC, NETWORKS, POLITICAL_PARTIES, PROGRAMMING_LANGUAGES,
		PROJECTS, RELIGIONS, SCHOOLS, SHOWS, STATES, TWITTER_ACCOUNTS, UNIVERSITIES};
	
	public enum IsARelationshipType implements RelationshipType {
		IS_BOOK, IS_CITY, IS_CODER, IS_CONCENTRATION, IS_COUNTRY, IS_EMPLOYER, IS_FACEBOOK_ACCOUNT, 
		IS_GROUP, IS_HACKYSTAT_ACCOUNT, IS_INTEREST, IS_MOVIE, IS_MUGGLE, IS_MUSIC, IS_NETWORK, 
		IS_POLITICAL_PARTY, IS_PROGRAMMING_LANGUAGE, IS_PROJECT, IS_RELIGION, IS_SCHOOL, IS_SHOW, IS_STATE, 
		IS_TWITTER_ACCOUNT, IS_UNIVERSITY};


	private final ArrayList<Node> subreferenceNodes;

	public NodeFactory(NeoService neo) {
		
		this.neo = neo;
		
		subreferenceNodes = new ArrayList<Node>();
		
		for(ReferenceNodeRelationshipTypes relationship : ReferenceNodeRelationshipTypes.values())
		{
			int relationshipIndex = relationship.ordinal();
			
			Relationship rel = this.neo.getReferenceNode().getSingleRelationship(
							relationship,
							Direction.OUTGOING);

			if (rel == null) 
			{
				subreferenceNodes.add(relationshipIndex, neo.createNode());
				
				this.neo.getReferenceNode().createRelationshipTo(subreferenceNodes.get(relationshipIndex),
						ReferenceNodeRelationshipTypes.values()[relationshipIndex]);
			}

			else 
			{
				subreferenceNodes.add(relationshipIndex, rel.getEndNode());
			}

		}
	}

	
	public SocialMediaNodeInterface createNode(IsARelationshipType whatKindOfNode) {

		Node newNode = neo.createNode();

		subreferenceNodes.get(whatKindOfNode.ordinal()).createRelationshipTo(newNode,
				whatKindOfNode);

		SocialMediaNodeInterface vertex = null;
		
		switch(whatKindOfNode.ordinal())
		{
			case 9: vertex = new Person(newNode);
					break;
					
			default: vertex = new SocialMediaNode(newNode);
					break;
		}
			
		return vertex;
	}

}
