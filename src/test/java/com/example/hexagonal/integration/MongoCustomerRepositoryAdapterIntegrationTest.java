package com.example.hexagonal.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.hexagonal.domain.Address;
import com.example.hexagonal.domain.Customer;
import com.example.hexagonal.infrastructure.adapter.output.MongoCustomerRepositoryAdapter;
import com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository;
import com.example.hexagonal.infrastructure.adapter.output.repository.entity.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class MongoCustomerRepositoryAdapterIntegrationTest {

    @Autowired
    private MongoCustomerRepositoryAdapter adapter;

    @Autowired
    private MongoCustomerRepository repository;

    @Test
    public void deveSalvarCustomerNoMongo() {
        // Limpa a base para garantir o teste limpo
        repository.deleteAll();

        // Arrange
        Customer customer = new Customer();
        customer.setName("Maria Teste");
        customer.setCpf("99988877700");
        customer.setAddress(new Address("Rua Teste", "Cidade Teste", "ST"));

        // Act
        adapter.save(customer);

        var all = repository.findAll();
        System.out.println("Registros após save: " + all.size());

        // Assert
        Optional<CustomerEntity> saved = repository.findAll().stream()
                .filter(c -> "99988877700".equals(c.getCpf()))
                .findFirst();

        assertThat(saved).isPresent();
        assertThat(saved.get().getName()).isEqualTo("Maria Teste");
        assertThat(saved.get().getAddress().getCity()).isEqualTo("Cidade Teste");
    }
}

// mvn -Dtest=MongoCustomerRepositoryAdapterIntegrationTest test
