package com.wooyoung85.shoppingmallrestapi.accounts.controller;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.products.controller.ProductController;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class AccountResource extends Resource<Account> {

    public AccountResource(Account account, Link... links) {
        super(account, links);
        add(linkTo(AccountController.class).slash(account.getId()).withSelfRel());
    }
}
