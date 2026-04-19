package com.example.demo.service;

import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.dto.patient.PatientHistoryResponseDto;
import com.example.demo.dto.patient.PatientRequestDto;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.dto.queue.QueueResponseDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.repository.DiagnosisRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.repository.QueueRepository;
import com.example.demo.repository.VitalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final VitalsRepository vitalsRepository;
    private final QueueRepository queueRepository;

    public PatientService(
            PatientRepository patientRepository,
            DiagnosisRepository diagnosisRepository,
            PrescriptionRepository prescriptionRepository,
            VitalsRepository vitalsRepository,
            QueueRepository queueRepository
    ) {
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.vitalsRepository = vitalsRepository;
        this.queueRepository = queueRepository;
    }

    public PatientResponseDto createPatient(PatientRequestDto requestDto) {
        patientRepository.findByPhone(requestDto.getPhone())
                .ifPresent(existing -> {
                    throw new BadRequestException("Patient already exists with phone: " + requestDto.getPhone());
                });

        Patient patient = Patient.builder()
                .firstName(requestDto.getFirstName().trim())
                .lastName(requestDto.getLastName().trim())
                .gender(requestDto.getGender().trim())
                .dateOfBirth(requestDto.getDateOfBirth())
                .phone(requestDto.getPhone().trim())
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .build();

        return toResponse(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(Long patientId) {
        return toResponse(fetchPatientOrThrow(patientId));
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> searchPatients(String query) {
        String sanitizedQuery = query == null ? "" : query.trim();
        if (sanitizedQuery.isEmpty()) {
            return getAllPatients();
        }
        return patientRepository.search(sanitizedQuery)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PatientResponseDto updatePatient(Long patientId, PatientRequestDto requestDto) {
        Patient patient = fetchPatientOrThrow(patientId);

        String updatedPhone = requestDto.getPhone().trim();
        if (!updatedPhone.equals(patient.getPhone())) {
            patientRepository.findByPhone(updatedPhone)
                    .ifPresent(existing -> {
                        throw new BadRequestException("Patient already exists with phone: " + updatedPhone);
                    });
        }

        patient.setFirstName(requestDto.getFirstName().trim());
        patient.setLastName(requestDto.getLastName().trim());
        patient.setGender(requestDto.getGender().trim());
        patient.setDateOfBirth(requestDto.getDateOfBirth());
        patient.setPhone(updatedPhone);
        patient.setEmail(requestDto.getEmail());
        patient.setAddress(requestDto.getAddress());

        return toResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long patientId) {
        Patient patient = fetchPatientOrThrow(patientId);
        patientRepository.delete(patient);
    }

    @Transactional(readOnly = true)
    public PatientHistoryResponseDto getPatientHistory(Long patientId) {
        Patient patient = fetchPatientOrThrow(patientId);

        List<DiagnosisResponseDto> diagnoses = diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId)
                .stream()
                .map(DiagnosisService::toResponse)
                .toList();
        List<PrescriptionResponseDto> prescriptions = prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId)
                .stream()
                .map(PrescriptionService::toResponse)
                .toList();
        List<VitalsResponseDto> vitals = vitalsRepository.findByPatientIdOrderByRecordedAtDesc(patientId)
                .stream()
                .map(VitalsService::toResponse)
                .toList();
        List<QueueResponseDto> queueHistory = queueRepository.findByPatientIdOrderByQueuedAtDesc(patientId)
                .stream()
                .map(QueueService::toResponse)
                .toList();

        return PatientHistoryResponseDto.builder()
                .patient(toResponse(patient))
                .diagnoses(diagnoses)
                .prescriptions(prescriptions)
                .vitals(vitals)
                .queueHistory(queueHistory)
                .build();
    }

    Patient fetchPatientOrThrow(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    }

    private PatientResponseDto toResponse(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}
