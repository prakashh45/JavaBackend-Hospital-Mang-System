package com.example.demo.repository;

import com.example.demo.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByPatientIdOrderByDiagnosisDateDesc(Long patientId);
    List<Diagnosis> findByPatientIdAndPriorityIgnoreCaseAndStatusIgnoreCaseOrderByDiagnosisDateDesc(Long patientId, String priority, String status);
    List<Diagnosis> findByPatientIdAndPriorityIgnoreCaseOrderByDiagnosisDateDesc(Long patientId, String priority);
    List<Diagnosis> findByPatientIdAndStatusIgnoreCaseOrderByDiagnosisDateDesc(Long patientId, String status);
    List<Diagnosis> findByPriorityIgnoreCaseAndStatusIgnoreCaseOrderByDiagnosisDateDesc(String priority, String status);
    List<Diagnosis> findByPriorityIgnoreCaseOrderByDiagnosisDateDesc(String priority);
    List<Diagnosis> findByStatusIgnoreCaseOrderByDiagnosisDateDesc(String status);
    List<Diagnosis> findAllByOrderByDiagnosisDateDesc();
}
