# Takes the tomcat base image version 10-jdk17
FROM tomcat:10-jdk17

# Remove the default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into the webapps directory
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port your app runs on
EXPOSE 8080

# Run the startup script
CMD ["catalina.sh", "run"]