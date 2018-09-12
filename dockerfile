FROM tomcat:8-jre8
RUN mkdir /home/evaluationResultsStorage
ADD scripts/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT [ "/wait-for-it.sh", "ers-db:5432", "--" ]
COPY target/evaluation-results-service.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]