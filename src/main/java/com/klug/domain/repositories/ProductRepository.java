package com.klug.domain.repositories;

import com.klug.domain.models.DomainProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<DomainProduct, Long> {
}
