package com.app.backend.services;

import com.app.backend.models.Product;
import com.app.backend.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Optional<Product> findById(String id) {
        return repo.findById(id);
    }

    public Product create(Product p) {
        p.setId(null);
        return repo.save(p);
    }

    public Optional<Product> update(String id, Product p) {
        return repo.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setDescription(p.getDescription());
            existing.setPrice(p.getPrice());
            return repo.save(existing);
        });
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
