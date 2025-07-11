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

## 🔁 Etapa 7: tutorial usado cpdificar e para Rodar a aplicação

Vamos agora realizar os seguintes passos para fazer tudo fucnionar:

## ✏️ Parte 1: Criar o Bean do create

src/main/java/com/example/hexagonal/config

src/main/java/com/example/hexagonal/config/CreateCustomerConfig.java

```java
package com.example.hexagonal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexagonal.application.usecase.CreateCustomerUseCase;
import com.example.hexagonal.infrastructure.adapter.output.MongoCustomerRepositoryAdapter;
import com.example.hexagonal.infrastructure.adapter.output.client.ViaCepAddressAdapter;

@Configuration
public class CreateCustomerConfig {

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(
            ViaCepAddressAdapter viaCepAddressAdapter,
            MongoCustomerRepositoryAdapter mongoCustomerRepositoryAdapter) {
        return new CreateCustomerUseCase(viaCepAddressAdapter, mongoCustomerRepositoryAdapter);
    }

}
```

## ✅ Criar um Endpoint GET para verificar os dados

em src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/CustomerController.java

método @GetMapping para listar os clientes está no caminho certo e incluir a injeção da dependência repository, que não está declarada no seu CustomerController

Crie algo simples para listar os clientes:

```java
@Autowired
    private MongoCustomerRepository repository; // 🔧 Adicionado aqui

@GetMapping
public List<CustomerEntity> listCustomers() {
    return repository.findAll(); // ou usar uma porta e mapper se quiser manter arquitetura 100%
}
```

# Criar um cliente

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
        "name": "João Silva",
        "cpf": "12345678900",
        "zipCode": "01001-000"
      }'
```

# Listar clientes

curl http://localhost:8080/api/v1/customers

## ✅ **✏️ Parte 2: Configurar o ambiente para rodar a aplicação**

### 📌 O que faremos:

-   Instalar o **Docker** e o **Docker Compose**
-   Criar um arquivo `docker-compose.yml` para subir o MongoDB e o Mongo Express
-   Subir os containers

---

### 🐧 **Passo 1: Instalar Docker no Linux**

Veja na documentação como instalar

Verifique se está funcionando:

```bash
docker --version
```

---

### 🐳 **Passo 2: Instalar o Docker Compose**

Veja na documentação como instalar

Verifique a instalação:

```bash
docker-compose --version
```

---

### 📁 **Passo 3: Criar a pasta `docker-local`**

No diretório raiz do seu projeto:

```bash
docker-local
```

---

### 📝 **Passo 4: Criar o arquivo `docker-compose.yml`**

Crie o arquivo:

```bash
docker-compose.yml
```

docker-local/docker-compose.yml

Cole o seguinte conteúdo:

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
            - mongo-compose-network

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
            - mongo-compose-network

networks:
    mongo-compose-network:
        driver: bridge
```

---

### 🚀 **Passo 5: Subir o MongoDB com Docker**

No terminal, dentro da pasta `docker-local`, rode:

```bash
docker-compose up -d
```

---

### ✅ **Tudo pronto!**

Você agora pode verificar no navegador:

-   MongoDB rodando em `localhost:27017`

    -   Acessando no navegador você vera a informação:It looks like you are trying to access MongoDB over HTTP on the native driver port.

-   ## Interface web do Mongo Express em `http://localhost:8081`

### ✅ Login do Mongo Express (interface web)

1. Acesse no navegador:

