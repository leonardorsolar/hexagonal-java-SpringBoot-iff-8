# Em contrução

# Tutorial Arquitetura Hexagonal - CRUD de Usuários | API + MongoDB (NoSQL) + Kafka (Mensageria)

Aprenda na prática como aplicar a **Arquitetura Hexagonal** em microsserviços utilizando **Java**, **Spring Boot**, **MongoDB** e **Kafka**.

Neste projeto, construiremos um **CRUD de Clientes**, explorando todas as camadas da arquitetura de forma clara e orientada.

### **Rode a aplicação**

Acesse o arquivo HexagonalApplicationTests.java:
Em src/test/java/com/example/hexagonal/HexagonalApplicationTests.java
Execute pela IDE (clique no botão de "Run Application")

ou

Você pode executar diretamente com o Maven:

```bash
mvn spring-boot:run
```

sua aplicação está todando na porta:
http://localhost:8080

Aparecerá: 🚀 API Hexagonal está no ar!

### **Subir o MongoDB com Docker**

No terminal, dentro da pasta `docker-local`, rode:

```bash
docker-compose up -d
```

## Rodar o test

```bash
mvn test
```

ou dar um run em src/main/java/com/example/hexagonal/HexagonalApplication.java

## 🧪 Verifique se está tudo certo

MongoDB rodando em `localhost:27017`

Você pode acessar o Mongo Express em:

```
http://localhost:8081
```

Perfeito! Seu `docker-compose.yml` está configurado para usar autenticação com:

-   **Usuário:** `root`
-   **Senha:** `123456789`
-   **Host:** `localhost` (porta `27017` mapeada para o container)

# Testes:

### 🧪 Usando `curl` (linha de comando)

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
        "name": "João Silva",
        "cpf": "12345678900",
        "zipCode": "01001-000"
      }'
```

Para visualizar a lista de clientes, use no navegador

```bash
http://localhost:8080/api/v1/customers
```

---

# Tutorial: Usando Confluent Kafka com VSCode e Spring Boot

---

**tutorial completo passo a passo** para rodar Kafka com Spring Boot usando Docker e visualizar tudo com o **Kafka UI** (interface web moderna). Isso funciona 100% no Ubuntu sem precisar da extensão Confluent do VSCode.

---

# ✅ Tutorial Completo: Spring Boot + Kafka + Kafka UI com Docker

---

## 🧱 1. Estrutura do projeto

Crie uma estrutura de projeto como esta:

```
spring-kafka-hexagonal/
├── docker-compose.yml
├── src/
│   └── main/
│       └── java/
│           └── com/example/kafka/
│               ├── KafkaApplication.java
│               └── producer/
│                   └── MessageProducer.java
│               └── consumer/
│                   └── MessageConsumer.java
└── application.yml
```

---

## ⚙️ 2. `docker-compose.yml`

Atualize o arquivo
docker-local/docker-compose.yml

```yml
version: "3"

services:
    mongo:
        image: mongo
        environment:
            MONGO_INITDB_ROOT_USERNAME: root
            MONGO_INITDB_ROOT_PASSWORD: 123456789
        ports:
            - "27017:27017"
        # volumes:
        #   - /home/hexagonal/Desenvolvimento/Docker/Volumes/MongoDB:/data/db
        networks:
            - hexagonal-network

    mongo-express:
        image: mongo-express
        ports:
            - 8081:8081
        environment:
            ME_CONFIG_BASICAUTH_USERNAME: root
            ME_CONFIG_BASICAUTH_PASSWORD: 123456789
            ME_CONFIG_MONGODB_ADMINUSERNAME: root
            ME_CONFIG_MONGODB_ADMINPASSWORD: 123456789
            ME_CONFIG_MONGODB_URL: mongodb://root:123456789@mongo:27017/
        networks:
            - hexagonal-network

    zookeeper:
        image: bitnami/zookeeper:latest
        ports:
            - "2181:2181"
        environment:
            - ALLOW_ANONYMOUS_LOGIN=yes
        networks:
            - hexagonal-network

    kafka:
        image: bitnami/kafka:3.4.0
        ports:
            - "9092:9092"
        environment:
            - KAFKA_ENABLE_KRAFT=no # <--- ESSENCIAL!
            - KAFKA_BROKER_ID=1
            - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092
            - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
            - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
            - ALLOW_PLAINTEXT_LISTENER=yes
        depends_on:
            - zookeeper
        networks:
            - hexagonal-network

    kafka-ui:
        image: provectuslabs/kafka-ui
        ports:
            - "9080:8080"
        environment:
            - KAFKA_CLUSTERS_0_NAME=hexagonal
            - KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9092
        depends_on:
            - kafka
        networks:
            - hexagonal-network

