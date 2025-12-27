package com.cole.common.dto;

import java.util.Map;

public record SubmitJobRequest(
        String type,
        Map<String, Object> payload
) {}