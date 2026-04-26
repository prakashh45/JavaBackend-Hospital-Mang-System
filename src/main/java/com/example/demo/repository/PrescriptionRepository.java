package com.example.demo.repository;

import com.example.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(Long patientId);
    List<Prescription> findByPatientIdAndStatusIgnoreCaseOrderByPrescribedDateDesc(Long patientId, String status);
    List<Prescription> findByStatusIgnoreCaseOrderByPrescribedDateDesc(String status);
    List<Prescription> findAllByOrderByPrescribedDateDesc();
}