networks:
    hexagonal-network:
        driver: bridge
```

---

## 🚀 3. Subir o ambiente com Docker

No terminal, execute:

```bash
docker-compose up -d
```

### 🔍 Acessando o Kafka UI

Depois, acesse o Kafka UI: [http://localhost:9080](http://localhost:9080)

Abra no navegador:

```
http://localhost:9080
```

-   Você verá o cluster com nome `hexagonal`.
-   Navegue pelos **tópicos**, **consumidores**, **brokers** e **mensagens**.
-   Pode **criar tópicos** como `tp-cpf-validation` diretamente na interface.

Para **criar um tópico no Kafka UI** com o nome `tp-cpf-validation`, siga este passo a passo no navegador:

---

### ✅ Passos para Criar o Tópico `tp-cpf-validation` no Kafka UI

1. **Acesse o Kafka UI**:
   Abra [http://localhost:9080](http://localhost:9080) no navegador.

2. **No menu lateral**, clique em **"Topics"**.

3. No canto superior direito, clique em **"add a topic"**.

4. Preencha o formulário da seguinte maneira:

    - **Topic Name**:
      `tp-cpf-validation`

    - **Number of Partitions**:
      `1` (ou mais, se quiser paralelismo; geralmente `1` para testes)

    - **Replication Factor**:
      `1` (se você tiver só um broker, esse é o valor correto)

    - **Cleanup policy**:
      `Delete` (padrão; remove mensagens após o tempo definido)

    - **Time to retain data (in ms)**:
      Escolha, por exemplo, `7 days` (ou o tempo que fizer sentido pro seu caso)

    - **Max size on disk in GB**:
      Pode deixar como **Not Set** (sem limite)

    - **Maximum message size in bytes**:
      Deixe em branco, a menos que queira configurar um limite

    - **Custom parameters**:
      Ignore por enquanto, a menos que queira configurar propriedades específicas

5. Clique em **"Create Topic"** no final do formulário.

---

Após isso, o tópico `tp-cpf-validation` estará criado e visível na lista de tópicos. Você poderá clicar nele para ver mensagens, produzir eventos, visualizar consumidores, etc.

---

---

## 🌐 4. Configurar Spring Boot

Confirme se já está instalado

### Dependências no `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

---

### Arquivo `application.properties`:

```
seu-projeto/
├── docker-local/
│   └── docker-compose.yml
├── src/
│   └── main/
│       ├── java/...
│       └── resources/
│           └── application.properties

```

atualize

```bash
spring.application.name=hexagonal
# MongoDB
spring.data.mongodb.uri=mongodb://root:123456789@localhost:27017
spring.data.mongodb.database=hexagonal_db
logging.level.org.springframework.web=DEBUG

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=hexagonal-consumer-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# Criação automática de tópicos (opcional)
spring.kafka.admin.auto-create=true
```

### 🧪 Testando no Spring Boot

Crie um `Producer` e um `Consumer` e envie mensagens para o tópico que você criou. Você verá essas mensagens aparecendo no `Kafka UI`.

---

## 🧱 1. Configurar KafkaConsumerConfig e KafkaProducerConfig

### Classe de configuração `KafkaConsumerConfig.java`:

### Classe `KafkaConsumerConfig.java`:

src/main/java/com/example/hexagonal/config/KafkaApplication.java

```java
import com.arantes.hexagonal.adapters.in.consumer.message.CustomerMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, CustomerMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(GROUP_ID_CONFIG, "example");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CustomerMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
```

Antes de criar a configuração do consumidor, vamos criar um objeto que irá consumir o tópico para atualizar o cadastro do cliente.

Acesse e crie o ConsumerMessage.java dentro da pasta consumer/message

src/main/java/com/example/hexagonal/infrastructure/adapter/input/consumer/message/CustomerMessage.java

