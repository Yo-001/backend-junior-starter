package com.example.app.integration;

import com.example.app.dto.ProductRequest;
import com.example.app.dto.ProductResponse;
import com.example.app.repository.ProductRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductApiIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("appdb")
            .withUsername("appuser")
            .withPassword("secret");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;
    private ProductRepository repo;

    @Test
    void criarProduto() {
        // Criar ProductRequest
      ProductRequest request = new ProductRequest("Produto Teste", BigDecimal.valueOf(100.0));

      // Enviar POST para o endpoint /products

        ResponseEntity<ProductResponse> response = restTemplate.exchange(
                "/products",
                HttpMethod.POST,
                new HttpEntity<>(request),
                ProductResponse.class
        );

        // Verificar se retornou 201 Created e os dados corretos

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        ProductResponse product = response.getBody();
        assertThat(product).isNotNull();
        assertThat(product.id()).isNotNull();
        assertThat(product.name()).isEqualTo("Produto Teste");
        assertThat(product.price()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
        assertThat(product.createdAt()).isNotNull();
    }
    
    @Test
    void getById() {
        //1. Criar produto via POST
        ProductRequest req = new ProductRequest("Produto testContainer", BigDecimal.valueOf(500.0));

        ResponseEntity<ProductResponse> postResponse = restTemplate.exchange(
                "/products",
                HttpMethod.POST,
                new HttpEntity<>(req),
                ProductResponse.class
        );
        assertThat(postResponse.getStatusCode().is2xxSuccessful()).isTrue();
        ProductResponse created = postResponse.getBody();
        assertNotNull(created);
        assertNotNull(created.id());

        //2. Buscar produto criado via GET
        ResponseEntity<ProductResponse> getResponse = restTemplate.exchange(
                "/products/" + created.id(),
                HttpMethod.GET,
                null,
                ProductResponse.class
        );
        assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();
        ProductResponse found = getResponse.getBody();
        assertNotNull(found);
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.name()).isEqualTo("Produto testContainer");
        assertThat(found.price()).isEqualByComparingTo(BigDecimal.valueOf(500.0));
    }

    @Test
    void updateTest() {

        ProductRequest req = new ProductRequest("TrekDéqui", BigDecimal.valueOf(2.90));

        ResponseEntity<ProductResponse> postResponse = restTemplate.exchange(
                "/products/",
                HttpMethod.POST,
                new HttpEntity<>(req),
                ProductResponse.class
        );

        ProductRequest updateReq = new ProductRequest("TechDeck", BigDecimal.valueOf(10.0));

        ProductResponse created = postResponse.getBody();
        assertNotNull(created);
        UUID id = created.id();

        restTemplate.exchange(
                "/products/" + created.id(),
                HttpMethod.PUT,
                new HttpEntity<>(updateReq),
                ProductResponse.class);


    }

    @Test
    void deleteTest(){
        ProductRequest req = new ProductRequest("BreckFleck", BigDecimal.valueOf(59.90));

        ResponseEntity<ProductResponse> postRes = restTemplate.exchange(
                "/products",
                HttpMethod.POST,
                new HttpEntity<>(req),
                ProductResponse.class
        );

        assertThat(postRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ProductResponse existing = postRes.getBody();
        assertNotNull(existing);
        UUID id = existing.id();

        ResponseEntity<Void> deleteRes = restTemplate.exchange(
                "/products/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<ProductResponse> getResponse = restTemplate.exchange(
                "/products/" + existing.id(),
                HttpMethod.GET,
                null,
                ProductResponse.class
        );

        assertThat(getResponse.getStatusCode().is4xxClientError()).isTrue();

    }
}