[http://localhost:8081](http://localhost:8081)

2. Insira:

-   **Username:** `admin`
-   **Password:** `123456789`

---

Se quiser, você pode alterar esses dados no `docker-compose.yml` e depois rodar novamente:

```bash
docker-compose down
docker-compose up -d
```

Perfeito! Seu `docker-compose.yml` está configurado para usar autenticação com:

-   **Usuário:** `root`
-   **Senha:** `123456789`
-   **Host:** `localhost` (porta `27017` mapeada para o container)

---

## ✅ Ajuste no `application.properties` ou `application.yml`

Para que sua aplicação Java consiga se autenticar no MongoDB, você **precisa configurar a URI de conexão com usuário e senha**, assim:

### 👉 `src/main/resources/application.properties`

```properties
spring.data.mongodb.uri=mongodb://root:123456789@localhost:27017
spring.data.mongodb.database=hexagonal_db
```

> Se quiser mudar o nome do banco, como `hexagonal_db`, ele será criado automaticamente na primeira operação.

---

## ✅ Reinicie sua aplicação

Depois de configurar isso, pare e reinicie sua aplicação com:

```bash
mvn clean spring-boot:run
```

ou

```bash
./mvnw spring-boot:run
```

---

## 🧪 Verifique se está tudo certo

Você pode acessar o Mongo Express em:

```
http://localhost:8081
```

Usuário: `root`
Senha: `123456789`

E verá os bancos criados, inclusive o `hexagonal_db` após a primeira chamada ao endpoint da sua API.

---

Isso indica que **nenhum banco de dados de aplicação foi criado ainda**, apenas os bancos **padrão do MongoDB**:

-   `admin`
-   `config`
-   `local`

---

### ✅ O que falta?

O MongoDB **só cria o banco de dados e as coleções** quando você faz **uma operação real** — por exemplo, uma requisição HTTP `POST` que insere um cliente via sua API.

---

### ✅ Teste agora seu endpoint `/api/v1/customers`

Faça uma chamada `POST` com um `JSON` semelhante a este:

```json
{
    "name": "João da Silva",
    "cpf": "12345678901",
    "zipCode": "01001000"
}
```

Você pode testar de várias formas:

#### ✔️ Via `curl`:

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"João da Silva", "cpf":"12345678901", "zipCode":"01001000"}'
```

#### ✔️ Via Postman ou Insomnia:

-   URL: `http://localhost:8080/api/v1/customers`
-   Método: `POST`
-   Body (JSON):

```json
{
    "name": "João da Silva",
    "cpf": "12345678901",
    "zipCode": "01001000"
}
```

---

### 🔍 Após isso...

1. Acesse novamente o **Mongo Express**: [http://localhost:8081](http://localhost:8081)
2. Você verá um novo banco com o nome `hexagonal_db` (ou o nome que você definiu em `application.properties`)
3. Dentro dele, verá a coleção (ex: `customers`)

---

Se quiser, envie aqui o conteúdo do seu `application.properties` e do DTO `CustomerRequestDTO` que eu posso confirmar se está tudo pronto para funcionar.

## 🧪 Verifique se está tudo certo

Você pode acessar o Mongo Express em:

```
http://localhost:8081
```

Usuário: `root`
Senha: `123456789`

E verá os bancos criados, inclusive o `hexagonal_db` após a primeira chamada ao endpoint da sua API.

---

## ✏️ Parte 2: Subir a aplicação java

Em src/main/java/com/example/hexagonal/HexagonalApplication.java clique em run

CustomerController:

src/main/java/com/example/hexagonal/infrastructure/adapter/input/controller/CustomerController.java

Perfeito! Seu controller `CustomerController` está configurado corretamente para receber requisições `POST` no endpoint:

```
POST http://localhost:8080/api/v1/customers
```

---

## ✅ Como testar seu endpoint

Você pode testar com ferramentas como **Postman**, **Insomnia** ou via `curl` no terminal. Abaixo, exemplos práticos.

---

### 🧪 1. Usando `curl` (linha de comando)

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
        "name": "João Silva",
        "cpf": "12345678900",
        "zipCode": "01001-000"
      }'
```

---

### 🧪 2. Usando Postman (ou Insomnia)

-   Método: `POST`
-   URL: `http://localhost:8080/api/v1/customers`
-   Headers:

    -   `Content-Type: application/json`

-   Body (raw, JSON):

```json
{
    "name": "João Silva",
    "cpf": "12345678900",
    "zipCode": "01001-000"
}
```

---

### ⚠️ Campos esperados

O DTO `CustomerRequestDTO` deve conter pelo menos os campos `name`, `cpf` e `zipCode`. Certifique-se de que ele esteja anotado com validações como:

```java
public class CustomerRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    @NotBlank
    private String zipCode;

    // getters e setters
}
```

---

### ✅ O que esperar como resposta

Como você está retornando:

```java
return ResponseEntity.ok().build();
```

A resposta será:

-   Status: `200 OK`
-   Sem corpo na resposta (`body` vazio)

---

### 📌 Próximos passos:

8. **Criação das configurações**

    - Criando as configurações do Kafla

9. **Criação do producer e consumer do kafka**

10. **Configuração do ambiente para rodar a aplicação**

---

Problemas com versões:

## ✅ Solução definitiva e recomendada

### 👉 Voltar para o **Java 17**, que é:

-   Suportado oficialmente pelo Spring Boot 3.3.x
-   Compatível com MapStruct, Lombok e o ecossistema em geral

---

## ✅ Passos para mudar para Java 17 no Linux

