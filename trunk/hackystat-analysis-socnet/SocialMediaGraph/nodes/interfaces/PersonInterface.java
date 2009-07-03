package nodes.interfaces;

public interface PersonInterface extends SocialMediaNodeInterface {
	
	public enum peopleRelationships{IS_FRIENDS_WITH, FOLLOWS, IS_FOLLOWING, LIKES, IS_INTERESTED_IN, 
		CONTRIBUTES_TO, ATTENDS, USES, PRACTICES, WORKS_FOR, IS_AFFILIATED_WITH, IS_AT};
	
	public String getFirstName();
	
	public void setFirstName(String firstName);
	
	public String getLastName();
	
	public void setLastName(String lastName);
	
}
