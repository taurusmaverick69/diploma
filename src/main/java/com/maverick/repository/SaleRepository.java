package com.maverick.repository;

import com.maverick.domain.Sale;
import com.maverick.domain.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by sednor-7 on 01/06/17.
 */
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findBySmartphone(Smartphone smartphone);
}
