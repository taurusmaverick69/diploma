package com.maverick.repository;

import com.maverick.domain.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmartphoneRepository extends JpaRepository<Smartphone, Integer> {


    List<Smartphone> findByIsTrackedTrue();

}
