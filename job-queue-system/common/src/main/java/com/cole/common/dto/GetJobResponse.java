package com.cole.common.dto;

import com.cole.common.job.JobStatus;

import java.time.Instant;
import java.util.Map;

public record GetJobResponse(
        String id,
        String type,
        JobStatus status,
        Instant createdAt,
        Instant updatedAt,
        Map<String, Object> payload,
        String result,
        String errorMessage
) {}