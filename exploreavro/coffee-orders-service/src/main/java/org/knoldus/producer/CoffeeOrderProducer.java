package org.knoldus.producer;

import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.knoldus.domain.generated.CoffeeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.knoldus.util.CoffeeOrderUtil.buildNewCoffeeOrder;

public class CoffeeOrderProducer {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeOrderProducer.class);
    private static final String COFFEE_ORDERS = "coffee-orders";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(properties);

        CoffeeOrder coffeeOrder = buildNewCoffeeOrder();

        byte[] value = coffeeOrder.toByteBuffer().array();

        ProducerRecord<String, byte[]> producerRecord =
                new ProducerRecord<>(COFFEE_ORDERS,value);

        var recordMetaData = producer.send(producerRecord).get();
//        Future<RecordMetadata>
        logger.info("recordMetaData :{}"+ recordMetaData);


    }

}
