package com.version.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;


@Stateless
@Path("products")
@Produces(MediaType.APPLICATION_JSON)

public class ProductController  {
	@GET
	@Path("/{id}")
	public Response getProduct(@PathParam("id") String id,@Context HttpServletRequest req) throws JSONException 
	{
		try{   
			 // To connect to mongodb server
		         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		         System.out.println(mongoClient);
		         // Now connect to your databases
		         DB db = mongoClient.getDB( "cmpe273" );
			     System.out.println("Connect to database successfully");
		    
		         DBCollection coll = db.getCollection("routers");
		         System.out.println("Collection mycol selected successfully");
		         System.out.println("id"+id);
//		         ObjectId objectid= new ObjectId(id); 
//		         System.out.println("objectid"+objectid);
		         BasicDBObject query = new BasicDBObject("brand", id);
		         
		         DBCursor cursor = coll.find(query);
		    //    System.out.println("curser value "+cursor);
		      // System.out.println("curser value "+cursor.next().toString()); 
		         
		         HttpSession session= req.getSession(true);
		     	 Object user = session.getAttribute("username");
		     	 if (user!=null){
		     		 System.out.println("user in session"+ user);
		     	 }else{
		     		 session.setAttribute("guest", user);
		     	 }
		         JSONObject jsonObject=new JSONObject(cursor.next().toString());
		         System.out.println("jsonobj"+jsonObject);
		         
		         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		         System.out.println("parsed :-----------"+jsonObject.getJSONObject("enddate"));
		         
		         Date d=formatter.parse(jsonObject.getJSONObject("enddate").getString("$date"));
		         
		         Date currentDate=new Date();
		         String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
				 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
				 Connection conn = null;
				 ResultSet rs=null;
				 Statement stmt=null;
				 @SuppressWarnings("unused")
				JSONObject obj =null;
				 @SuppressWarnings("unused")
				String origPasswd=null;
				 int productQuantity=0;
		         if(currentDate.compareTo(d)<0)
		         {
		        	 try {
						 	Class.forName(JDBC_DRIVER).newInstance();
						 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
						 	stmt = conn.createStatement();
						 	String queryProduct = "Select quantity from inventory where id = '"+id.toString()+"';";
							rs = stmt.executeQuery(queryProduct);
							while(rs.next())
							{
								productQuantity = Integer.parseInt(rs.getString("quantity"));
							}
							if (productQuantity>0)
							{
								return Response.status(202).entity(jsonObject.toString()).build();
							}
							else
							{
								return Response.status(204).entity("currently out of stock").build();
							}
							
		        	 }
		        	 catch(Exception e){}
		         }
		        	 
		        	mongoClient.close();
		        	conn.close();
		        return Response.ok(jsonObject.toString()).build();
		      }catch(Exception e){
			     System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			  }
		return Response.ok("Server Error").build();
	}
	
}