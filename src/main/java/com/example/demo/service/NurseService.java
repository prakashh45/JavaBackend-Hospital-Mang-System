package com.example.demo.service;

import com.example.demo.dto.nurse.NurseNoteRequest;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.model.NurseNote;
import com.example.demo.model.Patient;
import com.example.demo.repository.NurseNoteRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.QueueRepository;
import com.example.demo.repository.VitalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NurseService {

    private final NurseNoteRepository nurseNoteRepository;
    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final QueueRepository queueRepository;
    private final VitalsRepository vitalsRepository;

    public NurseService(
            NurseNoteRepository nurseNoteRepository,
            PatientService patientService,
            PatientRepository patientRepository,
            QueueRepository queueRepository,
            VitalsRepository vitalsRepository
    ) {
        this.nurseNoteRepository = nurseNoteRepository;
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.queueRepository = queueRepository;
        this.vitalsRepository = vitalsRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> dashboard() {
        long totalPatients = patientRepository.count();
        long criticalCount = patientRepository.findByPriorityOrderByUpdatedAtDesc("CRITICAL").size();
        long pendingQueue = queueRepository.countByStatusIgnoreCase("WAITING");
        long vitalsRecords = vitalsRepository.count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalPatients", totalPatients);
        response.put("criticalCount", criticalCount);
        response.put("pendingMeds", pendingQueue);
        response.put("shiftProgress", Map.of(
                "completedVitals", vitalsRecords,
                "pendingQueue", pendingQueue
        ));
        response.put("timeline", List.of(
                "Morning rounds completed",
                "Vitals updated",
                "Queue prioritized"
        ));
        return response;
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> wardPatients(String wardId, String priority) {
        return patientService.getWardPatients(wardId, priority);
    }

    public Map<String, Object> addNote(NurseNoteRequest request) {
        Patient patient = patientService.fetchPatientOrThrow(request.getPatientId());
        NurseNote note = NurseNote.builder()
                .patient(patient)
                .nurseName(request.getNurseName().trim())
                .wardId(request.getWardId())
                .note(request.getNote().trim())
                .build();
        NurseNote saved = nurseNoteRepository.save(note);
        return Map.of(
                "id", saved.getId(),
                "patientId", patient.getId(),
                "nurseName", saved.getNurseName(),
                "wardId", saved.getWardId(),
                "note", saved.getNote(),
                "createdAt", saved.getCreatedAt()
        );
    }
}
