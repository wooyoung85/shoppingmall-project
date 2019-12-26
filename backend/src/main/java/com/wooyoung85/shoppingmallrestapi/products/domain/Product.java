package com.wooyoung85.shoppingmallrestapi.products.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String brand;
    private String name;
    private String description;
    private String category;

    @Enumerated(EnumType.STRING)
    private ProductSupportType productSupportType = ProductSupportType.NONE;; // WELFARE, CASHBAG, NONE

    @NotNull
    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductItem> productItems = new ArrayList<>();
}
