package io.codepool.springchallenge.dao.repository;

import io.codepool.springchallenge.dao.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByName(String name);

    List<ProductEntity> findByActive(Boolean active);

    ProductEntity findByIdAndActive(Long id, Boolean active);
}
