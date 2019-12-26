package com.wooyoung85.shoppingmallrestapi.accounts.repository;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;

    public void save(Account account) {
        em.persist(account);
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    public List<Account> findAll() {
        return em.createQuery("select a from Account a", Account.class)
            .getResultList();
    }

    public List<Account> findByName(String name) {
        return em.createQuery("select a from Account a where a.name = :name", Account.class)
            .setParameter("name", name)
            .getResultList();
    }

    public Optional<Account> findOneByEmail(String email) {
        List<Account> accountList = em.createQuery("select a from Account a where a.email = :email", Account.class)
            .setParameter("email", email)
            .getResultList();

        if(accountList.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(accountList.get(0));
    }
}
