package com.amanefer.crud.repositories;

import com.amanefer.crud.models.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByTitle(String name);

    List<Product> findAllByDeletedAtIsNull();
}
