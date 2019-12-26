package com.wooyoung85.shoppingmallrestapi.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

public class BaseController {

    protected ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
