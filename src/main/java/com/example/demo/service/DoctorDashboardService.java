package com.example.demo.service;

import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.repository.DiagnosisRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.repository.QueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DoctorDashboardService {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final QueueRepository queueRepository;

    public DoctorDashboardService(
            PatientService patientService,
            PatientRepository patientRepository,
            DiagnosisRepository diagnosisRepository,
            PrescriptionRepository prescriptionRepository,
            QueueRepository queueRepository
    ) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.queueRepository = queueRepository;
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> response = new HashMap<>();

        long totalPatients = patientRepository.count();
        long waitingQueue = queueRepository.countByStatusIgnoreCase("WAITING");
        long inProgressQueue = queueRepository.countByStatusIgnoreCase("IN_PROGRESS");
        long activePrescriptions = prescriptionRepository.findByStatusIgnoreCaseOrderByPrescribedDateDesc("ACTIVE").size();
        long openDiagnoses = diagnosisRepository.findByStatusIgnoreCaseOrderByDiagnosisDateDesc("OPEN").size();

        List<PatientResponseDto> recentPatients = patientService.getAllPatients(null, null, 1, 5);

        Map<String, Object> snapshot = Map.of(
                "today", LocalDate.now().toString(),
                "queueWaiting", waitingQueue,
                "queueInProgress", inProgressQueue,
                "activePrescriptions", activePrescriptions,
                "openDiagnoses", openDiagnoses
        );

        response.put("totalPatients", totalPatients);
        response.put("recentPatients", recentPatients);
        response.put("healthSnapshot", snapshot);
        response.put("activity", List.of(
                "Reviewed patient charts",
                "Updated diagnosis status",
                "Issued prescriptions"
        ));

        return response;
    }
}
