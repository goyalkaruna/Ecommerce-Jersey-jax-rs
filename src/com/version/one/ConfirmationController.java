package com.version.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Stateless
@Path("ship")
@Produces(MediaType.APPLICATION_JSON)
public class ConfirmationController {
	@GET
	@Path("/{id}")
	public Response getOrder(@PathParam("id") String id,@Context HttpServletRequest req) throws JSONException 
	{
		try{ 
			System.out.println("in confirmation controller");
			 Date currentDate=new Date();
	         String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
			 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
			 Connection conn = null;
			 ResultSet rs=null;
			 Statement stmt=null;
			 JSONObject obj =null;
			 String orderstatus = null;
			 String message = null;
			 String orderid = null;
			 Date d= new Date();
			 Boolean allowcancel = false;
			 System.out.println("id in confirmation"+id);
				 	Class.forName(JDBC_DRIVER).newInstance();
				 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
				 	stmt = conn.createStatement();
				 	String queryProduct = "Select * from orderstatus where orderid = '"+id.toString()+"';";
					rs = stmt.executeQuery(queryProduct);
					System.out.println("resultset"+rs);
					while(rs.next())
					{   orderid = rs.getString("orderid");
						orderstatus = rs.getString("orderstatus");
						d = rs.getDate("date");
						System.out.println("date and status" + orderstatus + d);
					}
					long diff = currentDate.getTime() - d.getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					 
					System.out.print(diffDays + " days, ");
					if (diffDays>3){
					message ="Product will be delivered soon. ";
					}else{
					message = "Product yet to be dispatched";
					allowcancel = true;
					}
					
					obj = new JSONObject("{'orderid':'"+orderid+"','orderstatus':'"+orderstatus+"', 'date':'"+d+"', 'message':'"+message+"', 'allowcancel':'"+allowcancel+"'"
							+ ", 'user':'"+req.getSession().getAttribute("username").toString()+"'}");
					System.out.println("obj   json  "+ obj);
					return Response.ok(obj.toString()).build();
    }catch(Exception e){
	     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	  }
return Response.ok("Server Error").build();
}
	
	
}
