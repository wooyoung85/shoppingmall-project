package com.wooyoung85.shoppingmallrestapi.products.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.wooyoung85.shoppingmallrestapi.products.domain.Product;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ProductResource extends Resource<Product> {

    public ProductResource(Product product, Link... links) {
        super(product, links);
        add(linkTo(ProductController.class).slash(product.getId()).withSelfRel());
    }
}
