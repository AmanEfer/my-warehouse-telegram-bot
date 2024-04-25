package com.amanefer.crud.repositories;

import com.amanefer.crud.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByTitle(String name);

    List<Product> findAllByDeletedAtIsNull();

    @Query("from Product p where p.deletedAt is null")
    Page<Product> findAllByDeletedAtIsNullReturnsPageOfProducts(PageRequest pageRequest);

    @Query("from Product p where p.title ilike '%:title%' and p.deletedAt is null")
    Page<Product> findAllByByTitleAndDeletedAtIsNullReturnsPageOfProducts(String title, PageRequest pageRequest);

    Optional<Product> findByArticleAndDeletedAtIsNull(String article);

    Optional<Product> findByTitleAndDeletedAtIsNull(String title);
}
