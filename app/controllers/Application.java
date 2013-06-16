package controllers;
import play.*;
import play.mvc.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.mysql.jdbc.Connection;

import play.mvc.*;
import org.json.*; 
import play.libs.ws.*;
import play.db.*;

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
    // DataSource ds = DB.datasource;  
    // Connection conn = ds.getConnection();
     
     String str =sb.toString();
     JSONArray arr = new JSONArray();
     int index=0;
     arr = check_service_status("phx7b02c-0c9e", str, "Rabbit MQ master", arr, index); 
     index++;
     arr = check_service_status("phx8b03c-b7aa", str, "Rabbit MQ slave", arr, index); 
     index++;
    arr = check_service_status("phx8b03c-03f2", str, "Controller master", arr, index); 
    index++;
    arr = check_service_status("phx7b02c-8a8a", str, "Controller slave", arr, index); 
    index++;
    arr = check_service_status("10.87.199.131", str, "NVP gateway", arr, index); //site
    index++; 
    arr = check_service_status("10.87.199.132", str, "NVP gateway", arr, index); //site
    index++;
    arr = check_service_status("10.87.199.133", str, "NVP gateway", arr, index); //corp
    index++;
    arr = check_service_status("10.87.199.134", str, "NVP gateway", arr, index); //corp
    index++;
    arr = check_service_status("10.87.192.101", str, "NVP service node ", arr, index); 
    index++;
    arr = check_service_status("10.87.194.102", str, "NVP service node", arr, index); 
    index++;
    arr = check_service_status("10.87.196.101", str, "NVP service node", arr, index);
    index++;
   arr = check_service_status("phx7b02c-d551", str, "NVP controller", arr, index);//NVP controller
    index++;
    arr = check_service_status("phx8b03c-8323", str, "NVP controller", arr, index);//NVP controller
    index++;
    arr = check_service_status("phx8b03c-c809", str, "NVP controller", arr, index);//NVP controller
    index++;
    arr = check_service_status("phx8b03c-c476", str, "Openstratus DHCP", arr, index);//OS DHCP
    index++;
    arr = check_service_status("chd1b01c-d84b ", str, "Nova compute", arr, index);
    index++;
  /*  arr = check_service_status("phx8b03c-022e", str, "Nova compute", arr, index);
    index++;
    arr = check_service_status("phx8b03c-068a", str, "Nova compute", arr, index);
    index++;
    arr = check_service_status("phx8b03c-06df", str, "Nova compute", arr, index);
    index++;
   */ 
    
    renderText(arr.toString(1));
    }//health()
  
    
    @Util
    public static JSONArray check_service_status(String host, String str, String object_name, JSONArray arr, int index) throws JSONException
    {
    	JSONObject obj = new JSONObject(str);
    	JSONArray services_arr = obj.getJSONArray("services");
    	int service=0;
    	Boolean found =false;
    	while(service<services_arr.length()-1)
    	{
    	ArrayList<String> down = new ArrayList<String>();	
    	String host_name = services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    	
    	 while(host_name.equals(host))
    	 {
    		 String description = services_arr.getJSONObject(service).getString("service_description");
    		 found = true;
    		String host_status = services_arr.getJSONObject(service).getString("service_status");
    		if(host_status.equals("CRITICAL"))
    		{
    			while(host_name.equals(host))
    			{
    				if(services_arr.getJSONObject(service).getString("service_status").equals("CRITICAL") && !description.contains(":I:"))
    				{
    				down.add(services_arr.getJSONObject(service).getString("service_description"));
    				}
    				service++;
    				description = services_arr.getJSONObject(service).getString("service_description");
    				host_name = services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    			}
    			
    			//render red
    			JSONObject object = new JSONObject();
    			object.put("object_name",object_name);
    			object.put("host",host+".stratus.phx.ebay.com" );
    			object.put("found", true);
    			object.put("healthy", false);
    			object.put("down_services", down);
    			arr.put(index, object);
    			return arr;
    		}
    		 service++;
    		 host_name = services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    		 if(!host_name.equals(host))
    		 {
    			 //render green
    			JSONObject object = new JSONObject();
    			object.put("object_name",object_name);
     			object.put("host",host+".stratus.phx.ebay.com" );
     			object.put("found", true);
     			object.put("healthy", true);
     			object.put("down_services", "");
     			arr.put(index, object);
     			return arr;
    		 }
    		 host_name =services_arr.getJSONObject(service).getJSONObject("service_host").getString("host_name");
    	 }
    	 service++;
	}
    	if(found==false)
    	{
    		JSONObject object = new JSONObject();
			object.put("object_name",object_name);
 			object.put("host",host+".stratus.phx.ebay.com" );
 			object.put("found", false);
 			object.put("healthy", false);
 			object.put("down_services", "");
 			arr.put(index, object);
 			return arr;
    	}
    return arr;
    }
}//class Application

