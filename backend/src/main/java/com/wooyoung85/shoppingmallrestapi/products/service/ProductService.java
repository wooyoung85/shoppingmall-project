package com.wooyoung85.shoppingmallrestapi.products.service;

import com.wooyoung85.shoppingmallrestapi.products.repository.ProductRepository;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(Product product){
        productRepository.save(product);
        return product.getId();
    }
}
