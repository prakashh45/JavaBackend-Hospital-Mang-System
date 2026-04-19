package com.example.demo.service;

import com.example.demo.dto.queue.QueueRequestDto;
import com.example.demo.dto.queue.QueueResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.model.Queue;
import com.example.demo.model.QueueStatus;
import com.example.demo.repository.QueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QueueService {

    private final QueueRepository queueRepository;
    private final PatientService patientService;

    public QueueService(QueueRepository queueRepository, PatientService patientService) {
        this.queueRepository = queueRepository;
        this.patientService = patientService;
    }

    public QueueResponseDto createQueueEntry(QueueRequestDto requestDto) {
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        Queue queue = Queue.builder()
                .patient(patient)
                .tokenNumber(requestDto.getTokenNumber())
                .department(requestDto.getDepartment().trim())
                .status(requestDto.getStatus() == null ? QueueStatus.WAITING : requestDto.getStatus())
                .queuedAt(requestDto.getQueuedAt())
                .remarks(requestDto.getRemarks())
                .build();

        return toResponse(queueRepository.save(queue));
    }

    @Transactional(readOnly = true)
    public List<QueueResponseDto> getQueue() {
        return queueRepository.findAllByOrderByQueuedAtAsc()
                .stream()
                .map(QueueService::toResponse)
                .toList();
    }

    public QueueResponseDto updateQueueEntry(Long queueId, QueueRequestDto requestDto) {
        Queue queue = fetchQueueOrThrow(queueId);
        Patient patient = patientService.fetchPatientOrThrow(requestDto.getPatientId());

        queue.setPatient(patient);
        queue.setTokenNumber(requestDto.getTokenNumber());
        queue.setDepartment(requestDto.getDepartment().trim());
        queue.setStatus(requestDto.getStatus() == null ? queue.getStatus() : requestDto.getStatus());
        queue.setQueuedAt(requestDto.getQueuedAt() == null ? queue.getQueuedAt() : requestDto.getQueuedAt());
        queue.setRemarks(requestDto.getRemarks());

        return toResponse(queueRepository.save(queue));
    }

    public void deleteQueueEntry(Long queueId) {
        Queue queue = fetchQueueOrThrow(queueId);
        queueRepository.delete(queue);
    }

    private Queue fetchQueueOrThrow(Long queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue entry not found with id: " + queueId));
    }

    static QueueResponseDto toResponse(Queue queue) {
        return QueueResponseDto.builder()
                .id(queue.getId())
                .patientId(queue.getPatient().getId())
                .patientName(queue.getPatient().getFirstName() + " " + queue.getPatient().getLastName())
                .tokenNumber(queue.getTokenNumber())
                .department(queue.getDepartment())
                .status(queue.getStatus())
                .queuedAt(queue.getQueuedAt())
                .remarks(queue.getRemarks())
                .build();
    }
}
