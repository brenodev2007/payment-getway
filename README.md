# Payment Gateway

<p align="center">
  <strong>API REST para processamento e gerenciamento de transações financeiras</strong><br/>
  Desenvolvido com Java e Spring Boot, com foco em regras de negócio, consistência transacional e arquitetura backend.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Database-MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white" />
</p>

---

## Overview

O **Payment Gateway** é uma API backend que simula componentes essenciais de um sistema de pagamentos, incluindo gerenciamento de contas, controle de limites financeiros e processamento de transações.

O projeto explora conceitos fundamentais da engenharia de software aplicados ao domínio financeiro, como consistência de dados, controle de concorrência, idempotência e gerenciamento de estados transacionais.

A arquitetura busca manter as responsabilidades bem definidas, separando a camada de exposição HTTP das regras de negócio e da persistência de dados.

> **Status:** Em desenvolvimento. Projeto educacional e de portfólio, não destinado ao processamento de pagamentos reais.

## Features

* Gerenciamento e consulta de contas.
* Consulta de saldo e limites financeiros.
* Registro e processamento de transações.
* Controle do ciclo de vida das transações.
* Gerenciamento de estados como `PENDING`, `PROCESSING` e estados finais.
* Estorno de transações com validação das regras de negócio.
* Validação de limites por transação e limites diários.
* Persistência relacional utilizando Spring Data JPA.
* Controle de operações por meio de transações de banco de dados.
* Estratégias de concorrência para operações financeiras.
* Evolução de mecanismos de idempotência para prevenção de operações duplicadas.

## Tech Stack

| Tecnologia              | Aplicação                             |
| ----------------------- | ------------------------------------- |
| Java 17+                | Linguagem principal                   |
| Spring Boot             | Framework da aplicação                |
| Spring Web MVC          | Construção da API REST                |
| Spring Data JPA         | Persistência de dados                 |
| Hibernate               | Mapeamento objeto-relacional          |
| MySQL                   | Banco de dados relacional             |
| Jakarta Bean Validation | Validação de entradas                 |
| Maven                   | Gerenciamento de dependências e build |
| Lombok                  | Redução de código boilerplate         |

## Architecture

A aplicação segue uma arquitetura em camadas, com separação de responsabilidades entre os componentes:

```text
Client
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
Database
```

### Responsabilidades

**Controller**

* Exposição dos endpoints HTTP.
* Validação inicial das requisições.
* Conversão de parâmetros e respostas.
* Delegação das operações para a camada de serviço.

**Service**

* Centralização das regras de negócio.
* Validação de limites financeiros.
* Gerenciamento do ciclo de vida das transações.
* Controle de operações de processamento e estorno.
* Coordenação de operações atômicas.

**Repository**

* Abstração do acesso ao banco de dados.
* Consultas utilizando Spring Data JPA.
* Persistência e recuperação de entidades.
* Suporte a operações com controle de concorrência.

**Entity**

* Representação do domínio financeiro.
* Mapeamento das estruturas relacionais.
* Definição dos relacionamentos entre contas e transações.

## Engineering Principles

O projeto adota princípios importantes para sistemas que manipulam informações financeiras.

### Transactional Consistency

Operações que envolvem alteração de saldo e atualização do estado de uma transação devem preservar a atomicidade dos dados.

O uso de `@Transactional` permite agrupar operações relacionadas em uma única unidade de trabalho, evitando que atualizações parciais sejam confirmadas em caso de falha.

### Concurrency Control

Operações simultâneas sobre uma mesma conta exigem mecanismos explícitos de sincronização.

O projeto explora estratégias de bloqueio de registros para reduzir condições de corrida e preservar a integridade dos saldos.

### Idempotency

Requisições repetidas não devem produzir efeitos financeiros duplicados.

A implementação de mecanismos de idempotência busca garantir que uma mesma operação lógica seja reconhecida mesmo quando submetida mais de uma vez.

### Monetary Precision

Valores monetários são representados utilizando `BigDecimal`, evitando os problemas de precisão associados a tipos de ponto flutuante.

### State Management

As transações possuem estados definidos, e suas mudanças devem respeitar as regras de transição estabelecidas pelo domínio.

Isso permite controlar o processamento, impedir operações inválidas e manter a rastreabilidade do ciclo de vida financeiro.

## Getting Started

### Prerequisites

* JDK compatível com a versão configurada no projeto.
* Maven.
* MySQL.

### Clone

```bash
git clone https://github.com/brenodev2007/payment-getway.git

cd payment-getway
```

### Database Configuration

Crie o banco de dados:

```sql
CREATE DATABASE payment_gateway;
```

Configure a conexão no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_gateway
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

Defina as credenciais no ambiente antes de executar a aplicação. Não versione senhas ou segredos no repositório.

### Run Application

```bash
mvn spring-boot:run
```

A aplicação estará disponível, por padrão, em:

```text
http://localhost:8080
```

### Build

```bash
mvn clean package
```

### Run Tests

```bash
mvn test
```

## Security Considerations

Este projeto possui finalidade educacional. Uma implementação destinada a ambientes reais exigiria avaliações adicionais de segurança, autenticação, autorização, auditoria, observabilidade, gestão de segredos e conformidade com os requisitos aplicáveis ao setor financeiro.

## Author

**Breno Soriani**

[GitHub](https://github.com/brenodev2007)

---

<p align="center">
  Desenvolvido para estudo de engenharia backend, APIs REST e consistência em sistemas financeiros.
</p>
