package nodes;

import org.neo4j.api.core.Node;

import nodes.interfaces.SocialMediaNodeInterface;

public class Concentration extends SocialMediaNode implements SocialMediaNodeInterface
{

	public Concentration(Node underlyingNode)
	{
		super(underlyingNode);
	}
	
}
