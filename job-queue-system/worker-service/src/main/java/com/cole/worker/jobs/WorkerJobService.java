package com.cole.worker.jobs;

import com.cole.common.dto.GetJobResponse;
import com.cole.common.dto.SubmitJobRequest;
import com.cole.common.dto.SubmitJobResponse;
import com.cole.common.job.Job;
import com.cole.common.job.JobStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class WorkerJobService {

    private final InMemoryJobRepository repo = new InMemoryJobRepository();

    public SubmitJobResponse submit(SubmitJobRequest req) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();

        Job job = new Job(
                id,
                req.type(),
                JobStatus.QUEUED,
                now,
                now,
                req.payload(),
                null,
                null
        );

        repo.save(job);
        return new SubmitJobResponse(id);
    }

    public GetJobResponse get(String id) {
        Job job = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        return toResponse(job);
    }

    public void markRunning(String id) {
        Job job = repo.findById(id).orElseThrow();
        Instant now = Instant.now();
        repo.save(new Job(
                job.id(), job.type(), JobStatus.RUNNING,
                job.createdAt(), now, job.payload(),
                job.result(), job.errorMessage()
        ));
    }

    public void markSucceeded(String id, String result) {
        Job job = repo.findById(id).orElseThrow();
        Instant now = Instant.now();
        repo.save(new Job(
                job.id(), job.type(), JobStatus.SUCCEEDED,
                job.createdAt(), now, job.payload(),
                result, null
        ));
    }

    public Iterable<Job> allJobs() {
        return repo.findAll();
    }

    private static GetJobResponse toResponse(Job job) {
        return new GetJobResponse(
                job.id(),
                job.type(),
                job.status(),
                job.createdAt(),
                job.updatedAt(),
                job.payload(),
                job.result(),
                job.errorMessage()
        );
    }
}
