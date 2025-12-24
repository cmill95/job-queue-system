package com.cole.worker.jobs;

import com.cole.common.job.Job;
import com.cole.common.job.JobStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobWorker {

    private final WorkerJobService service;

    public JobWorker(WorkerJobService service) {
        this.service = service;
    }

    // every 1 second, look for QUEUED jobs and process them
    @Scheduled(fixedDelay = 1000)
    public void processQueuedJobs() {
        for (Job job : service.allJobs()) {
            if (job.status() == JobStatus.QUEUED) {
                // “Claim” it
                service.markRunning(job.id());

                // Simulate some work
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                // Fake “result”
                String result = "processed type=" + job.type() + " payload=" + job.payload();
                service.markSucceeded(job.id(), result);
            }
        }
    }
}
