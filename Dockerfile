FROM tomcat:latest
COPY /target/caloriesmanager.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]