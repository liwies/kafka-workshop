# kafka-workshop
Kafka, Spring Boot and the Art of Stream Processing

NOTE: Connect to the capgemini network for dependencies download
NOTE: Maybe override the settings.xml file
NOTE: choose application.properties vs application.yaml

https://docs.kafka-ui.provectus.io/configuration/configuration-file
https://env.simplestep.ca/
https://github.com/provectus/kafka-ui/blob/master/documentation/compose/kafka-ui.yaml




Steps
1. Open terminal and run docker ps, you will see kafka0 as a container
2. Exec into the pod. In your services right-click on the container and click 'Exec' --> type in /bin/bash
3. Once in let's create a topic with the following command:
4. kafka-topics --create --bootstrap-server localhost:9092 --topic user-events --partitions 3 --replication-factor 2
5. You will notice that we cannot create the topic as replica set is 1, we can add a second broker kafka1 or set the replica set to 1
6. run 'kafka-topics --create --bootstrap-server localhost:9092 --topic user-events --partitions 3 --replication-factor 1'
7. Now go to our kafka-ui, you should see the topic now
8. Next step is to produce a message. 
9. From producer-service --> copy the content data/sample-data  'producer-service set 1'
10. Open a command line / terminal and paste the data
11. Exec into the ksqlDB CLI pod. In your services right-click on the container and click 'Exec' --> type in /bin/bash
12. Type: ksql http://ksqldb-server:8088
13. Create the producer container running the below:
    14. mvm clean package
    15. Run: 'docker build -t producer-service .' to build the docker image
    16. Run: 'docker run -p 8084:8084 producer-service' or run producer-service from docker-compose
17. Goto docker-compose.yml and run 
18. Goto ksqldb-script and copy & paste the following:
    14. Create a stream from the Kafka topic, defining the schema. --> This created our streams
    15. Create a new stream that filters events. --> This filters our stream for certain events
