package com.klug.domain.repositories;

import com.klug.domain.models.DomainOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<DomainOrder, Long> {
}
