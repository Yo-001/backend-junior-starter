# Estudo Backend Java/Spring

## 1. JPA / Hibernate

- **JPA (Java Persistence API):** Especificacao que define como trabalhar com persistência de dados em Java.
- **Hibernate:** Transforma objetos Java em registro no banco de dados, e vice-versa.
- **Entidades:** Classes Java anotadas com "@Entity" que representa tabelas do banco.
- **Repository:** Interface que estende "JpaRepository" para CRUD automático.
- **Service:** Camada opcional de lógica de negócio.
- **Controller:** Camada que expoes endpoints REST

## 2. Persistence Context

- Área de memória gerenciada pelo Hibernate/JPA.
Objetos criados/alterados seram enviados para cá, onde serao
monitorados pelo Hibernate, para ao final da transacao o JPA
comparar esses objetos com os já presente no banco e gera
INSERTs, UPDATES ou DELETES, conforme o necessário.

## 3. Diferença entre .save() e managed entity

- O .save() vai enviar os objetos para o banco de dados.
Em caso de o ID nao existir, o "persist()" será chamado e 
enviará o objeto para o Persistence Context e gera um INSERT.
Em caso de o ID existir, o "merge()" será chamado que
através do UPDATE sincronizará as alteraçoes com o banco.

O managed entity se refere ao objeto que está no Persistence
Context sendo gerenciado pelo Hibernate/JPA.

## 4. Validaçao com anotaçoes do Bean Validation

- Forma prática de validar dados de acordo com diferentes
critérios proporcionado pelo Bean Validation. O Spring gera
erros automaticamente se a validaçao falhar.

## 5. Pageable

- O Pageable evita que todos itens do banco de dados sejam
carregados de uma só vez, o que tornaria o programa lento
e com alto consumo de memória. Com o Pageable os itens podem
ser carregados por páginas, e definir a quantidade de itens
por página e a quantidade de páginas.


