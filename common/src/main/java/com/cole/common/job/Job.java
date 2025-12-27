package com.cole.common.job;

import java.time.Instant;
import java.util.Map;

public record Job(
        String id,
        String type,
        JobStatus status,
        Instant createdAt,
        Instant updatedAt,
        Map<String, Object> payload,
        String result,        // placeholder for now
        String errorMessage   // placeholder for now
) {}