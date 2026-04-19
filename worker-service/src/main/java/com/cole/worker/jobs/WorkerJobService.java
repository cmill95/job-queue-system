package com.cole.worker.jobs;

import com.cole.common.dto.GetJobResponse;
import com.cole.common.dto.SubmitJobRequest;
import com.cole.common.dto.SubmitJobResponse;
import com.cole.common.job.Job;
import com.cole.common.job.JobStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkerJobService {

    private final JobRepository repo;
    private final ObjectMapper objectMapper;

    public WorkerJobService(JobRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    public SubmitJobResponse submit(SubmitJobRequest req) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        repo.save(new JobEntity(id, req.type(), JobStatus.QUEUED.name(),
                now, now, toJson(req.payload()), null, null, true));
        return new SubmitJobResponse(id);
    }

    public GetJobResponse get(String id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
    }

    @Transactional
    public void markRunning(String id) {
        JobEntity job = repo.findById(id).orElseThrow();
        repo.save(new JobEntity(id, job.getType(), JobStatus.RUNNING.name(),
                job.getCreatedAt(), Instant.now(), job.getPayload(), job.getResult(), job.getErrorMessage()));
    }

    @Transactional
    public void markSucceeded(String id, String result) {
        JobEntity job = repo.findById(id).orElseThrow();
        repo.save(new JobEntity(id, job.getType(), JobStatus.SUCCEEDED.name(),
                job.getCreatedAt(), Instant.now(), job.getPayload(), result, null));
    }

    @Transactional
    public void markFailed(String id, String errorMessage) {
        JobEntity job = repo.findById(id).orElseThrow();
        repo.save(new JobEntity(id, job.getType(), JobStatus.FAILED.name(),
                job.getCreatedAt(), Instant.now(), job.getPayload(), null, errorMessage));
    }

    public List<Job> findQueued() {
        return repo.findByStatus(JobStatus.QUEUED.name()).stream()
                .map(this::toDomain)
                .toList();
    }

    private Job toDomain(JobEntity e) {
        return new Job(e.getId(), e.getType(), JobStatus.valueOf(e.getStatus()),
                e.getCreatedAt(), e.getUpdatedAt(), fromJson(e.getPayload()),
                e.getResult(), e.getErrorMessage());
    }

    private GetJobResponse toResponse(JobEntity e) {
        return new GetJobResponse(e.getId(), e.getType(), JobStatus.valueOf(e.getStatus()),
                e.getCreatedAt(), e.getUpdatedAt(), fromJson(e.getPayload()),
                e.getResult(), e.getErrorMessage());
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }

    private Map<String, Object> fromJson(String json) {
        if (json == null) return Map.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize payload", e);
        }
    }
}
