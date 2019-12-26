package com.wooyoung85.shoppingmallrestapi.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotEmpty
    private String brand;
    @NotEmpty
    private String name;
    private String description;
    @NotNull
    private String category;
    private String productSupportType;
    @NotNull
    private List<ProductItemDto> productItems;
    private LocalDateTime createDateTime;
}


