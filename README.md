# kafka-workshop
Kafka, Spring Boot and the Art of Stream Processing

1.  **Prerequisite:** Run `docker-compose up kafka-ui -d` from a terminal within the `kafka-workshop` folder.
2.  Open a terminal and run `docker ps`. You will see `kafka0` as a container.
3.  Access the `kafka0` container's shell. In your Docker management interface, right-click on the container and select 'Exec' --> type in `/bin/bash`.
4.  Alternatively, run the following from the command line: `docker exec -it kafka0 /bin/bash`.
5.  Once inside the `kafka0` container, create a topic with the following command:
    ```bash
    kafka-topics --create --bootstrap-server localhost:9092 --topic user-events --partitions 3 --replication-factor 1
    ```
6.  Now, go to your Kafka UI (http://localhost:8080/ui/clusters/local/all-topics?perPage=25). You should now see the `user-events` topic.
7.  **Producer Service (producer-service):**
    * Create the producer container by running the following commands under the module (`producer-service`):
        * i) In a terminal run: `mvn clean package`
        * ii) Once done, execute `docker build -t producer-service .`. This will build the Docker container for the producer service.
        * iii) Once done, execute `docker-compose up -d producer-service`.
        * iv) Look at the ui, there should be one message in the Topic user-ebents under "Number of messages"
8.  Run `docker ps`, you should see `kafka-workshop-producer-service-1`.
9.  The next step is to produce a message.
    * i) From the `producer-service` directory, copy the content of the `data/sample-data-login` file (specifically, 'producer-service set 1').
    * ii) Open a command line (gitbash) terminal and paste the data. Alternatively, under Postman, import the collection into Postman.
10. **Consumer Service (consumer-service):**
    * Create the consumer container by running the following commands under the module (`consumer-service`):
        * i) In a terminal run: `mvn clean package -DskipTests` (This packages our Spring Boot project into a deployable jar).
        * ii) Once done, execute `docker build -t consumer-service .`. This will build the Docker container for the consumer service.
        * iii) Once done, execute `docker-compose up -d consumer-service`. This launches the container and `consumer-service` in detach mode.
11. Go to your `consumer-service` output log and note that the messages from the topic have been consumed.
        * i) Publish a few more messages from sample-login-data
12. Let’s say we are only interested in events that are triggered during login. We want to send those to a different topic so that we can build dashboards on how many users logged in during a specific time period.
13. This is where Kafka Streams come in. We can aggregate a different set of events into their own classification.
14. **Streams Service (streams-service):**
    * Create the streams container by running the following commands under the module (`streams-service`):
        * i) In a terminal run: `mvn clean package -DskipTests`
        * ii) Once done, execute `docker build -t streams-service .`. This will build the Docker container for the producer service.
        * iii) Once done, execute `docker-compose up -d streams-service`.
15. Go to the `streams-service` output log. You notice messages are now being consumed from the topic: `user-events` and directed to `login-events`.
16. To view this in action, go to: http://localhost:8080/
17. Record the message count of `login-events` & `user-events`.
18. Produce a message from `sample-data-login` (e.g., `producer-service set 16`).
19. You will notice that both the message count from `login-events` & `user-events` increased.
20. Produce a message from `sample-data-logout` (e.g., `producer-service set 4`).
21. You will notice that only the message count from `user-events` increased.

**Recap:**

* **Kafka Setup:**
    * A Kafka broker is running in Docker.
    * A `user-events` topic is created.
* **Producer Service:**
    * A Docker container sends messages to the `user-events` topic.
* **Consumer Service:**
    * A Docker container consumes and logs messages from the `user-events` topic.
* **Kafka Streams Service:**
    * A Docker container using Kafka Streams filters `user-events` for login events.
    * Login events are published to a new `login-events` topic.
* **Verification:**
    * Kafka UI shows both topics.
    * Producing login events increments counts on both topics.
    * Producing logout events only increments the `user-events` topic.

**Trouble shooting**
* Fatal error compiling: error: release version 17 not supported --> Ensure your JAVA_HOME is set to 17

**References**
* https://docs.kafka-ui.provectus.io/configuration/configuration-file
* https://env.simplestep.ca/
* https://github.com/provectus/kafka-ui/blob/master/documentation/compose/kafka-ui.yaml