package com.example.demo.repository;

import com.example.demo.model.Queue;
import com.example.demo.model.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findAllByOrderByQueuedAtAsc();
    List<Queue> findByPatientIdOrderByQueuedAtDesc(Long patientId);
    long countByStatus(QueueStatus status);
}
