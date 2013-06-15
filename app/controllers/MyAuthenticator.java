package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
import java.net.URL;
import java.io.*;
import java.net.*;
import play.*;
import play.mvc.*;
import java.util.*;
import models.*;
import java.net.URL;
import java.io.*;
import java.net.*;

public class MyAuthenticator extends Authenticator {  
   private String username, password;  
 
   public MyAuthenticator(String user, String pass) {  
     username = user;  
     password = pass;  
   }  
   protected PasswordAuthentication getPasswordAuthentication() {
       System.out.println("Requesting Host  : " + getRequestingHost());
       System.out.println("Requesting Port  : " + getRequestingPort());
       System.out.println("Requesting Prompt : " + getRequestingPrompt());
       System.out.println("Requesting Protocol: " + getRequestingProtocol());
       System.out.println("Requesting Scheme : " + getRequestingScheme());
       System.out.println("Requesting Site  : " + getRequestingSite());
       return new PasswordAuthentication(username, password.toCharArray());
   }
 

 }  
