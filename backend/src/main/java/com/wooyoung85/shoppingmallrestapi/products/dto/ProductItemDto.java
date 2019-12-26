package com.wooyoung85.shoppingmallrestapi.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto {
    private String color;
    private String colorCode;
    private String imageCode;
    @Min(0)
    private Integer quantity;
}