Este será o objeto que vou receber lá do kafta para atualizar os dados do cliente

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMessage {

    private String id;

    private String name;

    private String zipCode;

    private String cpf;

    private Boolean isValidCpf;

}
```

iremos pegar esses dados e alterar no cadastro do cliente.
A idéia aqui é enviar para tópico do kafka o cpf e a aplicaçao fictícia simular a validação do cpf retornando peo id do do cliente, comnome e o cep junto com o cpf também, falando se é válido ou não.

### Classe de configuração `KafkaProducerConfig.java`:

### Classe `KafkaProducerConfig.java`:

src/main/java/com/example/hexagonal/config/KafkaProducerConfig.java

```java
package com.arantes.hexagonal.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configProps.put(GROUP_ID_CONFIG, "example");
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
```

## 🧱 2. Implementar o produtor e o consumidor

A ideia seria:
1- Inserir os dados do cliente na api principal
2- Buscamos os dados do endereço na api CEP externa
3- Salvamos na base dados no MongoDB
4- Enviar um cpf do cliente para uma fila do kafka
5- A Api externa CPF vai consumir a fila do kafka: ler o cpf, fazer as validações necessárias
6- A Api externa CPF vai postar em outra fila os dados corretos do cliente
7- A Api principal irá consumir os dados corretos do cliente que irá ler do tópico e dar update no cadastro do cliente

---

## Producer - Parate do envio do cpf para kafka

Vamos inciar criando o produtor com o cpf que enviará para fila do kafka:

vamos acessar o usecase CreateCustomerUseCase.java
src/main/java/com/example/hexagonal/application/usecase/CreateCustomerUseCase.java

Em CreateCustomerUseCase.java precisamos enviar daods para uma fila do kafka

Assim, precisamos criar uma porta de saída para enviar a mensagem para o textrior (fila do kafka)

Como a responsabilidade da porta é enviar o CPF para validação externa, o nome da interface deve refletir o que a aplicação precisa que seja feito, sem se preocupar com como isso será implementado.

Sugestão de nome claro e expressivo: SendCpfForValidationOutputPort
Melhor nome para a implementação (Adaptador):SendCpfForValidationKafkaAdapter

```text
hexagonal/
├── application/
│   └── port/
│       └── output/
│           └── SendCpfForValidationOutputPort.java
├── infrastructure/
│   └── messaging/
│       └── kafka/
│           └── SendCpfForValidationKafkaAdapter.java
```

Nome da interface: focado no o que (ação de negócio que precisa ser feita).
Nome do adaptador: focado no como (implementação técnica específica).
Isso mantém o sistema limpo, testável e desacoplado de detalhes técnicos.

```java
public class CreateCustomerUseCase implements CreateCustomerInputPort {

    private final AddressLookupOutputPort addressLookupOutputPort;
    private final CustomerPersistenceOutputPort customerPersistenceOutputPort;
    private final SendCpfForValidationOutputPort sendCpfForValidationOutputPort;

    public CreateCustomerUseCase(AddressLookupOutputPort addressLookupOutputPort,
            CustomerPersistenceOutputPort customerPersistenceOutputPort, SendCpfForValidationOutputPort sendCpfForValidationOutputPort) {
        this.addressLookupOutputPort = addressLookupOutputPort;
        this.customerPersistenceOutputPort = customerPersistenceOutputPort;
        this.sendCpfForValidationOutputPort = sendCpfForValidationOutputPort;

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
        sendCpfForValidationOutputPort.send(customer.getCpf());
    }

}
```

Assim adiiconamos no usecase por injeção de dpendência a porta de saída e o métood de envio

Agora precisamos da interface e do adaptador:

### Interface do Produtor `SendCpfForValidationOutputPort.java`:

```java
package com.arantes.hexagonal.application.ports.out;

public interface SendCpfForValidationOutputPort {

    void send(String cpf);

}
```

### Implementação da classe concreta do Produtor `SendCpfForValidationKafkaAdapter.java`:

criaremos o adapter em kafka/SendCpfForValidationKafkaAdapter.java

```text
hexagonal/
├── application/
│   └── port/
│       └── output/
│           └── SendCpfForValidationOutputPort.java
├── infrastructure/
│   └── adapter/
│         └── output/
│                 └── kafka/
│                      └── SendCpfForValidationKafkaAdapter.java
```

```java
package com.arantes.hexagonal.adapters.out;

