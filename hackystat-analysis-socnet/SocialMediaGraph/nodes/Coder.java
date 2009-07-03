package nodes;

import nodes.interfaces.CoderInterface;

import org.neo4j.api.core.Node;

public class Coder extends Muggle implements CoderInterface{

	protected final String KEY_HACKYSTAT_USERNAME = "hackystatUsername";
	
	public Coder(Node underlyingNode) {
		super(underlyingNode);

	}

	@Override
	public String getHackystatUsername() {
		
		return (String)underNode.getProperty(KEY_HACKYSTAT_USERNAME);
	}

	@Override
	public void setHackystatUsername(String hackystatUserID) {
		
		underNode.setProperty(KEY_HACKYSTAT_USERNAME, hackystatUserID);
		
	}

}
