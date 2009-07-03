package nodes;

import nodes.interfaces.SocialMediaNodeInterface;

import org.neo4j.api.core.Node;


public abstract class SocialMediaNode implements SocialMediaNodeInterface{

	protected Node underNode;
	
	protected final String KEY_NAME = "name";
	
	public SocialMediaNode(Node underlyingNode)
	{
		underNode = underlyingNode;
	}
	
	public String getName()
	{
		return (String) underNode.getProperty(KEY_NAME);
	}
	
	public void setName(String name)
	{
		underNode.setProperty(KEY_NAME, name);
	}
	

}