import com.arantes.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendCpfForValidationAdapter implements SendCpfForValidationOutputPort {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String cpf) {
        kafkaTemplate.send("tp-cpf-validation", cpf);
    }

}
```

Envio para o kafka
Basicamente injetamos o kafka template
por parâmetros enviamos o tópico e os dados

Precisamos atualizar o bean:

src/main/java/com/example/hexagonal/config/CreateCustomerConfig.java

```java
@Configuration
public class CreateCustomerConfig {

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(
            ViaCepAddressAdapter viaCepAddressAdapter,
            MongoCustomerRepositoryAdapter mongoCustomerRepositoryAdapter,
            SendCpfForValidationAdapter sendCpfForValidationAdapter) {
        return new CreateCustomerUseCase(viaCepAddressAdapter, mongoCustomerRepositoryAdapter, sendCpfForValidationAdapter);
    }

}
```

## Consumer

Agora criaremos o consumidor

```text
hexagonal/
├── application/
│   └── port/
│       └── output/
│           └── SendCpfForValidationOutputPort.java
├── infrastructure/
│   └── adapter/
│         └── input/
│                 └── consumer/
│                       └── ReceuveValidatedCpfConsumer.java
│
```

### Consumidor `ReceuveValidatedCpfConsumer.java`:

Precisamso receber a mensagem que virá do kafka

```java
//package com.arantes.hexagonal.adapters.in.consumer;

import com.arantes.hexagonal.adapters.in.consumer.mapper.CustomerMessageMapper;
import com.arantes.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.arantes.hexagonal.application.ports.in.UpdateCustomerInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveValidatedCpfConsumer {

    @Autowired
    private UpdateCustomerInputPort updateCustomerInputPort;

    @Autowired
    private CustomerMessageMapper customerMessageMapper;

    @KafkaListener(topics = "tp-cpf-validated", groupId = "example")
    public void receive(CustomerMessage customerMessage) {
        var customer = customerMessageMapper.toCustomer(customerMessage);
        updateCustomerInputPort.update(customer, customerMessage.getZipCode());
    }

}
```

Temos que criar o mapper:

```java
package com.arantes.hexagonal.adapters.in.consumer.mapper;

import com.arantes.hexagonal.adapters.in.consumer.message.CustomerMessage;
import com.arantes.hexagonal.application.core.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMessageMapper {

    @Mapping(target = "address", ignore = true)
    Customer toCustomer(CustomerMessage customerMessage);

}
```

é necessário implementar o updateCustomerInputPort

---

## 🧪 5. Rodando tudo

Você pode:

1. Rodar o app com `mvn spring-boot:run`
2. Enviar dados do cliente via post em `http://localhost:8081/api/v1/customers`
3. visualizar a lista de clientes no navegador http://localhost:8080/api/v1/customers
4. Acessar o banco de dadod do mongodb em http://localhost:8081 via Mongo Express
5. Buscar o cliente por id http://localhost:8080/api/v1/customers/colar o id
6. Acessar o kafka no navegador http://localhost:9080/ e verificar se foi enviado ( mostra só o binário)
7. Testar o update em `http://localhost:8081/api/v1/customers`
8. Testar a mensagem do kafka do cpf validado no tópico tc-cpf-validated porque o nosso consumidor fica escultando
   (basta publicar a mensagem - publish single message ) Muda-se o cep e que o cpf está validado
9. Testar a deleção do cliente

### 🧪 Usando `curl` (linha de comando)

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
        "name": "João Silva",
        "cpf": "12345678900",
        "zipCode": "01001-000"
      }'
```

Para visualizar a lista de clientes, use no navegador

```bash
http://localhost:8080/api/v1/customers
```

MongoDB rodando em `localhost:27017`

Você pode acessar o Mongo Express em:

```
http://localhost:8081
```

Perfeito! Seu `docker-compose.yml` está configurado para usar autenticação com:

-   **Usuário:** `root`
-   **Senha:** `123456789`
-   **Host:** `localhost` (porta `27017` mapeada para o container)

---

## ✅ Pronto!

Você agora tem um ambiente **Kafka completo** com Spring Boot e interface moderna.

## Extras:

Forçar remoção limpando todas as redes e volumes
docker compose down --volumes --remove-orphans
docker network prune -f

docker compose down --volumes --remove-orphans
docker network prune -f

## Biblioteca archunit

https://www.archunit.org/userguide/html/000_Index.html

### Definindo acessos entre camadas

Teste para verificar que somente a camada de config poderá acessar a camada de adapatadores
Assim só vamos permitir que acesse somnete a cadamada de dadapatadores a camada de configuração

### Garantindo que as classes estejam nos pacotes corretos

### Garantindo que um pacote tenha determinado sufixo
