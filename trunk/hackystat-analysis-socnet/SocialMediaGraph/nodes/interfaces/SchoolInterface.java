package nodes.interfaces;

public interface SchoolInterface extends SocialMediaNodeInterface {
	
	public enum SchoolType{PUBLIC, PRIVATE, CHARTER, MAGNET};
	
	public SchoolType getType();
	
	public void setType(SchoolType typeOfSchool);

}
