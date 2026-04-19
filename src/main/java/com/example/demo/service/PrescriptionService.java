package com.example.demo.service;

import com.example.demo.dto.prescription.MedicineRequestDto;
import com.example.demo.dto.prescription.MedicineResponseDto;
import com.example.demo.dto.prescription.PrescriptionRequestDto;
import com.example.demo.dto.prescription.PrescriptionResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PatientService patientService) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientService = patientService;
    }

    public PrescriptionResponseDto createPrescription(PrescriptionRequestDto requestDto) {
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .prescribedDate(requestDto.getPrescribedDate())
                .instructions(requestDto.getInstructions().trim())
                .doctorName(requestDto.getDoctorName().trim())
                .medicines(new ArrayList<>())
                .build();

        applyMedicines(prescription, requestDto.getMedicines());
        return toResponse(prescriptionRepository.save(prescription));
    }

    @Transactional(readOnly = true)
    public PrescriptionResponseDto getPrescriptionById(Long prescriptionId) {
        return toResponse(fetchPrescriptionOrThrow(prescriptionId));
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPrescriptionsByPatient(Long patientId) {
        patientService.fetchPatientOrThrow(patientId);
        return prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId)
                .stream()
                .map(PrescriptionService::toResponse)
                .toList();
    }

    public PrescriptionResponseDto updatePrescription(Long prescriptionId, PrescriptionRequestDto requestDto) {
        Prescription prescription = fetchPrescriptionOrThrow(prescriptionId);
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        prescription.setPatient(patient);
        prescription.setPrescribedDate(requestDto.getPrescribedDate());
        prescription.setInstructions(requestDto.getInstructions().trim());
        prescription.setDoctorName(requestDto.getDoctorName().trim());

        prescription.getMedicines().clear();
        applyMedicines(prescription, requestDto.getMedicines());

        return toResponse(prescriptionRepository.save(prescription));
    }

    public void deletePrescription(Long prescriptionId) {
        Prescription prescription = fetchPrescriptionOrThrow(prescriptionId);
        prescriptionRepository.delete(prescription);
    }

    private Prescription fetchPrescriptionOrThrow(Long prescriptionId) {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));
    }

    private void applyMedicines(Prescription prescription, List<MedicineRequestDto> medicineDtos) {
        List<Medicine> medicines = medicineDtos.stream()
                .map(dto -> Medicine.builder()
                        .prescription(prescription)
                        .medicineName(dto.getMedicineName().trim())
                        .dosage(dto.getDosage().trim())
                        .frequency(dto.getFrequency().trim())
                        .durationDays(dto.getDurationDays())
                        .build())
                .toList();
        prescription.getMedicines().addAll(medicines);
    }

    static PrescriptionResponseDto toResponse(Prescription prescription) {
        return PrescriptionResponseDto.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .patientName(prescription.getPatient().getFirstName() + " " + prescription.getPatient().getLastName())
                .prescribedDate(prescription.getPrescribedDate())
                .instructions(prescription.getInstructions())
                .doctorName(prescription.getDoctorName())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .medicines(prescription.getMedicines().stream()
                        .map(medicine -> MedicineResponseDto.builder()
                                .id(medicine.getId())
                                .medicineName(medicine.getMedicineName())
                                .dosage(medicine.getDosage())
                                .frequency(medicine.getFrequency())
                                .durationDays(medicine.getDurationDays())
                                .build())
                        .toList())
                .build();
    }
}
