package com.wooyoung85.shoppingmallrestapi.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String email;

    private String password;

    private String name;

    private List<String> roles;
}
