package com.example.demo.controller;

import com.example.demo.dto.queue.QueueRequestDto;
import com.example.demo.dto.queue.QueueResponseDto;
import com.example.demo.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('NURSE')")
    public QueueResponseDto createQueueEntry(@Valid @RequestBody QueueRequestDto requestDto) {
        return queueService.createQueueEntry(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE')")
    public List<QueueResponseDto> getQueue() {
        return queueService.getQueue();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public QueueResponseDto updateQueueEntry(@PathVariable("id") Long queueId, @Valid @RequestBody QueueRequestDto requestDto) {
        return queueService.updateQueueEntry(queueId, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NURSE')")
    public void deleteQueueEntry(@PathVariable("id") Long queueId) {
        queueService.deleteQueueEntry(queueId);
    }
}
