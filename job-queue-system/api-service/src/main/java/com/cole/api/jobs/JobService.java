package com.cole.api.jobs;

import com.cole.api.worker.WorkerClient;
import com.cole.common.dto.GetJobResponse;
import com.cole.common.dto.SubmitJobRequest;
import com.cole.common.dto.SubmitJobResponse;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final WorkerClient workerClient;

    public JobService(WorkerClient workerClient) {
        this.workerClient = workerClient;
    }

    public SubmitJobResponse submit(SubmitJobRequest req) {
        return workerClient.submitToWorker(req);
    }

    public GetJobResponse get(String id) {
        return workerClient.getFromWorker(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
    }
}

