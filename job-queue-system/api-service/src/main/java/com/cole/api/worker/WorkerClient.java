package com.cole.api.worker;

import com.cole.common.dto.GetJobResponse;
import com.cole.common.dto.SubmitJobRequest;
import com.cole.common.dto.SubmitJobResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

@Component
public class WorkerClient {

    private final RestClient rest;

    public WorkerClient(@Value("${worker.base-url}") String baseUrl) {
        this.rest = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public SubmitJobResponse submitToWorker(SubmitJobRequest req) {
        return rest.post()
                .uri("/internal/jobs")
                .body(req)
                .retrieve()
                .body(SubmitJobResponse.class);
    }

    public Optional<GetJobResponse> getFromWorker(String id) {
        try {
            GetJobResponse resp = rest.get()
                    .uri("/internal/jobs/{id}", id)
                    .retrieve()
                    .body(GetJobResponse.class);
            return Optional.ofNullable(resp);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }
}
