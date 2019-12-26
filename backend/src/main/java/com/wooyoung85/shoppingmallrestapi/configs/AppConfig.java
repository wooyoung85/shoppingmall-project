package com.wooyoung85.shoppingmallrestapi.configs;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.accounts.domain.AccountRole;
import com.wooyoung85.shoppingmallrestapi.accounts.repository.AccountRepository;
import com.wooyoung85.shoppingmallrestapi.accounts.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Optional<Account> existedAdmin = accountService.findOneByEmail(appProperties.getAdminEmail());
                Optional<Account> existedUser = accountService.findOneByEmail(appProperties.getUserEmail());

                if (existedAdmin.isEmpty()) {
                    Account admin = Account.builder()
                        .email(appProperties.getAdminEmail())
                        .name(appProperties.getAdminName())
                        .password(appProperties.getAdminPassword())
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                    accountService.createAccount(admin);
                }

                if (existedUser.isEmpty()) {
                    Account user = Account.builder()
                        .email(appProperties.getUserEmail())
                        .name(appProperties.getUserName())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                    accountService.createAccount(user);
                }
            }
        };
    }
}
