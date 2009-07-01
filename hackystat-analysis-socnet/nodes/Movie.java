package nodes;

import nodes.interfaces.MovieInterface;

import org.neo4j.api.core.Node;

public class Movie extends SocialMediaNode implements MovieInterface {

	public final String KEY_MOVIE_DIRECTOR = "movieDirector";
	public final String KEY_MOVIE_STUDIO = "movieStudio";
	
	public Movie(Node underlyingNode) {
		super(underlyingNode);
	}

	@Override
	public String getDirector() {

		return (String) underNode.getProperty(KEY_MOVIE_DIRECTOR);
		
	}

	@Override
	public String getStudio() {
		return (String) underNode.getProperty(KEY_MOVIE_STUDIO);
	}

	@Override
	public void setDirector(String directorName) {
		underNode.setProperty(KEY_MOVIE_DIRECTOR, directorName);
		
	}

	@Override
	public void setStudio(String studio) {
		underNode.setProperty(KEY_MOVIE_STUDIO, studio);
		
	}

}
