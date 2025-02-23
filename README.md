# API de Parquímetro

## Descrição

Esta API RESTful foi desenvolvida em Java com Spring Boot e permite o gerenciamento de um sistema de parquímetro.

## Funcionalidades

* Registrar a entrada e saída de veículos.
* Calcular o valor devido pelo tempo de estacionamento.
* Consultar o valor devido por placa.
* Gerenciar os registros de estacionamento.

## Tecnologias

* Java
* Spring Boot
* Spring Data JPA
* H2 Database
* Swagger

## Como executar

1. Clone o repositório: `git clone `
2. Navegue até o diretório do projeto: `cd challange-two`
3. Execute o projeto: `./mvnw spring-boot:run`

## Swagger

A documentação da API está disponível através do Swagger UI:

* Acesse a URL: `http://localhost:8080/swagger-ui.html`

## Endpoints

| Método | URL                                      | Descrição                                   |
| ------ | ----------------------------------------- | --------------------------------------------- |
| POST   | /parquimetro/iniciar/{placa}             | Inicia o estacionamento de um veículo.        |
| PUT    | /parquimetro/encerrar/{placa}            | Encerra o estacionamento de um veículo.       |
| GET    | /parquimetro/valor-devido/{placa}       | Consulta o valor devido de um veículo.      |

## Autor

* [Lucas Caldeira]

## Licença

[MIT](https://choosealicense.com/licenses/mit/)