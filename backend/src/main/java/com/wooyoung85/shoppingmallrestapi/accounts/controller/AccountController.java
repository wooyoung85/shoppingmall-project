package com.wooyoung85.shoppingmallrestapi.accounts.controller;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.accounts.dto.AccountDto;
import com.wooyoung85.shoppingmallrestapi.accounts.service.AccountService;
import com.wooyoung85.shoppingmallrestapi.common.BaseController;
import com.wooyoung85.shoppingmallrestapi.products.controller.ProductController;
import com.wooyoung85.shoppingmallrestapi.products.controller.ProductResource;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/accounts", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class AccountController extends BaseController {

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid AccountDto accountDto,
                                     Errors errors){
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Account account = modelMapper.map(accountDto, Account.class);

        Long createdAccountId = accountService.createAccount(account).getId();

        ControllerLinkBuilder selfLinkBuilder = linkTo(AccountController.class).slash(createdAccountId);
        URI createUri = selfLinkBuilder.toUri();
        AccountResource accountResource = new AccountResource(account);
        return ResponseEntity.created(createUri).body(accountResource);
    }
}
