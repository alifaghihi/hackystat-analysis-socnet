package nodes;

import nodes.interfaces.PersonInterface;

import org.neo4j.api.core.Node;

public class Person extends SocialMediaNode implements PersonInterface {
	
	protected final String KEY_FIRST_NAME = "firstName";
	
	protected final String KEY_LAST_NAME = "lastName";
	

	public Person(Node underlyingNode)
	{
		super(underlyingNode);
	}
	
	@Override
	public String getFirstName() {
		
		return (String) underNode.getProperty(KEY_FIRST_NAME);
	}

	@Override
	public String getLastName() {
		
		return (String) underNode.getProperty(KEY_LAST_NAME);
	}

	@Override
	public void setFirstName(String firstName) {
		
		underNode.setProperty(KEY_FIRST_NAME, firstName);
	}

	@Override
	public void setLastName(String lastName) {
	
		underNode.setProperty(KEY_LAST_NAME, lastName);

	}

	@Override
	public String getName() {
		
		return (String) underNode.getProperty(KEY_FIRST_NAME) + (String) underNode.getProperty(KEY_LAST_NAME);
	}

	@Override
	public void setName(String name) {
		
		String[] firstAndLast = name.split(" ");
		underNode.setProperty(KEY_FIRST_NAME, firstAndLast[0]);
		underNode.setProperty(KEY_LAST_NAME, firstAndLast[1]);
		
	}

}
