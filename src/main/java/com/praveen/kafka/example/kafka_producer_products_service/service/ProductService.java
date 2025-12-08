package com.praveen.kafka.example.kafka_producer_products_service.service;

import com.praveen.kafka.example.kafka_producer_products_service.model.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel);
}
