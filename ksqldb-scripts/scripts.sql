-- Create a stream from the Kafka topic, defining the schema.
CREATE STREAM user_events_stream (
    user_id VARCHAR KEY,
    event_payload STRUCT<event_type VARCHAR, timestamp BIGINT>
) WITH (
    KAFKA_TOPIC = 'user-events',
    VALUE_FORMAT = 'JSON'
);

-- Create a new stream that filters events.
CREATE STREAM login_events_stream AS
SELECT
    user_id,
    event_payload.event_type AS event_type,
    event_payload.timestamp AS event_timestamp
FROM user_events_stream
WHERE event_payload.event_type = 'login';

-- Print the filtered events to the console.
SELECT * FROM login_events_stream EMIT CHANGES;

-- Example of creating an aggregate
CREATE TABLE user_login_counts AS
SELECT
    user_id,
    COUNT(*) AS login_count
FROM user_events_stream
WHERE event_payload.event_type = 'login'
GROUP BY user_id;

SELECT * FROM user_login_counts EMIT CHANGES;