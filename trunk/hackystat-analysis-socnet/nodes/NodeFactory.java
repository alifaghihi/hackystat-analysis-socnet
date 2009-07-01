package nodes;

import java.util.ArrayList;

import nodes.factories.MuggleFactory.referenceNodeRelationshipTypes;
import nodes.interfaces.PersonInterface;
import nodes.interfaces.SocialMediaNodeInterface;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class NodeFactory {

	public enum ReferenceNodeRelationshipTypes implements RelationshipType {
		MUGGLES, CODERS, BOOKS, MOVIES, SHOWS, MUSIC, UNIVERSITIES, SCHOOLS, INTERESTS, 
		PROJECTS, EMPLOYERS, GROUPS, CONCENTRATIONS, RELIGIONS, POLITICAL_PARTIES, 
		NETWORKS, LOCATION, PROGRAMMING_LANGUAGES
	};
	
	public enum IsARelationshipType implements RelationshipType {
		IS_MUGGLE, IS_CODER, IS_BOOK, IS_MOVIE, IS_SHOW, IS_MUSIC, IS_UNIVERSITY,
		IS_SCHOOL, IS_INTEREST, IS_PROJECT, IS_EMPLOYER, IS_GROUP, IS_CONCENTRATION, 
		IS_RELIGION, IS_POLITICAL_PARTY, IS_NETWORK, IS_LOCATION, IS_PROGRAMMING_LANGUAGE
	};

	private final NeoService neo;

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
				
				neo.getReferenceNode().createRelationshipTo(subreferenceNodes.get(relationshipIndex),
						IsARelationshipType.values()[relationshipIndex]);
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

		SocialMediaNodeInterface vertex;
		
		switch(whatKindOfNode.ordinal())
		{
			case 1: vertex = new Muggle(newNode);
			vertex = new Coder(newNode);
			vertex = new Book(newNode);
			vertex = new Show(newNode);
			vertex = new Music(newNode);
			vertex = new University(newNode);
			vertex = new School(newNode);
			vertex = new Interest(newNode);
			vertex = new Project(newNode);
			vertex = new Employer(newNode);
			vertex = new Group(newNode);
			vertex = new Concentration(newNode);
			vertex = new Religion(newNode);
			vertex = new PoliticalParty(newNode);
			vertex = new Network(newNode);
			vertex = new Location(newNode);
			vertex = new ProgrammingLanguage(newNode); 
		
		}
		return new Muggle(newNode);
	}
}
