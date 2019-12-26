package com.wooyoung85.shoppingmallrestapi.accounts.controller;

import com.wooyoung85.shoppingmallrestapi.accounts.dto.AccountDto;
import com.wooyoung85.shoppingmallrestapi.common.BaseController;
import com.wooyoung85.shoppingmallrestapi.common.BaseControllerTest;
import com.wooyoung85.shoppingmallrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Test
    @Rollback(false)
    @TestDescription("정상으로 계정을 생성하는 테스트")
    public void createAccount() throws Exception {

        AccountDto accountDto = AccountDto.builder()
            .email("test@test.com")
            .name("테스트")
            .password("1234")
            .roles(new ArrayList<>(Arrays.asList("USER", "ADMIN")))
            .build();

        mockMvc.perform(post("/api/accounts/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(accountDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
        ;
    }
}