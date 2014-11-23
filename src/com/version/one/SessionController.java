package com.version.one;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;


@Stateless
@Path("session")
@Produces(MediaType.APPLICATION_JSON)

public class SessionController  {
	@GET
	@Path("/check")
	public Response checkSession(@Context HttpServletRequest req) throws JSONException 
	{
		System.out.println("inside the session function");
		HttpSession session= req.getSession(true);
		Object checkSession=session.getAttribute("username");
		if(checkSession!=null)
		{
			JSONObject responseJSON=new JSONObject("{'session':'exists'}");
			System.out.println("username in session:"+checkSession);
			return Response.ok(responseJSON.toString()).build();
		}
		else
		{
			JSONObject responseJSON=new JSONObject("{'session':'null'}");
			return Response.ok(responseJSON.toString()).build();
		}
		
	}
	
}