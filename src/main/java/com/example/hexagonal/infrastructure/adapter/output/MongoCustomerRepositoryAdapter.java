package com.example.hexagonal.infrastructure.adapter.output;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import com.example.hexagonal.infrastructure.adapter.output.repository.mapper.CustomerEntityMapper;

// pensar em nomear MongoCreateCustomerRepositoryAdapter
@Component
public class MongoCustomerRepositoryAdapter implements CustomerPersistenceOutputPort {

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private CustomerEntityMapper mapper;

    @Override
    public void save(Customer customer) {
        System.out.println("🎯 Dados recebidos do mapper: " + customer.show());
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        System.out.println("🎯 Dados recebidos: " + entity);
        repository.save(entity);
    }
}