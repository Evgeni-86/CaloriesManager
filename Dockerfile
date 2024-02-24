# Используем официальный образ Tomcat
FROM tomcat:latest
# Копируем приложение WAR в директорию webapps Tomcat
COPY /target/caloriesmanager.war /usr/local/tomcat/webapps/
# Запускаем Tomcat
CMD ["catalina.sh", "run"]