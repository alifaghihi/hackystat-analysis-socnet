package nodes;

import nodes.interfaces.ShowInterface;

import org.neo4j.api.core.Node;

public class Show extends SocialMediaNode implements ShowInterface{

	public final String KEY_TV_NETWORK = "tvNetwork";
	
	public final String KEY_TV_WRITER = "tvWriter";
	
	public Show(Node underlyingNode) {
		super(underlyingNode);
	}

	@Override
	public String getNetwork() {
		return (String) underNode.getProperty(KEY_TV_NETWORK);
	}

	@Override
	public String getWriter() {
		
		return (String) underNode.getProperty(KEY_TV_WRITER);
	}

	@Override
	public void setNetwork(String network) {
		
		underNode.setProperty(KEY_TV_NETWORK, network);
		
	}

	@Override
	public void setWriter(String writerName) {
		
		underNode.setProperty(KEY_TV_WRITER, writerName);		
	}
	

}
