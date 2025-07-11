package com.example.hexagonal.application.usecase;

import com.example.hexagonal.application.port.input.CreateCustomerInputPort;
import com.example.hexagonal.application.port.output.AddressLookupOutputPort;
import com.example.hexagonal.application.port.output.CustomerPersistenceOutputPort;
import com.example.hexagonal.domain.Customer;

public class CreateCustomerUseCase implements CreateCustomerInputPort {

    private final AddressLookupOutputPort addressLookupOutputPort;
    private final CustomerPersistenceOutputPort customerPersistenceOutputPort;

    public CreateCustomerUseCase(AddressLookupOutputPort addressLookupOutputPort,
            CustomerPersistenceOutputPort customerPersistenceOutputPort) {
        this.addressLookupOutputPort = addressLookupOutputPort;
        this.customerPersistenceOutputPort = customerPersistenceOutputPort;
    }

    // this.cpfValidationMessagePort = cpfValidationMessagePort;

    public void create(Customer customer, String zipCode) {
        System.out.println("🎯 Entrou CreateCustomerUseCase: ");
        var address = addressLookupOutputPort.findByZipCode(zipCode);
        System.out.println("🎯 address: " + address.getCity());
        customer.setAddress(address);
        System.out.println("🎯 Entrou CreateCustomerUseCase: " + customer.show());
        customerPersistenceOutputPort.save(customer);
        // cpfValidationMessagePort.sendCpfForValidation(customer.getCpf());
    }

}
