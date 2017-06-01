package com.maverick.repository;

import com.maverick.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sednor-7 on 01/06/17.
 */
public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
