package com.example.app.integration;

import com.example.app.dto.ProductRequest;
import com.example.app.dto.ProductResponse;
import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    
    
    public ProductResponse getById(UUID id) {
        var p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCreatedAt());
    }
}
