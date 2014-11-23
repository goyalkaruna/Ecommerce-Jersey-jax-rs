package com.version.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Stateless
@Path("cancel")
@Produces(MediaType.APPLICATION_JSON)
public class CancelOrder {
	
	@POST
	@Path("/{id}")
	public Response cancelOrder(@PathParam("id") String id,@Context HttpServletRequest req) throws JSONException 
	{
		System.out.println("in cancel");
		 String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
		 Connection conn = null;
		 Statement stmt=null;
		 JSONObject obj =null;
		 try{   
				Class.forName(JDBC_DRIVER).newInstance();
			 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
			 	stmt = conn.createStatement();
			 	
			 	System.out.println("in try block");
			 
			 	HttpSession session= req.getSession(true);
				String user=(String) session.getAttribute("username").toString();
				System.out.println("user in dao "+user);
				String queryInsertOrder1 = "update orderstatus set orderstatus = 'Cancelled' where orderid= '"+id.toString()+"';";
				System.out.println(queryInsertOrder1);
				int temp1 = stmt.executeUpdate(queryInsertOrder1);
				
				String queryInsertOrder = "delete from orders where id= '"+id.toString()+"';";
				System.out.println(queryInsertOrder);
				
				int temp = stmt.executeUpdate(queryInsertOrder);
				
				
				
				System.out.println("temp in cancel order" + temp);
				if (temp>0 && temp1>0)
				{
					obj = new JSONObject("{'orderstatus':'Order cancelled', 'user':'"+req.getSession().getAttribute("username").toString()+"'}");
					System.out.println("id "+req.getSession().getAttribute("username").toString());
				}
				else{
					obj = new JSONObject("{'orderstatus':'Order doesnt exists', 'user':'"+req.getSession().getAttribute("username").toString()+"'}");
					System.out.println("cant be cancelled");
				}
				conn.close();
				System.out.println("json sent for delete"+ obj);
				return Response.ok(obj.toString()).build();
			}
		catch(Exception e)
		{
			System.out.println("Error in POST request for shipping Details : "+e.toString());
		}
			return Response.status(500).entity("server error").build();
		}	
	

}
