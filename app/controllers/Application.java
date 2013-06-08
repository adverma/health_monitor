package controllers;

import play.*;

import play.mvc.*;

import java.io.*;

import java.net.*;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

//import models.*;
import play.mvc.*;
import org.json.*; 
import play.libs.ws.*;


public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void health() throws Exception {
    	
    	String urlString = "http://phx8b03c-d14d.stratus.phx.ebay.com/nagios3/cgi-bin/status-json.cgi";  
    	String username = "readonly";  
    	String password = "readonly";  
    	StringBuilder sb = new StringBuilder();
    	
        try {
			Authenticator.setDefault(new MyAuthenticator(username, password));  
			URL url = new URL(urlString);  
			URLConnection conn = url.openConnection();  
			InputStream content = (InputStream) url.getContent();  
			BufferedReader in = new BufferedReader(new InputStreamReader(content));  
			String line;  
			
			while ((line = in.readLine()) != null) { 
				sb.append(line+"\n");	
			} 
			content.close();
			in.close();
		
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
      String str =sb.toString();
     JSONArray arr = new JSONArray();
     int index=0;
    arr = check_service_status("phx8b03c-03f2", str, "Controller master", arr, index); //controller master
    index++;
    arr = check_service_status("phx7b02c-8a8a", str, "Controller slave", arr, index); //controller slave
    index++;
    arr = check_service_status("phx7b02c-0c9e", str, "MySQL Master", arr, index); //rabbit mq master (check)
    index++;
    arr = check_service_status("phx8b03c-b7aa", str, "MySQL Slave", arr, index); //rabbit mq slave (check)
    renderText(arr.toString(1));
    }//health()
  
    @Util
    public static JSONArray check_service_status(String host, String str, String object_name, JSONArray arr, int index) throws JSONException
    {
    	JSONObject obj = new JSONObject(str);
    	JSONArray services_arr = obj.getJSONArray("services");
    	int service=0;
    	
    	while(service<services_arr.length()-1)
    	{
    	ArrayList<String> down = new ArrayList<String>();	
    	String host_name = services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    	
    	 while(host_name.equals(host))
    	 {
    		String host_status = services_arr.getJSONObject(service).getString("service_status");
    		if(host_status.equals("CRITICAL"))
    		{
    			while(host_name.equals(host))
    			{
    				if(services_arr.getJSONObject(service).getString("service_status").equals("CRITICAL"))
    				{
    				down.add(services_arr.getJSONObject(service).getString("service_description"));
    				}
    				service++;
    				host_name = services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    			}
    			
    			//render red
    			JSONObject object = new JSONObject();
    			object.put("object_name",object_name);
    			object.put("host",host+".stratus.phx.ebay.com" );
    			object.put("healthy", false);
    			object.put("down services", down);
    			arr.put(index, object);
    			return arr;
    		}
    		 service++;
    		 if(!host_name.equals(host))
    		 {
    			 //render green
    			JSONObject object = new JSONObject();
    			object.put("object_name",object_name);
     			object.put("host",host+".stratus.phx.ebay.com" );
     			object.put("healthy", true);
     			arr.put(index, object);
     			return arr;
    		 }
    		 host_name =services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    	 }
    	 service++;
	}
    return arr;
    }
}//class Application

