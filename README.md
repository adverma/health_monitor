A monitoring tool to provide overview of the stratus infrastructure
==============

Steps to run the application in your machine:

-Clone the git repository (git clone https://github.com/adverma/health_monitor)
-Install java and Play framework (http://www.playframework.com/documentation/1.0.1/install)
-Start the play application (play run) and go to http://localhost:9000/public/html/index.html in the browser
-View health of components in JSON format at http://localhost:9000/health
-Add/delete stratus components in the file health_monitor/app/controller/Application.java 
-Make change in the GUI through the file health_monitor/public/html/index.html
