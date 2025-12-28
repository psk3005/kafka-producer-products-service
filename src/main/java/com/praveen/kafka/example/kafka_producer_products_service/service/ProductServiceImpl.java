package com.praveen.kafka.example.kafka_producer_products_service.service;

import com.praveen.kafka.example.kafka_core.ProductCreatedEvent;
import com.praveen.kafka.example.kafka_producer_products_service.model.CreateProductRestModel;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService{

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity());

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>("product-created-events-topic",
                productId,
                productCreatedEvent);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send(record);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
                LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
                LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
                LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

            }
        });
        //making the call sync
        future.join();
        LOGGER.info("Returning product id");
        return productId;
    }
}
