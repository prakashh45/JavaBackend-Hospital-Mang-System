package com.example.demo.repository;

import com.example.demo.model.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, Long> {
}
