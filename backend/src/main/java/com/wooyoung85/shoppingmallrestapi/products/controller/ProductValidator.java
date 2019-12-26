package com.wooyoung85.shoppingmallrestapi.products.controller;

import com.wooyoung85.shoppingmallrestapi.products.dto.ProductDto;
import com.wooyoung85.shoppingmallrestapi.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public void validate(ProductDto productDto, Errors errors) {
        String productName = productDto.getName();

        if (productRepository.findByName(productName).size() > 0) {
            errors.rejectValue("name", "wrongValue", "name is Wrong!!");
        }

        if(productDto.getProductItems().size() <= 0){
            errors.rejectValue("productItem", "wrongValue", "productItem is Wrong!!");
        }
    }
}
