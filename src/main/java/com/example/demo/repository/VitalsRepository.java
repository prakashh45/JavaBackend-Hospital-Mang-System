package com.example.demo.repository;

import com.example.demo.model.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VitalsRepository extends JpaRepository<Vitals, Long> {
    List<Vitals> findByPatientIdOrderByRecordedAtDesc(Long patientId);
    Vitals findTopByPatientIdOrderByRecordedAtDesc(Long patientId);
}
