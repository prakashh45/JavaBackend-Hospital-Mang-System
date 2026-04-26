package com.example.demo.repository;

import com.example.demo.model.NurseNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NurseNoteRepository extends JpaRepository<NurseNote, Long> {
    List<NurseNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
