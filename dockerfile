FROM tomcat:8-jre8
ADD scripts/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
COPY target/evaluation-results-service.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]