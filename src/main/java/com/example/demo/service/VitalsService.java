package com.example.demo.service;

import com.example.demo.dto.vitals.VitalsRequestDto;
import com.example.demo.dto.vitals.VitalsResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.model.Vitals;
import com.example.demo.repository.VitalsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VitalsService {

    private final VitalsRepository vitalsRepository;
    private final PatientService patientService;

    public VitalsService(VitalsRepository vitalsRepository, PatientService patientService) {
        this.vitalsRepository = vitalsRepository;
        this.patientService = patientService;
    }

    public VitalsResponseDto createVitals(VitalsRequestDto requestDto) {
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        Vitals vitals = Vitals.builder()
                .patient(patient)
                .recordedAt(requestDto.getRecordedAt())
                .temperature(requestDto.getTemperature())
                .bloodPressure(requestDto.getBloodPressure().trim())
                .pulse(requestDto.getPulse())
                .respiratoryRate(requestDto.getRespiratoryRate())
                .oxygenSaturation(requestDto.getOxygenSaturation())
                .nurseName(requestDto.getNurseName().trim())
                .build();

        return toResponse(vitalsRepository.save(vitals));
    }

    @Transactional(readOnly = true)
    public List<VitalsResponseDto> getVitalsByPatient(Long patientId) {
        patientService.fetchPatientOrThrow(patientId);
        return vitalsRepository.findByPatientIdOrderByRecordedAtDesc(patientId)
                .stream()
                .map(VitalsService::toResponse)
                .toList();
    }

    public VitalsResponseDto updateVitals(Long vitalsId, VitalsRequestDto requestDto) {
        Vitals vitals = fetchVitalsOrThrow(vitalsId);
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        vitals.setPatient(patient);
        vitals.setRecordedAt(requestDto.getRecordedAt());
        vitals.setTemperature(requestDto.getTemperature());
        vitals.setBloodPressure(requestDto.getBloodPressure().trim());
        vitals.setPulse(requestDto.getPulse());
        vitals.setRespiratoryRate(requestDto.getRespiratoryRate());
        vitals.setOxygenSaturation(requestDto.getOxygenSaturation());
        vitals.setNurseName(requestDto.getNurseName().trim());

        return toResponse(vitalsRepository.save(vitals));
    }

    private Vitals fetchVitalsOrThrow(Long vitalsId) {
        return vitalsRepository.findById(vitalsId)
                .orElseThrow(() -> new ResourceNotFoundException("Vitals not found with id: " + vitalsId));
    }

    static VitalsResponseDto toResponse(Vitals vitals) {
        return VitalsResponseDto.builder()
                .id(vitals.getId())
                .patientId(vitals.getPatient().getId())
                .patientName(vitals.getPatient().getFirstName() + " " + vitals.getPatient().getLastName())
                .recordedAt(vitals.getRecordedAt())
                .temperature(vitals.getTemperature())
                .bloodPressure(vitals.getBloodPressure())
                .pulse(vitals.getPulse())
                .respiratoryRate(vitals.getRespiratoryRate())
                .oxygenSaturation(vitals.getOxygenSaturation())
                .nurseName(vitals.getNurseName())
                .build();
    }
}
