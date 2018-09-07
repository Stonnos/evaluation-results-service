FROM tomcat:8-jre8
RUN MKDIR -p /home/evaluationResultsStorage
COPY target/evaluation-results-service.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]