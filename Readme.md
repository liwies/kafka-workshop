# Kafka Workshop Setup
These steps guide you through setting up a Kafka environment using Docker, creating a Kafka topic, and building Spring Boot producer and consumer services. You'll learn to send messages to Kafka, consume them, and then use Kafka Streams to process and filter these messages into a new topic based on their content, demonstrating stream processing.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#step-1-installation)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting) 
- [References](#references)

## Prerequisites

Before you begin, ensure you have the following installed and configured:

- Docker, Podman or Rancher
- Docker Compose interpreter
- Maven (for building Java services)
- A terminal or command-line interface
- Optionally, Gitbash for easier command-line operations
- Optionally, Postman for sending HTTP requests
- Java 17 (and up)

## Step 1: Installation

1.  **Start Kafka UI:** Navigate to the `kafka-workshop` folder in your terminal and run:
    ```bash
    docker-compose up kafka-ui -d
    ```

2.  **Verify Kafka Container:** Open a new terminal and run:
    ```bash
    docker ps
    ```
    You should see a container named `kafka0` running.
    
3.  **Access Kafka Broker Shell:** You can access the `kafka0` container's shell using either of the following methods:
    * **Docker Management Interface:** Right-click on the `kafka0` container and select 'Exec'. Then, type `/bin/bash`.
    * **Command Line:** Run the following command in your terminal:
        ```bash
        docker exec -it kafka0 /bin/bash
        ```

4.  **Create Kafka Topic:** Once inside the `kafka0` container's shell, create the `user-events` topic:
    ```bash
    kafka-topics --create --bootstrap-server localhost:9092 --topic user-events --partitions 3 --replication-factor 1
    ```

5.  **Verify the Topic in Kafka UI:** Open your web browser and navigate to the Kafka UI: [http://localhost:8080/ui/clusters/local/all-topics?perPage=25](http://localhost:8080/ui/clusters/local/all-topics?perPage=25). You should now see the `user-events` topic listed.

## Usage

This section outlines how to build and run the producer, consumer, and streams services.

### Step 2: Producer Service

1.  **Build Producer Container:** Navigate to the `producer-service` module in your terminal and run the following commands:
    ```bash
    mvn clean package
    docker build -t producer-service .
    docker-compose up -d producer-service
    ```

2.  **Verify Producer Container:** Run `docker ps`. You should see a container named `kafka-workshop-producer-service-1` running.

3.  **Produce Initial Message:**
    * Check the Kafka UI ([http://localhost:8080/ui/clusters/local/all-topics?perPage=25&hideInternal=true](http://localhost:8080/ui/clusters/local/all-topics?perPage=25&hideInternal=true)). 
    *  Under "Number of messages" for the `user-events` topic, you should see zero messages.

4.  **Produce Messages:**
    * Copy the content of the `data/sample-data-login` file (e.g. 'producer-service set 1') from the `producer-service` directory.
    * Open a new command-line (gitbash) terminal and paste the copied data, hit enter to send the data to the topic. 
    * Check the Kafka UI ([http://localhost:8080/ui/clusters/local/all-topics?perPage=25&hideInternal=true](http://localhost:8080/ui/clusters/local/all-topics?perPage=25&hideInternal=true)).
    * Under "Number of messages" for the `user-events` topic, you should now see 1 message.

### Step 3: Consumer Service

1.  **Build Consumer Container:** Navigate to the `consumer-service` module in your terminal and run the following commands:
    ```bash
    mvn clean package -DskipTests
    docker build -t consumer-service .
    docker-compose up -d consumer-service
    ```

2.  **View Consumer Logs:** Go to your `consumer-service` output log. You should see the messages from the `user-events` topic being consumed.

3.  **Publish More Messages:** Publish a few more message(s) from the `sample-login-data` file using the method described in the Producer Service section. Observe the consumer logs to see them being processed.

### Step 4: Streams Service

1.  **Build Streams Container:** Navigate to the `streams-service` module in your terminal and run the following commands:
    ```bash
    mvn clean package -DskipTests
    docker build -t streams-service .
    docker-compose up -d streams-service
    ```

2.  **View Streams Logs:** Go to the `streams-service` output log. You should see messages being consumed from the `user-events` topic and directed to a new topic named `login-events`.

3.  **Observe in Kafka UI:** Navigate to the Kafka UI ([http://localhost:8080/](http://localhost:8080/)).

4.  **Record Initial Message Counts:** Note the initial message counts for both the `login-events` and `user-events` topics.

5.  **Produce Login Event:** Produce a message from `sample-data-login` (e.g., `producer-service set 16`). Observe that the message counts for both `login-events` and `user-events` increase.

6.  **Produce Logout Event:** Produce a message from `sample-data-logout` (e.g., `producer-service set 4`). Observe that only the message count for `user-events` increases, while `login-events` remains the same. 
    * **This demonstrates the Kafka Streams service filtering and routing login events to a separate topic.**


## Troubleshooting

1.  **Cannot publish to your producer through command line :** As an alternative use Kafka UI ([http://localhost:8080/](http://localhost:8080/) Topics --> user-events and use the "Produce Message" option.
2.  **Fatal error compiling, release version 17 not supported :** Ensure your JAVA_HOME is set to 17
3.  **Port already bind exception :** For the service in question change the port number in application.yml, or stop the service using the specific port

## References

* https://docs.kafka-ui.provectus.io/configuration/configuration-file
* https://env.simplestep.ca/
* https://github.com/provectus/kafka-ui/blob/master/documentation/compose/kafka-ui.yaml
* https://spring.io/guides/gs/spring-boot
* https://docs.spring.io/spring-kafka/reference/
* https://github.com/confluentinc/
