package com.version.one;



import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;



@Stateless
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
public class LoginController  {
	@POST
	@Path("/login")
	public Response login(String incomingData, @Context HttpServletRequest req) 
	{
		System.out.println("in login controller"); 
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		 String DB_URL = "jdbc:mysql://localhost:3306/cmpe273";
		 Connection conn = null;
		 ResultSet rs=null;
		 Statement stmt=null;
		 JSONObject obj =null;
		 String origPasswd=null;
			
		 try {
				 	Class.forName(JDBC_DRIVER).newInstance();
				 	conn = DriverManager.getConnection(DB_URL,"root","smiling03");
				 	stmt = (Statement) conn.createStatement();
				 	System.out.println("incomingdata"+incomingData);
				 	obj= new JSONObject(incomingData);
					String query = "Select pwd from userlogin where username = '"+obj.get("username").toString()+"';";
					rs = stmt.executeQuery(query);
					while(rs.next())
					{
						origPasswd = rs.getString("pwd");
					}
					
					System.out.println("password:"+origPasswd);
					 MessageDigest md = MessageDigest.getInstance("SHA-1");
					 String pwd = (String) obj.get("pwd");
			         md.update(pwd.getBytes()); 
			         System.out.println("pwd"+pwd);
			         byte[] pwdsha = md.digest();
			         System.out.println("SHA1(\""+pwd+"\") =");
			         System.out.println("   "+bytesToHex(pwdsha));
			         		         
					if (bytesToHex(pwdsha).toString().equalsIgnoreCase(origPasswd))
					{
						
						conn.close();
						HttpSession session= req.getSession(true);
						session.setAttribute("username", obj.get("username").toString());
						JSONObject responseJSON=new JSONObject("{'user':'valid'}");
						System.out.println("username in session:"+req.getSession().getAttribute("username").toString());
						
						return Response.ok(responseJSON.toString()).build();
						
					}
					else
					{
						conn.close();
						return Response.ok("invalid").build();
					}
				} 
				catch (Exception e) {
				e.printStackTrace();
			}
		 
			return Response.status(500).entity("server error").build();
		
	}
	 public static String bytesToHex(byte[] b) {
	      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	      StringBuffer buf = new StringBuffer();
	      for (int j=0; j<b.length; j++) {
	         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	         buf.append(hexDigit[b[j] & 0x0f]);
	      }
	      return buf.toString();
	   }
}