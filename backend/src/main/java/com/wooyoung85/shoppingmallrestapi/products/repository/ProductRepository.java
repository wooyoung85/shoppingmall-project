package com.wooyoung85.shoppingmallrestapi.products.repository;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public Product findOne(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAll() {
        return em.createQuery("select m from Product m", Product.class)
            .getResultList();
    }

    public Page<Product> findAll(Pageable pageable) {
        List<Product> productList = em.createQuery("select m from Product m", Product.class)
            .getResultList();

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > productList.size() ? productList.size()
            : (start + pageable.getPageSize()));

        return new PageImpl<Product>(productList.subList(start, end), pageable, productList.size());
    }

    public List<Product> findByName(String name) {
        return em.createQuery("select m from Product m where m.name = :name", Product.class)
            .setParameter("name", name)
            .getResultList();
    }

    public Optional<Product> findById(Integer id) {
        Product product = em.find(Product.class, id);
        return Optional.ofNullable(product);
    }
}
