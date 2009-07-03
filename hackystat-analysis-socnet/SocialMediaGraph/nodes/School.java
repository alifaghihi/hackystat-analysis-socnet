package nodes;

import nodes.interfaces.SchoolInterface;

import org.neo4j.api.core.Node;

public class School extends SocialMediaNode implements SchoolInterface{

	public final String KEY_SCHOOL_TYPE = "schoolType";
	
	public School(Node underlyingNode) {
		super(underlyingNode);
	}

	@Override
	public SchoolType getType() {
		return (SchoolType) underNode.getProperty(KEY_SCHOOL_TYPE);
	}

	@Override
	public void setType(SchoolType typeOfSchool) {
		
		underNode.setProperty(KEY_SCHOOL_TYPE, typeOfSchool);
		
	}

}
