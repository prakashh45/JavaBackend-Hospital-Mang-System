package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPhone(String phone);

    @Query("""
            SELECT p FROM Patient p
            WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(COALESCE(p.phone, '')) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY p.updatedAt DESC
            """)
    List<Patient> search(@Param("query") String query);
}
