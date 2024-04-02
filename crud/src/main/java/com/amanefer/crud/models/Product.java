package com.amanefer.crud.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product", schema = "my_db_bot")
public class Product {

    @Id
    @NotBlank
    @Column(name = "id", length = 6)
    private String article;

    @Column(name = "title", length = 128, nullable = false, unique = true)
    private String title;

    @Column(name = "purchase_last_price")
    private BigDecimal purchaseLastPrice;

    @Column(name = "sale_last_price")
    private BigDecimal saleLastPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductQuantity> quantityList;

}
