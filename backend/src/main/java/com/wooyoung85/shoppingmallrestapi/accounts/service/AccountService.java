package com.wooyoung85.shoppingmallrestapi.accounts.service;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.accounts.domain.AccountRole;
import com.wooyoung85.shoppingmallrestapi.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public Account createAccount(Account account){
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return accountRepository.findOne(account.getId());
    }

    public Optional<Account> findOneByEmail(String email){
        return accountRepository.findOneByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findOneByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        //return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
        return new AccountAdapter(account);
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE" + r.name()))
            .collect(Collectors.toSet());
    }
}
