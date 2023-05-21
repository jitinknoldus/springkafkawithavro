package com.knoldus.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.knoldus.domain.generated.CoffeeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CoffeeOrderConsumer {
    private static final Logger log = LoggerFactory.getLogger(CoffeeOrderConsumer.class);

    private static final String COFFEE_ORDERS_TOPIC = "coffee-orders";

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee.consumer");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(COFFEE_ORDERS_TOPIC));
        log.info("Consumer started!!!!!!");

        while(true){
            ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String , byte[]> records : consumerRecords) {
                try{
                    CoffeeOrder coffeeOrder = decodeCoffeeOrder(records.value());
                    log.info("Consumed Message, key : {}, value : {}", records.key(), coffeeOrder.toString());
                }
                catch (Exception e){
                    log.error("Exception is : {}", e.getMessage());

                }

            }
        }


    }

    public static CoffeeOrder decodeCoffeeOrder(byte[] array) throws IOException {

        return CoffeeOrder.fromByteBuffer(ByteBuffer.wrap(array));
    }
}
