FROM openjdk:11-jdk-slim
ADD scripts/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
COPY target/evaluation-results-service.war evaluation-results-service.war
ENTRYPOINT exec /wait-for-it.sh $DB_CONTAINER_WAIT --timeout=$DB_CONTAINER_WAIT_TIMEOUT -- java $JAVA_OPTS -jar evaluation-results-service.war
