package com.example.hexagonal.domain;

//import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

public class Customer {

    public Customer() {
        this.isValidCpf = false;
    }

    public Customer(String id, String name, Address address, String cpf, Boolean isValidCpf) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cpf = cpf;
        this.isValidCpf = isValidCpf;
    }

    private String id;

    private String name;

    private Address address;

    private String cpf;

    private Boolean isValidCpf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getIsValidCpf() {
        return isValidCpf;
    }

    public void setIsValidCpf(Boolean isValidCpf) {
        this.isValidCpf = isValidCpf;
    }

    // Método show para exibir os dados formatados do Customer
    public String show() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer {\n");
        sb.append("  id: ").append(id).append(",\n");
        sb.append("  name: ").append(name).append(",\n");
        sb.append("  cpf: ").append(cpf).append(",\n");
        sb.append("  isValidCpf: ").append(isValidCpf).append(",\n");
        sb.append("  address: ");
        if (address != null) {
            sb.append("{ street: ").append(address.getStreet())
                    .append(", city: ").append(address.getCity())
                    .append(", state: ").append(address.getState())
                    .append(" }");
        } else {
            sb.append("null");
        }
        sb.append("\n}");
        return sb.toString();
    }

}
