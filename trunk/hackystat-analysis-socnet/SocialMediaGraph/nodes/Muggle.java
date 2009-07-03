package nodes;

import nodes.interfaces.PersonInterface;

import org.neo4j.api.core.Node;

public class Muggle extends SocialMediaNode implements PersonInterface {
	
	protected final String KEY_FIRST_NAME = "firstName";
	
	protected final String KEY_LAST_NAME = "lastName";
	
	protected final String KEY_FACEBOOK_USERNAME = "facebookUsername";
	
	protected final String KEY_TWITTER_USERNAME = "twitterUsername";
	
	protected final String KEY_TWITTER_USER_ID = "twitterUserID";
	
	public Muggle(Node underlyingNode)
	{
		super(underlyingNode);
	}
	
	@Override
	public String getFacebookUsername() {

		return (String) underNode.getProperty(KEY_FACEBOOK_USERNAME);
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
	public int getTwitterUserID() {
		
		return (Integer) underNode.getProperty(KEY_TWITTER_USER_ID);
	}

	@Override
	public String getTwitterUsername() {
		
		return (String) underNode.getProperty(KEY_TWITTER_USERNAME);
	}

	@Override
	public void setFacebookUsername(String facebookUsername) {
		
		underNode.setProperty(KEY_FACEBOOK_USERNAME, facebookUsername);
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
	public void setTwitterUserID(int twitterUserID) {
		
		underNode.setProperty(KEY_TWITTER_USER_ID, twitterUserID);

	}

	@Override
	public void setTwitterUsername(String twitterUsername) {
		
		underNode.setProperty(KEY_TWITTER_USERNAME, twitterUsername);
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
