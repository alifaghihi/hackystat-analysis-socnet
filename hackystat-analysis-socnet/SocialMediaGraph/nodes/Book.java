package nodes;

import nodes.interfaces.BookInterface;

import org.neo4j.api.core.Node;


public class Book extends SocialMediaNode
					implements BookInterface
{

	public final String KEY_AUTHOR = "author";
	
	public Book(Node underlyingNode) {
		
		super(underlyingNode);
	}

	@Override
	public String getAuthor() {
		
		return (String) underNode.getProperty(KEY_AUTHOR);
	}


	@Override
	public void setAuthor(String authorName) {
	
		underNode.setProperty(KEY_AUTHOR, authorName);
		
	}

}
