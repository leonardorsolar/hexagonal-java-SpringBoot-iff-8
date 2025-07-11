## ✅ **Compilar e rodar sua aplicação Spring Boot com MapStruct**

### 1️⃣ **Compile e gere os mappers com Maven**

Abra o terminal na raiz do projeto e execute:

```bash
mvn clean install
```

Ou, se quiser apenas compilar os arquivos sem rodar os testes:

```bash
mvn clean compile
```

> Isso garante que o **MapStruct** gere as classes de mapeamento (`*.Impl`) corretamente.

---

### 2️⃣ **Rode a aplicação**

Você pode executar diretamente com o Maven:

```bash
mvn spring-boot:run
```

Ou executar a JAR gerada:

```bash
java -jar target/hexagonal-0.0.1-SNAPSHOT.jar
```

---

### 3️⃣ **Verifique se o Maven está usando o JDK correto**

Execute:

```bash
mvn -v
```

Você deve ver algo como:

```text
Java version: 17.x.x
Java home: /usr/lib/jvm/java-17-openjdk-amd64
```

Se **não estiver usando o JDK 17**, faça o seguinte:

---

### 4️⃣ **Configure o Maven para usar o JDK 17**

#### 🔧 A. Instale o JDK 17 (caso não tenha):

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

#### 🔧 B. Exporte as variáveis de ambiente:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

> Você pode adicionar essas linhas ao seu `~/.bashrc` ou `~/.zshrc` para manter a configuração após reiniciar.

---

### 5️⃣ **Limpe o cache do Maven (se necessário)**

Se estiver enfrentando erros estranhos, pode ser útil limpar o repositório local de plugins Maven:

```bash
mvn clean
rm -rf ~/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin
mvn clean install
```

---

### ✅ Pronto!

Agora, o Maven deverá:

-   Compilar sua aplicação com o JDK 17
-   Gerar corretamente os mappers com o MapStruct
-   Permitir que você rode sua API REST sem erros de injeção de dependência.

Com base no seu código, **sim, tudo está estruturado corretamente para salvar no banco de dados MongoDB**. Vamos confirmar **passo a passo o fluxo de execução** e o que você pode observar para garantir que está salvando de verdade.

---

## ✅ **Checklist de Funcionamento do Fluxo**

1. **Controller** recebe os dados do `POST`:

    - Endpoint: `POST /api/v1/customers`
    - Requisição cURL está ok ✅
    - `CustomerRequestDTO` com `name`, `cpf`, `zipCode` ✅

2. **Mapper** converte `DTO → Domain (Customer)`:

    - Ignora `id`, `address` e `isValidCpf` (ok) ✅

3. **UseCase (`CreateCustomerUseCase`)**:

    - Chama `findByZipCode()` no `ViaCepAddressAdapter` ✅
    - Define o endereço no objeto `Customer`
    - Salva via `customerPersistenceOutputPort.save(customer)` ✅

4. **Mongo Adapter (`MongoCustomerRepositoryAdapter`)**:

    - Mapeia `Customer → CustomerEntity`
    - Usa Spring Data MongoDB (`MongoRepository.save(...)`) ✅

---

## 🧪 **Como Verificar que Salvou Corretamente**

### ✅ 1. **Observe os prints no console**

No seu controller e use case, você tem os prints:

```java
System.out.println("🚀 Método createCustomer foi chamado");
System.out.println("🎯 Dados recebidos do mapper: " + customer);
System.out.println("🎯 CreateCustomerUseCase1: " + customer);
```

➡ Isso garante que os dados estão fluindo.

---

### ✅ 2. **Verifique no MongoDB manualmente**

Se estiver com o Mongo rodando localmente:

```bash
# Acesse o Mongo shell:
mongo

# Use o banco correto (geralmente 'test' ou um nome definido no application.yml)
use seu_banco

# Liste os dados da coleção:
db.customerEntity.find().pretty()
```

> Obs: o nome da coleção é derivado do nome da classe `CustomerEntity` (ou anotação `@Document("...")` se você tiver usado).

---

### ✅ 3. **Validação no código: coloque um print no Adapter**

Você pode **adicionar um log extra** no `MongoCustomerRepositoryAdapter` para confirmar a gravação:

```java
@Override
public void save(Customer customer) {
    CustomerEntity entity = mapper.toCustomerEntity(customer);
    System.out.println("📦 Salvando no Mongo: " + entity);
    repository.save(entity);
}
```

---

## 🚨 Possíveis Erros se NÃO estiver salvando

1. ⚠ **Mapper não mapeando dados corretamente?**

    - Verifique se `CustomerEntityMapper` está funcionando corretamente.
    - Veja se os campos obrigatórios da `CustomerEntity` estão sendo preenchidos.

2. ⚠ **Mongo não está conectado corretamente?**

    - Veja o `application.properties` ou `application.yml`.
    - Exemplo de config mínima:

        ```properties
        spring.data.mongodb.database=seu_banco
        spring.data.mongodb.uri=mongodb://localhost:27017
        ```

3. ⚠ **ViaCep não retorna endereço?**

    - Verifique se o `ViaCepAddressAdapter` está mesmo preenchendo `Address`.

---

## ✅ Dica Final: Criar um Endpoint GET para verificar os dados

Crie algo simples para listar os clientes:

```java
@GetMapping
public List<CustomerEntity> listCustomers() {
    return repository.findAll(); // ou usar uma porta e mapper se quiser manter arquitetura 100%
}
```

---
