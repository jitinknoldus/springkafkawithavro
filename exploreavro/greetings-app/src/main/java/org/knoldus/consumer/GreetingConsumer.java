package org.knoldus.consumer;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.knoldus.Greeting;
import org.knoldus.producer.GreetingProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class GreetingConsumer {

    private static final String GREETING_TOPIC = "greeting";
    private static final Logger logger = LoggerFactory.getLogger(GreetingConsumer.class);

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"greeting.consumer");

        KafkaConsumer<String,byte[]> consumer = new KafkaConsumer<String, byte[]>(properties);

        consumer.subscribe(Collections.singleton(GREETING_TOPIC));

        logger.info("Consumer Started");

        while (true){
           ConsumerRecords<String, byte[]> records  = consumer.poll(Duration.ofMillis(100));

           for (ConsumerRecord<String, byte[]> record : records){

              try{
                  Greeting greeting = decodeAvroGreeting(record.value());
                  logger.info("Consumed Message , key : {}, value : {}",record.key(), greeting.toString());
              }
              catch (Exception e){
                  logger.error("Exception is: {}",e.getMessage(),e);
              }

           }

        }

    }

    private static Greeting decodeAvroGreeting(byte[] array) throws IOException {

        return Greeting.fromByteBuffer(ByteBuffer.wrap(array));

    }

}
