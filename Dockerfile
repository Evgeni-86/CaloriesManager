FROM tomcat:jre17
COPY /target/caloriesmanager.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]