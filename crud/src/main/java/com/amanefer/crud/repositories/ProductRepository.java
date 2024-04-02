package com.amanefer.crud.repositories;

import com.amanefer.crud.models.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Override
    @NonNull
    Optional<Product> findById(@NonNull String s);

    Optional<Product> findByTitle(String name);
}
