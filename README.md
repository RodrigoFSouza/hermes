# HERMES - Gerenciador financeiro pessoal
Projeto feito em Java com Spring Boot, gerenciador financeiro pessoal. Tem o propósito de efetuar o lançamento de 
receitas e despesas, gerenciar contas, categorizar as despesas e gerar dados para análise da saúde financeira


### Tecnologias:

* spring boot 3.0.5
* java 17
* spring data
* spring devtools
* bean validation
* lombok
* postgresql
* mavem

### Funcionalidades:

* gerenciar usuarios
* gerenciar lançamentos


### Inciciando a API

* No Postgresql crie um banco de dados com o nome **hermes**

* Compilando o projeto
```shell script
mvn clean package
```

* Iniciando
  1. primeiro entre na pasta target dentro do projeto: cd target
  2. execute o seguinte comando: 
```shell script
java -jar hermes-0.0.1-SNAPSHOT.jar
```
* Acesse a seguinte url : http://localhost:8097
* Para acessar o swagger: http://localhost:8097/swagger-ui.html