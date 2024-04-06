package com.amanefer.crud.repositories;

import com.amanefer.crud.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByTitle(String name);

    List<Product> findAllByDeletedAtIsNull();

    Optional<Product> findByArticleAndDeletedAtIsNull(String article);

    Optional<Product> findByTitleAndDeletedAtIsNull(String title);
}
