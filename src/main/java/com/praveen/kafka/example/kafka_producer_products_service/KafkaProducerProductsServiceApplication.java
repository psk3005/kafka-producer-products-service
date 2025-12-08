package com.praveen.kafka.example.kafka_producer_products_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaProducerProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerProductsServiceApplication.class, args);
	}

}
