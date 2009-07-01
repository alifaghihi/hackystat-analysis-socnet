package nodes.interfaces;

public interface PersonInterface extends SocialMediaNodeInterface {
	
	public String getFirstName();
	
	public void setFirstName(String firstName);
	
	public String getLastName();
	
	public void setLastName(String lastName);
	
	public String getFacebookUsername();
	
	public void setFacebookUsername(String facebookUsername);
	
	public String getTwitterUsername();
	
	public void setTwitterUsername(String twitterUsername);
	
	public int getTwitterUserID();
	
	public void setTwitterUserID(int twitterUserID);
}
