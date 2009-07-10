package org.hackystat.socnet.server.resource.helloping;

import org.hackystat.socnet.server.resource.SocNetResource;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class HelloPingResource extends SocNetResource{

	public HelloPingResource(Context context, Request request, Response response) {
	    super(context, request, response);
	}
	
	public Representation represent(Variant variant) {
		return new StringRepresentation("Hello, World");
	}

}
