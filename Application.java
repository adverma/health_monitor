package controllers;

import play.*;

import play.mvc.*;

import java.io.*;

import java.net.*;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.*;
import play.mvc.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void health() {
    	
    	String urlString = "http://phx8b03c-d14d.stratus.phx.ebay.com/nagios3/cgi-bin/status-json.cgi";  
    	String username = "readonly";  
    	String password = "readonly";  
    	StringBuilder sb = new StringBuilder();
    	//JSONReader reader = new JSONReader(new FileReader("http://localhost:9000/health"));
    	
    /*	try {
			Authenticator.setDefault(new MyAuthenticator(username, password));  //
			URL url = new URL(urlString);  
			URLConnection conn = url.openConnection();  
			InputStream content = (InputStream) url.getContent();  
			BufferedReader in = new BufferedReader(new InputStreamReader(content));  
			String line;  
			
			while ((line = in.readLine()) != null) { 
				sb.append(line);	
				sb.append("\n");
			} 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int i=0;
    
    */
    	
    	JsonObject m1 = new JsonObject();
    	m1.addProperty("component", "mysql.master");
    	m1.addProperty("host", "mm.stratus.phx.ebay.com");
    	m1.addProperty("healthy", "true");

    	JsonObject m2 = new JsonObject();
    	m2.addProperty("component", "mysql.slave");
    	m2.addProperty("host", "ms.stratus.phx.ebay.com");
    	m2.addProperty("healthy", "false");

    	JsonArray arr = new JsonArray();
    	arr.add(m1);
    	arr.add(m2);
    	String s = "ad";
    	renderText(s);
    	
    }

}