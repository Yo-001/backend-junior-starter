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

- Forma prática de validar dados de acordo com diferentes critérios proporcionado pelo Bean Validation. O Spring gera
erros automaticamente se a validaçao falhar.

## 5. Pageable

- O Pageable evita que todos itens do banco de dados sejam carregados de uma só vez, o que tornaria o programa lento
e com alto consumo de memória. Com o Pageable os itens podem ser carregados por páginas, e definir a quantidade de itens
por página e a quantidade de páginas.

## 6. @RestControllerAdvice

- Permite criar tratadores globais de exceçao para todos os controllers REST da aplicaçao.
- Facilita o tratamento de erros ao centralizar o tramento de erros da API, sem precisar repetir try/catch
em cada controller.
- Padroniza respostas de erro.
- Captura exceçoes especificas ou gerias dentro dos controllers.

## 7 . Ciclo de vida uma requisiçao no Spring

- Recebe requisicao do cliente, por exemplo, através de um POST ou PUT, o DispatcherServlet encaminha a requisicao
para o controller correto através do handlerMapping, e caso houver, passa pelo handlerInterceptor. Ao chegar ao
controller, ele processa a requisicao, recebe e valida os dados e envia para o service executar a lógica de negocio. 
O repository acessa o banco de dados, e em seguida o service retorna um DTO ou objeto que representa a resposta.
O resultado do controller é convertido para JSON ou XML através do HttpMessageConverter, e em seguida é feito o
tratamento de erros caso tenha ocorrido algum, e se a respostar estiver ok o dispatcherServlet envia a resposta de volta
ao cliente.

## 8 . DTO vs Entity

- Entity representa a entidade do banco de dados, e reflete como os dados sao armazenados. DTO é o objeto usado para 
transferir dados entre a API e o cliente, podendo ser um Request DTO ou Response DTO.
- O uso de DTOs em alternativa a Entity se deve a diversas vantagens: Segurança - evita expor dados sensíveis;
- Flexibilidade - Permite devolver dados diferentes dos que estao no banco de dados; Evoluçao da API - Se a entidade
mudar a API nao quebra; Validacao - O DTO pode ter anotaçoes para validar os dados sem poluir a entidade.

## 9. Spring Security

- Filter Chain: Sequencia de filtros para fazer autenticacao e autorizacao de cada requisicao que chega a aplicacao.
- Autenticaçao vs Autorizaçao: Autenticaçao é identificar o usuario(login e password), já autorizaçao é verificar se o usuario tem acesso a uma determinada sessao da aplicaçao.
- Usuários em memória: Criaçao de usuários fixos na memória para tester e protótipos, sem precisar de banco de dados.