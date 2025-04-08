# Kafka Workshop Setup (Time to spare)

If you have completed the main `Readme.md` and have `time to spare`, you can continue with the tasks below. **Tip: you can use the internet to research.** 😉

## Table of Contents

- [Prerequisites](#prerequisites)
- [Limitations and Improvements for Reliability and Performance](#limitations-and-improvements-for-reliability-and-performance)
- [Modifications to the Project](#modifications-to-the-project)
- [Troubleshooting](#troubleshooting)
- [References](#references)

## Prerequisites

You have completed the steps contained in the `Readme.md` file.

## Limitations and Improvements for Reliability and Performance

Our current Kafka cluster infrastructure has a significant limitation: it only supports a **replication factor of 1**. This means that each partition of our topics exists on a single broker, making our system vulnerable to data loss if that broker becomes unavailable.

You are tasked with improving the balance between **reliability** (ensuring data durability and availability) and **performance** (maintaining acceptable throughput and latency).

To achieve this, we will align our Kafka configuration with concepts similar to the recommended settings for robust Kafka deployments, aiming for a configuration that provides a good trade-off. Specifically, we will aim for a configuration that, in a fully functional environment, would resemble:

* **Replication Factor: 3** (The target state for optimal reliability)
* **`min.insync.replicas`: 2** (The target state to ensure data is written to at least two replicas before acknowledgement)
* **`acks=all`** (The target state to ensure the producer waits for all in-sync replicas to acknowledge writes)

## Modifications to the Project

Given our current infrastructure limitations (replication factor of 1), we will make the following adjustments to our project setup and configuration to prepare for a future environment that supports higher replication:

1.  **Increase the Broker Topology to 3:**
    * Modify your infrastructure configuration to provision and run **three** distinct Kafka broker instances. This will lay the groundwork for enabling replication across multiple brokers once the underlying infrastructure allows.

2.  **Adjust the Replication Factor for the Topic to 2:**
    * Update the configuration for your Kafka topic to specify a **replication factor of 2**. While our current cluster might only effectively maintain one replica per partition, setting this configuration will ensure that when the cluster is expanded to three or more brokers, Kafka will automatically create and manage the desired number of replicas.

3.  **Ensure Acknowledgement Configuration (`acks=all`):**
    * Configure your Kafka Producer to use the `acks=all` setting. This instructs the producer to wait for acknowledgement from all in-sync replicas before considering a write successful. While with a replication factor of 1, this effectively means waiting for the leader to acknowledge, it prepares our application for the stronger durability guarantees provided by a higher replication factor in the future.
    * Ensure your throughput is not blocked by using `CompletableFuture`.

## Troubleshooting

## References