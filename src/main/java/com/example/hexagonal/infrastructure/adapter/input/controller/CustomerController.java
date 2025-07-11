package com.example.hexagonal.infrastructure.adapter.input.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonal.application.port.input.CreateCustomerInputPort;
import com.example.hexagonal.infrastructure.adapter.input.controller.mapper.CustomerMapper;
import com.example.hexagonal.infrastructure.adapter.input.controller.request.CustomerRequestDTO;
import com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CreateCustomerInputPort createCustomerInputPort;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MongoCustomerRepository repository;

    @PostMapping
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        System.out.println("🚀 Método createCustomer foi chamado");
        var customer = customerMapper.toCustomer(customerRequestDTO);
        System.out.println("🎯 Dados recebidos do mapper: " + customer.show());
        System.out.println("🎯 getZipCode: " + customerRequestDTO.getZipCode());
        createCustomerInputPort.create(customer, customerRequestDTO.getZipCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<CustomerEntity> listCustomers() {
        return repository.findAll(); // ou usar uma porta e mapper se quiser manter arquitetura 100%
    }

    @GetMapping("/")
    public String healthCheck() {
        return "🚀 API Hexagonal está no ar!";
    }

}
