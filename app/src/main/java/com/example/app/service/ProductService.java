package com.example.app.service;

import com.example.app.dto.ProductRequest;
import com.example.app.dto.ProductResponse;
import com.example.app.exception.ProductNotFoundException;
import com.example.app.model.Product;
import com.example.app.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repo;
    public ProductService(ProductRepository repo) {this.repo = repo;}

    public ProductResponse create(ProductRequest req) {
        Product p = Product.builder()
                .name(req.name())
                .price(req.price())
                .build();
        p = repo.save(p);
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCreatedAt());
    }

    public Page<ProductResponse> list(Pageable pageable){
        return repo.findAll(pageable)
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCreatedAt()));
    }

    public ProductResponse get(UUID id) {
        var p = repo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found."));
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getCreatedAt());
    }

    public ProductResponse updateProduct(UUID id, ProductRequest request){

        //Buscar produto pelo id
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        //Atualizar campos (name and price)
        existing.setName(request.name());
        existing.setPrice(request.price());

        //Salvar no banco
        Product updated = repo.save(existing);

        //Retornar DTO
        return new ProductResponse(
                updated.getId(),
                updated.getName(),
                updated.getPrice(),
                updated.getCreatedAt()
        );
    }

    @Transactional
    public void deleteProduct(UUID id){
        Product product = repo.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        repo.delete(product);
    }
}
