package com.version.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;

@Stateless
@Path("summary")
@Produces(MediaType.APPLICATION_JSON)
public class Summary {
	@GET
	@Path("/detail")
	public Response getProduct(@Context HttpServletRequest req) throws JSONException 
	{
		try{  
		         HttpSession session= req.getSession(true);
		     	 Object user = session.getAttribute("username");
		     	 if (user!=null){
		     		 System.out.println("user in session for summary "+ user);
		     	 }else{
		     		 session.setAttribute("guest", user);
		     	 }

		         		         
		         String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
				 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
				 Connection conn = null;
				 ResultSet rs=null;
				 Statement stmt=null;
				 JSONArray obj =null;
				 String jsonstring= "";
				 String orderid = null;
						 	Class.forName(JDBC_DRIVER).newInstance();
						 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
						 	stmt = conn.createStatement();
						 	String queryProduct = "select * from orders where username = '"+user.toString()+"' order by date;";
							System.out.println(queryProduct);
						 	rs = stmt.executeQuery(queryProduct);
						 	
							System.out.println("Query executed ");
							while(rs.next())
							{
							orderid = (rs.getString("id"));
							jsonstring = jsonstring + ("{'id':'"+orderid+"', 'productid':'"+rs.getString("productid")+"','date':'"+rs.getDate("date")+"'},");
							System.out.println("json in summary  "+ jsonstring);
						
							}
							obj = new JSONArray("["+jsonstring+"]");
							System.out.println("bye while");
							conn.close();
							return Response.ok(obj.toString()).build();
							
		        	 }
		        	 catch(Exception e){
		        		 e.printStackTrace();
		        		 return Response.ok("Server Error").build();
		        	 
		         	        	 	        	 }        	
		        
	}
		
	
	
}