### 1. Instale o JDK 17 (se ainda não tiver):

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### 2. Exporte o `JAVA_HOME` e atualize o `PATH`

No terminal (vale para esta sessão):

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

Confirme com:

```bash
java -version
# Deve mostrar: openjdk version "17"
```

```bash
mvn -v
# Deve mostrar: Java version: 17
```

### 3. Agora rode:

```bash
mvn clean install
```

---

## ✅ Tornar o Java 17 permanente (opcional)

Se quiser que o Java 17 seja usado **sempre automaticamente**, adicione ao final do seu arquivo `~/.bashrc` ou `~/.zshrc`:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

Depois execute:

```bash
source ~/.bashrc
```

---

## Se ainda quiser usar Java 21 no futuro:

-   Será necessário atualizar o **MapStruct** para uma versão **experimental ou futura** (quando disponível).
-   Ou usar plugins alternativos que não dependam de APIs internas do `javac`.

# _Compilar e rodar sua aplicação Spring Boot _ Start no projeto

### 🚀 **Subir o MongoDB com Docker**

No terminal, dentro da pasta `docker-local`, rode:

```bash
docker-compose up -d
```

```bash
docker ps
```

```bash
docker-compose down
docker-compose up -d
```

### **Rode a aplicação**

Você pode executar diretamente com o Maven:

```bash
mvn spring-boot:run
```

sua aplicação está todando na porta:
http://localhost:8080

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

Você está no caminho certo ao adicionar o `GET` para listar os clientes! Porém, há **um pequeno erro no código**: a variável `repository` não foi declarada ou injetada no `CustomerController`.

---

## ✅ Correção Sugerida para litar clientes seguindo a arquitetura hexagonal

Para que o método `listCustomers()` funcione, você precisa injetar o repositório Mongo (`MongoCustomerRepository`) no controller — ou, melhor ainda, **usar um serviço ou porta apropriada** para manter a arquitetura hexagonal limpa.

### 🔧 Solução rápida (para testes locais)

Se o objetivo for **verificar rapidamente os dados salvos**, você pode **injetar diretamente o repositório** (mesmo que isso quebre um pouco a separação de camadas — tudo bem para teste manual):

```java
@Autowired
private com.example.hexagonal.infrastructure.adapter.output.repository.MongoCustomerRepository repository;

@GetMapping
public List<CustomerEntity> listCustomers() {
    return repository.findAll();
}
```

---

### 🧼 Solução ideal (seguindo a arquitetura hexagonal)

1. Criar uma nova **porta de saída**:

```java
package com.example.hexagonal.application.port.output;

import java.util.List;
import com.example.hexagonal.domain.Customer;

public interface LoadCustomerOutputPort {
    List<Customer> findAll();
}
```

2. Criar um **caso de uso** (opcional):

```java
package com.example.hexagonal.application.usecase;

import java.util.List;
import com.example.hexagonal.application.port.output.LoadCustomerOutputPort;
import com.example.hexagonal.domain.Customer;
import org.springframework.stereotype.Service;

@Service
public class ListCustomersUseCase {

    private final LoadCustomerOutputPort loadCustomerOutputPort;

    public ListCustomersUseCase(LoadCustomerOutputPort loadCustomerOutputPort) {
        this.loadCustomerOutputPort = loadCustomerOutputPort;
    }

    public List<Customer> execute() {
        return loadCustomerOutputPort.findAll();
    }
}
```

3. Implementar o adaptador no Mongo:

```java
@Component
public class MongoCustomerRepositoryAdapter implements CustomerPersistenceOutputPort, LoadCustomerOutputPort {

    @Autowired
    private MongoCustomerRepository repository;

    @Autowired
    private CustomerEntityMapper mapper;

    @Override
    public void save(Customer customer) {
        CustomerEntity entity = mapper.toCustomerEntity(customer);
        repository.save(entity);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll().stream()
                .map(mapper::toCustomer)
                .toList();
    }
}
```

4. Atualizar o `CustomerController`:

```java
@Autowired
private ListCustomersUseCase listCustomersUseCase;

@GetMapping
public List<Customer> listCustomers() {
    return listCustomersUseCase.execute();
}
```

---

## ✅ Resumo

| Abordagem                                                | Vantagem                      | Quando usar                    |
| -------------------------------------------------------- | ----------------------------- | ------------------------------ |
| Injetar o `MongoCustomerRepository` direto no controller | Rápido e prático              | Para testes rápidos e locais   |
| Usar portas + casos de uso + mappers                     | Arquitetura limpa e escalável | Produção e manutenções futuras |

---
