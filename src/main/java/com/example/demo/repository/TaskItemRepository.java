package com.example.demo.repository;

import com.example.demo.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findByWardIdOrderByCreatedAtDesc(String wardId);
    List<TaskItem> findByShiftDateOrderByCreatedAtDesc(LocalDate shiftDate);
    List<TaskItem> findByWardIdAndShiftDateOrderByCreatedAtDesc(String wardId, LocalDate shiftDate);
}
