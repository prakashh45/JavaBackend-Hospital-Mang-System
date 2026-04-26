package com.example.demo.service;

import com.example.demo.dto.diagnosis.DiagnosisResponseDto;
import com.example.demo.dto.patient.PatientHistoryResponseDto;
import com.example.demo.dto.patient.PatientPatchRequestDto;
import com.example.demo.dto.patient.PatientRequestDto;
import com.example.demo.dto.patient.PatientResponseDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.dto.queue.QueueResponseDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.model.Vitals;
import com.example.demo.repository.DiagnosisRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import com.example.demo.repository.QueueRepository;
import com.example.demo.repository.VitalsRepository;
import org.springframework.data.domain.PageRequest;
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
                .firstName(trim(requestDto.getFirstName()))
                .lastName(trim(requestDto.getLastName()))
                .gender(trim(requestDto.getGender()))
                .dateOfBirth(requestDto.getDateOfBirth())
                .phone(trim(requestDto.getPhone()))
                .email(trimToNull(requestDto.getEmail()))
                .address(trimToNull(requestDto.getAddress()))
                .status(normalizeDefault(requestDto.getStatus(), "ACTIVE"))
                .wardId(trimToNull(requestDto.getWardId()))
                .priority(normalizeDefault(requestDto.getPriority(), "NORMAL"))
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
    public List<PatientResponseDto> getAllPatients(String search, String status, Integer page, Integer limit) {
        int resolvedPage = page == null || page < 1 ? 1 : page;
        int resolvedLimit = limit == null || limit < 1 ? 50 : Math.min(limit, 200);

        return patientRepository.searchWithStatus(search, status, PageRequest.of(resolvedPage - 1, resolvedLimit))
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

        String updatedPhone = trim(requestDto.getPhone());
        if (!updatedPhone.equals(patient.getPhone())) {
            patientRepository.findByPhone(updatedPhone)
                    .ifPresent(existing -> {
                        throw new BadRequestException("Patient already exists with phone: " + updatedPhone);
                    });
        }

        patient.setFirstName(trim(requestDto.getFirstName()));
        patient.setLastName(trim(requestDto.getLastName()));
        patient.setGender(trim(requestDto.getGender()));
        patient.setDateOfBirth(requestDto.getDateOfBirth());
        patient.setPhone(updatedPhone);
        patient.setEmail(trimToNull(requestDto.getEmail()));
        patient.setAddress(trimToNull(requestDto.getAddress()));
        patient.setStatus(normalizeDefault(requestDto.getStatus(), patient.getStatus()));
        patient.setWardId(trimToNull(requestDto.getWardId()));
        patient.setPriority(normalizeDefault(requestDto.getPriority(), patient.getPriority()));

        return toResponse(patientRepository.save(patient));
    }

    public PatientResponseDto patchPatient(Long patientId, PatientPatchRequestDto requestDto) {
        Patient patient = fetchPatientOrThrow(patientId);

        if (requestDto.getFirstName() != null) patient.setFirstName(trim(requestDto.getFirstName()));
        if (requestDto.getLastName() != null) patient.setLastName(trim(requestDto.getLastName()));
        if (requestDto.getGender() != null) patient.setGender(trim(requestDto.getGender()));
        if (requestDto.getDateOfBirth() != null) patient.setDateOfBirth(requestDto.getDateOfBirth());
        if (requestDto.getPhone() != null) {
            String updatedPhone = trim(requestDto.getPhone());
            if (!updatedPhone.equals(patient.getPhone())) {
                patientRepository.findByPhone(updatedPhone)
                        .ifPresent(existing -> {
                            throw new BadRequestException("Patient already exists with phone: " + updatedPhone);
                        });
            }
            patient.setPhone(updatedPhone);
        }
        if (requestDto.getEmail() != null) patient.setEmail(trimToNull(requestDto.getEmail()));
        if (requestDto.getAddress() != null) patient.setAddress(trimToNull(requestDto.getAddress()));
        if (requestDto.getStatus() != null) patient.setStatus(normalizeDefault(requestDto.getStatus(), patient.getStatus()));
        if (requestDto.getWardId() != null) patient.setWardId(trimToNull(requestDto.getWardId()));
        if (requestDto.getPriority() != null) patient.setPriority(normalizeDefault(requestDto.getPriority(), patient.getPriority()));

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

    @Transactional(readOnly = true)
    public VitalsResponseDto getCurrentVitals(Long patientId) {
        fetchPatientOrThrow(patientId);
        Vitals vitals = vitalsRepository.findTopByPatientIdOrderByRecordedAtDesc(patientId);
        if (vitals == null) {
            throw new ResourceNotFoundException("No vitals found for patient id: " + patientId);
        }
        return VitalsService.toResponse(vitals);
    }

    @Transactional(readOnly = true)
    public List<DiagnosisResponseDto> getPatientDiagnoses(Long patientId) {
        fetchPatientOrThrow(patientId);
        return diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId)
                .stream().map(DiagnosisService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPatientPrescriptions(Long patientId) {
        fetchPatientOrThrow(patientId);
        return prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId)
                .stream().map(PrescriptionService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDto> getWardPatients(String wardId, String priority) {
        if (wardId != null && !wardId.isBlank() && priority != null && !priority.isBlank()) {
            return patientRepository.findByWardIdAndPriorityOrderByUpdatedAtDesc(wardId, priority.toUpperCase())
                    .stream().map(this::toResponse).toList();
        }
        if (wardId != null && !wardId.isBlank()) {
            return patientRepository.findByWardIdOrderByUpdatedAtDesc(wardId)
                    .stream().map(this::toResponse).toList();
        }
        if (priority != null && !priority.isBlank()) {
            return patientRepository.findByPriorityOrderByUpdatedAtDesc(priority.toUpperCase())
                    .stream().map(this::toResponse).toList();
        }
        return getAllPatients();
    }

    public Patient fetchPatientOrThrow(Long patientId) {
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
                .status(patient.getStatus())
                .wardId(patient.getWardId())
                .priority(patient.getPriority())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String trimToNull(String value) {
        String trimmed = trim(value);
        return trimmed == null || trimmed.isBlank() ? null : trimmed;
    }

    private String normalizeDefault(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim().toUpperCase();
    }
}
