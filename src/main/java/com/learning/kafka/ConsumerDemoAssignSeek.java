package com.learning.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;


public class ConsumerDemoAssignSeek {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class.getName());

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        TopicPartition partitionToReadFrom = new TopicPartition("first_topic", 0);
        consumer.assign(Arrays.asList(partitionToReadFrom));

        consumer.seek(partitionToReadFrom, 3L);

        int messagesToRead = 3;
        int messagesRead = 0;
        boolean keepReading = true;

        while (keepReading) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record: records) {
                messagesRead++;
                logger.info("Key: {} Value: {} Partition: {} Offset: {}",
                        record.key(), record.value(), record.partition(), record.offset());
                if(messagesRead >= messagesToRead) {
                    keepReading = false;
                    break;
                }
            }
        }
        logger.info("App exiting");

    }
}
