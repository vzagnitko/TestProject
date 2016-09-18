# TestProject

1. Run ant script with target "start-server-unix" if you using Unix family system or run target “start-server-windows” if you are using Windows”
2. Go http://localhost:8080/TestProject/rest/swagger-ui.html

P.S. 
1. I have used JAVA 1.8 to write the project.
2. If you are going to run the project under Unix system, please run before this script “chmod +x start-server.sh”
3. To write the project I have used Maven, if you want to build the project please run the command "mvn clean install".
This command will compile and assembled jar and war files. Also you can a run a war file with Tomcat server, just copy
a war file in webapp Tomcat directory.