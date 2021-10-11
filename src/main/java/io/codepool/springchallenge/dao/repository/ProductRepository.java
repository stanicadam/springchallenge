package io.codepool.springchallenge.dao.repository;

import io.codepool.springchallenge.dao.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByName(String name);
}
