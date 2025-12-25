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

    @Scheduled(fixedDelay = 250)
    public void processQueuedJobs() {
        for (Job job : service.allJobs()) {
            if (job.status() != JobStatus.QUEUED) continue;

            service.markRunning(job.id());

            try {
                // Simulate/perform work based on job type
                String result = execute(job);

                // Add a little delay so RUNNING is observable
                Thread.sleep(1500);

                service.markSucceeded(job.id(), result);
            } catch (Exception e) {
                service.markFailed(job.id(), e.getMessage());
            }
        }
    }

    private String execute(Job job) throws InterruptedException {
        return switch (job.type()) {
            case "echo" -> "echo payload=" + job.payload();

            case "uppercase" -> {
                Object msg = job.payload().get("msg");
                if (msg == null) throw new IllegalArgumentException("payload.msg is required");
                yield msg.toString().toUpperCase();
            }

            case "sleep" -> {
                Object msObj = job.payload().getOrDefault("ms", 2000);
                long ms = Long.parseLong(msObj.toString());
                Thread.sleep(ms);
                yield "slept " + ms + "ms";
            }

            case "fail" -> throw new RuntimeException("Intentional failure requested");

            default -> throw new IllegalArgumentException("Unknown job type: " + job.type());
        };
    }

}
