package com.example.app.service;

import com.example.app.dto.ProductRequest;
import com.example.app.dto.ProductResponse;
import com.example.app.exception.ProductNotFoundException;
import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repo;
    private ProductService service;

    @BeforeEach
    void setup() {
        repo = mock(ProductRepository.class);
        service = new ProductService(repo);
    }

    @Test
    void createProduct_success(){
        ProductRequest req = new ProductRequest("Monitor 27", BigDecimal.valueOf(150));
        Product saved = Product.builder().id(UUID.randomUUID())
                .name(req.name())
                .price(req.price())
                .build();

                when(repo.existsByName(req.name())).thenReturn(false);
                when(repo.save(any(Product.class))).thenReturn(saved);

                ProductResponse res = service.create(req);

                assertEquals(req.name(), res.name());
                assertEquals(req.price(), res.price());
                verify(repo, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_nameDuplicate_throwsException() {
        ProductRequest req = new ProductRequest("Monitor 27", BigDecimal.valueOf(150));
        when(repo.existsByName(req.name())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.create(req);
        });

       assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void getById_productExists() {
        UUID id = UUID.randomUUID();
        Product p = Product.builder()
                .id(id)
                .name("Monitor 27")
                .price(BigDecimal.valueOf(150))
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(p));

        ProductResponse res = service.get(id);

        assertEquals(p.getId(), res.id());
        assertEquals(p.getName(), res.name());
    }

    @Test
    void getById_productNotFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.get(id));
    }

    @Test
    void updateProduct_success() {
        UUID id= UUID.randomUUID();
        Product existing = Product.builder()
                .id(id)
                .name("Old")
                .price(BigDecimal.valueOf(100))
                .build();
        ProductRequest req = new ProductRequest("New", BigDecimal.valueOf(200));

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductResponse res = service.updateProduct(id, req);

        assertEquals("New", res.name());
        assertEquals(BigDecimal.valueOf(200), res.price());
    }

    @Test
    void deleteProduct_success(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                        .id(id)
                                .name("Tecrado Gamer")
                                        .price(BigDecimal.valueOf(200))
                                                .build();

        when(repo.findById(id)).thenReturn(Optional.of(product));

        service.deleteProduct(id);

        verify(repo, times(1)).delete(product);
    }

    @Test
    void deleteProduct_notFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.deleteProduct(id));

        verify(repo, never()).delete(any());
    }
}
