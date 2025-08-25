package com.example.app.controller;


import com.example.app.dto.ProductRequest;
import com.example.app.dto.ProductResponse;
import com.example.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) {this.service = service;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse created(@RequestBody @Valid ProductRequest req) {
        return service.create(req);
    }

    @GetMapping
    public Page<ProductResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id){
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request){
        return service.updateProduct(id, request);
    }

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        service.deleteProduct(id);
}

}
