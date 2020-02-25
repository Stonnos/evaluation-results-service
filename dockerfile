FROM openjdk:8
ADD scripts/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
COPY target/evaluation-results-service.war evaluation-results-service.war
ENTRYPOINT exec java -jar evaluation-results-service.war