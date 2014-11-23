package com.version.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;


@Stateless
@Path("order")
@Produces(MediaType.APPLICATION_JSON)

public class ShippingController  {
	@POST
	@Path("/shippingdetails")
	public Response addShippingDetails(String incomingData,@Context HttpServletRequest req) throws JSONException 
	{
		 String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
		 Connection conn = null;
		 Statement stmt=null;
		 JSONObject obj =null;
		 try{   
			 
			 Class.forName(JDBC_DRIVER).newInstance();
			 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
			 	stmt = conn.createStatement();
			 	
			 	System.out.println("incomingdata"+incomingData);
			 	obj= new JSONObject(incomingData);
			 	Date d = new Date();
			 	Random rand = new Random();
			 	int tempId=rand.nextInt(10000);
			 	
			 	HttpSession session= req.getSession(true);
				String user=(String) session.getAttribute("username");
				System.out.println("user in dao "+user +" date "+ d);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				sdf.format(new Date());
				System.out.println("in code:  "+ sdf);
				
				String queryInsertOrder = "insert into orders (id,username,productid,productname,cost,date,apartment,street,city,state,creditcard) "
				+ "values ('"+tempId+"','"+user+"','"+obj.get("productid").toString()+"','"+obj.get("productname")+"','"+obj.get("productprice").toString()
				+"',"+"now()"+",'"+obj.get("apartment").toString()+"','"+obj.get("street").toString()+"','"+obj.get("city").toString()+"','"+obj.get("state").toString()+"','"+obj.get("credit")+"')";
				System.out.println(queryInsertOrder);
				
				int temp = stmt.executeUpdate(queryInsertOrder);
				
				System.out.println("temp "+temp);
				String queryInsertOrderStatus="insert into orderstatus (orderid,date,orderstatus) values ('"+tempId+"',"+"now()"+",'Ordered')";
				@SuppressWarnings("unused")
				int temp1=stmt.executeUpdate(queryInsertOrderStatus);
				System.out.println("Data inserted");
				
				JSONObject responseJSON=new JSONObject("{'order':'Submitted and is under process', 'id':'"+tempId+"','user':'"+req.getSession().getAttribute("username").toString()+"'}");
					System.out.println("id "+req.getSession().getAttribute("username").toString());
				conn.close();
				System.out.println("Response"+Response.ok(responseJSON.toString()));
				return Response.ok(responseJSON.toString()).build();
		}
	catch(Exception e)
	{
		System.out.println("Error in POST request for shipping Details : "+e.toString());
	}
		return Response.status(500).entity("server error").build();
	}
	

}